package ivanludvig.livecounting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import ivanludvig.livecounting.stats.Assists;
import ivanludvig.livecounting.stats.AverageCounts;
import ivanludvig.livecounting.stats.Bars;
import ivanludvig.livecounting.stats.CountPercent;
import ivanludvig.livecounting.stats.Counts;
import ivanludvig.livecounting.stats.DayParts;
import ivanludvig.livecounting.stats.DayStreak;
import ivanludvig.livecounting.stats.FavouriteCounter;
import ivanludvig.livecounting.stats.FirstCounts;
import ivanludvig.livecounting.stats.Gets;
import ivanludvig.livecounting.stats.HoE;
import ivanludvig.livecounting.stats.HoP;
import ivanludvig.livecounting.stats.Hours;
import ivanludvig.livecounting.stats.KParts;
import ivanludvig.livecounting.stats.OddEven;
import ivanludvig.livecounting.stats.OneKStreak;
import ivanludvig.livecounting.stats.Pairs;
import ivanludvig.livecounting.stats.Pee;
import ivanludvig.livecounting.stats.Pincus;
import ivanludvig.livecounting.stats.Stat;
import ivanludvig.livecounting.stats.TenToHundredK;
import ivanludvig.livecounting.stats.TopStreaks;
import ivanludvig.livecounting.stats.TwentyK;


public class Main implements Runnable {
	
	static Main main;
	public ArrayList<Message> messages = new ArrayList<Message>();
	public ArrayList<String> users = new ArrayList<String>();
	public ArrayList<Stat> stats = new ArrayList<Stat>();
	int latestcount = 0;
	String lastdate = "0";
	String ld = "0";
	public final int n = 5200;
	GUI gui;
	SimpleDateFormat sdfUTC, sdfEST, sdfESThour;
	
	public static void main(String args[]) throws IOException {
		main = new Main();
		main.gui = new GUI(main);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		main.sdfUTC =  new SimpleDateFormat("dd/MM/yyyy");
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
		main.sdfEST =  new SimpleDateFormat("dd/MM/yyyy");
		main.sdfESThour =  new SimpleDateFormat("HH");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		//main.gui.start();
	}
	
	int rev = 0;
	int hop = 0;
	public void start(int a[]) throws IOException {
		int assists = 0, gets = 0, counts = 0, kparts = 0, days = 0;
		if(a[0]==1) {
			stats.add(new Pairs(this));
		}
		if(a[1]==1) {
			stats.add(new FavouriteCounter(this));
		}
		if(a[2]==1) {
			stats.add(new HoE(this));
		}
		if(a[3]==1) {
			stats.add(new Hours(this));
		}
		if(a[4]==1) {
			stats.add(new Bars(this));
		}
		if(a[5]==1) {
			stats.add(new OddEven(this));
		}
		if(a[6]==1) {
			stats.add(new Pincus(this));
		}
		if(a[7]==1) {
			stats.add(new TwentyK(this));
		}
		if(a[8]==1) {
			stats.add(new DayStreak(this));
		}
		if(a[9]==1) {
			stats.add(new FirstCounts(this));
		}
		if(a[10]==1) {
			stats.add(new OneKStreak(this));
		}
		if(a[11]==1) {
			stats.add(new TopStreaks(this));
		}
		if(a[12]==1) {
			stats.add(new Pee(this));
		}
		if(a[13]==1) {
			stats.add(new CountPercent(this));
		}
		if(a[14]==1) {
			stats.add(new KParts(this));
			kparts = stats.size()-1;
		}
		if(a[15]==1) {
			stats.add(new Counts(this));
			counts = stats.size()-1;
		}
		if(a[16]==1) {
			stats.add(new Gets(this));
			gets = stats.size()-1;
		}
		if(a[17]==1) {
			stats.add(new Assists(this));
			assists = stats.size()-1;
		}
		if(a[18]==1) {
			stats.add(new DayParts(this));
			days = stats.size()-1;
		}
		if(a[19]==1) {
			//System.out.println(assists+" "+ gets+" "+ counts+" "+ kparts+" "+ days);
			stats.add(new HoP(this, assists, gets, counts, kparts, days));
			hop = 1;
		}
		if(a[20]==1) {
			stats.add(new TenToHundredK(this));
			rev = 1;
		}
		if(a[21]==1) {
			stats.add(new AverageCounts(this));
			rev = 1;
		}
		/*
		if(a[22]==1) {
			stats.add(new Speed(this));
			rev = 1;
		}
		*/
		main.reset();
		if(rev == 0) {
			main.getJson();
		}else {
			main.reversedGetJson();
		}
		gui.updateProgress("Saving...");
		System.out.println("Saving...");
		main.write();
		gui.setGreenColour();
		gui.updateProgress("Done!");
		System.out.println("Done!");
		//gui.updateProgress("  Updated up to "+main.latestcount);
		//System.out.print("  Updated up to "+main.latestcount);
	}
	
	//int all = 0;
	//int delta = 0;
	//int first, end = 0;
	public void getJson() throws IOException {
		BufferedReader br = null;
		int messagesnum = 0;
		try {
			int last = readLastChatFile(br);
			int lastcount = readLastCount(br);
			lastdate = readLastDate(br);
			main.latestcount = lastcount;
			
			JsonParser parser = new JsonParser();
			JsonReader reader;
			Gson gson = new GsonBuilder().create();
			
			for(int i = 0; i <= last; i++) {
				if((last-i)!=82 && (last-i)!=83 && (last-i)!=84) {
					br = new BufferedReader(new FileReader("res/chat"+Integer.toString(last-i)+".json"));
					reader = new JsonReader(br);
					gui.updateProgress("reading res/chat"+(last-i)+".json...");
					System.out.println("reading res/chat"+(last-i)+".json...");
					//JsonArray array = (JsonArray) parser.parse(br).getAsJsonArray();
					//gui.updateProgress("messages in file: "+array.size());
					//System.out.println("messages in file: "+array.size());
					/*
					for(int j = 0; j < array.size(); j++) {
						/*
						Message m = new Message(main, array.get(j).getAsJsonObject());
						main.messages.add(m);
						if(m.ok==0) {
							all+=1;
						}
						*/
						//main.messages.add(new Message(main, array.get(j).getAsJsonObject()));
					//}
				
					//reader.setLenient(true);
					reader.beginArray();
					
			        while (reader.hasNext()) {
			        	main.messages.add(new Message(main, gson.fromJson(reader, JsonObject.class)));
			        	update();
			        	messagesnum+=1;
			        	//System.out.println(n);
			        	messages.clear();
			        }
			        System.out.println("messages in file: "+messagesnum);
			        gui.updateProgress("messages in file: "+messagesnum);		
			        messagesnum=0;
					/*
					Message m = messages.get(0);
					int p = 0;
					while(m.ok!=0) {
						p+=1;
						m = messages.get(p);
					}
					first = m.count;
					
					m = messages.get(messages.size()-1);
					p = messages.size()-1;
					while(m.ok!=0) {
						p-=1;
						m = messages.get(p);
					}
					end = m.count;
					delta = all-Math.abs(end - first);
					*/
					//System.out.println("DELTA "+delta+" "+all+" | "+first+" "+end);
					//all=0;
			        /*
					int n=0;
					Message check = messages.get(n);
					if(i==0) {
						while(check.ok==1) {
							n++;
							check = messages.get(n);
						}
						if(check.ok==0) {
							gui.updateProgress("latest count: "+check.count);
						    System.out.println("latest count: "+check.count);
						   	main.latestcount = check.count;
						   	ld = main.dateof(check);
						}
					}
					update();*/
				}
			}
			/*
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/lastcount.txt"));
	    	writer.write(Integer.toString(main.latestcount));
	    	writer.close();
	    	main.saveLastDate(writer);
	    	*/

		} finally {
			br.close();
		}
	}
	
	public void reversedGetJson() throws IOException {
		BufferedReader br = null;
		try {
			int last = readLastChatFile(br);
			int lastcount = readLastCount(br);
			lastdate = readLastDate(br);
			main.latestcount = lastcount;
			
			JsonParser parser = new JsonParser();
			for(int i = 0; i <= last; i++) {
				if(i!=82 && i!=83 && i!=84) {
					br = new BufferedReader(new FileReader("res/chat"+Integer.toString(i)+".json"));
					gui.updateProgress("reading res/chat"+i+".json...");
					System.out.println("reading res/chat"+i+".json...");
					JsonArray array = (JsonArray) parser.parse(br).getAsJsonArray();
					gui.updateProgress("messages in file: "+array.size());
					System.out.println("messages in file: "+array.size());
					for(int j = array.size()-1; j >= 0 ; j--) {
						main.messages.add(new Message(main, array.get(j).getAsJsonObject()));
					}
					int n=0;
					Message check = new Message(main, array.get(n).getAsJsonObject());
					if(i==last) {
						while(check.ok==1) {
							n++;
							check = new Message(main, array.get(n).getAsJsonObject());
						}
						if(check.ok==0) {
							gui.updateProgress("latest count: "+check.count);
						   	System.out.println("latest count: "+check.count);
						   	main.latestcount = check.count;
						    ld = main.getUTCDate(check);
						}
					}
					reversedUpdate();
				}
			}
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/lastcount.txt"));
	    	writer.write(Integer.toString(main.latestcount));
	    	writer.close();
	    	//main.saveLastDate(writer);

		} finally {
			br.close();
		}
	}
	
	public String readLastDate(BufferedReader br) throws NumberFormatException, IOException {
		br = new BufferedReader(new FileReader("res/lastDate.txt"));
		String lastDate = br.readLine();
		br.close();
		return lastDate;
	}
	
	
	public int readLastChatFile(BufferedReader br) throws NumberFormatException, IOException {
		br = new BufferedReader(new FileReader("res/lastChatFile.txt"));
		int last = Integer.parseInt(br.readLine());
		br.close();
		return last;
	}
	
	public int readLastCount(BufferedReader br) throws NumberFormatException, IOException {
		br = new BufferedReader(new FileReader("res/lastcount.txt"));
		int lastcount = Integer.parseInt(br.readLine());
		br.close();
		return lastcount;
	}
	
	public void saveLastDate(BufferedWriter writer) throws IOException {
    	writer = new BufferedWriter(new FileWriter("res/lastDate.txt"));
    	writer.write(main.ld);
    	writer.close();
	}
	
	public void reset() throws IOException {
    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/lastcount.txt"));
    	writer.write("0");
    	writer.close();
    	writer = new BufferedWriter(new FileWriter("res/lastDate.txt"));
    	writer.write("0000000");
    	writer.close();
	}
	
	/*
	public String getESTDate(Message message, int sdf) {
		//TimeZone.setDefault(TimeZone.getTimeZone("EST"));
		Date date = new Date((Long.valueOf(message.date)*1000));
		System.out.println(sdf.format(date)+" "+message.date);
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}	
	
	public String getUTCDate(Message message, int sdf) {
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Date date = new Date((Long.valueOf(message.date))*1000);
		return sdf.format(date);
	}
	*/
	
	public String getESTDate(Message message) {
		Date date = new Date((Long.valueOf(message.date))*1000);
		return sdfEST.format(date);
	}
	
	public String getESThour(Message message) {
		Date date = new Date((Long.valueOf(message.date))*1000);
		return sdfESThour.format(date);
	}
	
	public String getUTCDate(Message message) {
		Date date = new Date((Long.valueOf(message.date))*1000);
		return sdfUTC.format(date);
	}
	
	
	public void update() {
		for (Stat stat : stats) {
			stat.update();
		}
		messages = new ArrayList<Message>();
	}
	
	public void reversedUpdate(){
		for (Stat stat : stats) {
			stat.update();
		}
		messages = new ArrayList<Message>();
	}
	
	public void write() {
		for (Stat stat : stats) {
			stat.write();
		}
		gui.updateProgress("number of users: "+users.size());
		System.out.println("number of users: "+users.size());
	}

	@Override
	public void run() {
		try {
			start(main.gui.a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
