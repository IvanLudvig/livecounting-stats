package ivanludvig.livecounting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import ivanludvig.livecounting.stats.Bars;
import ivanludvig.livecounting.stats.FavouriteCounter;
import ivanludvig.livecounting.stats.HoE;
import ivanludvig.livecounting.stats.Hours;
import ivanludvig.livecounting.stats.OddEven;
import ivanludvig.livecounting.stats.Pairs;


public class Main {
	
	static Main main;
	public ArrayList<Message> messages = new ArrayList<Message>();
	public ArrayList<String> users = new ArrayList<String>();
	Pairs pairs;
	FavouriteCounter favourite;
	HoE hoe;
	Hours hours;
	Bars bars;
	OddEven oddeven;
	int latestcount = 0;
	String lastdate = "0";
	String ld = "0";
	
	public static void main(String args[]) throws IOException {
		main = new Main();
		main.pairs = new Pairs(main);
		main.favourite = new FavouriteCounter(main);
		main.hoe = new HoE(main);
		main.hours = new Hours(main);
		main.bars = new Bars(main);
		main.oddeven = new OddEven(main);
		main.read();
		//main.reset();
		main.getJson();
		System.out.println("Saving...");
		main.write();
		System.out.println("Done!");
		System.out.print("  Updated up to "+main.latestcount);
	}
	
	public void getJson() throws IOException {
		BufferedReader br = null;
		try {
			int last = readLastChatFile(br);
			int lastcount = readLastCount(br);
			lastdate = readLastDate(br);
			main.latestcount = lastcount;
			
			JsonParser parser = new JsonParser();
			for(int i = 0; i <= last; i++) {
				if((last-i)!=82 && (last-i)!=83 && (last-i)!=84) {
					br = new BufferedReader(new FileReader("res/chat"+Integer.toString(last-i)+".json"));
					System.out.println("reading res/chat"+(last-i)+".json...");
					JsonArray array = (JsonArray) parser.parse(br).getAsJsonArray();
					System.out.println("messages in file: "+array.size());
					for(int j = 0; j < array.size(); j++) {
						main.messages.add(new Message(main, array.get(j).getAsJsonObject()));
					}
					int n=0;
					Message check = new Message(main, array.get(n).getAsJsonObject());
					while(check.ok==1) {
						n++;
						check = new Message(main, array.get(n).getAsJsonObject());
					}
					if(check.ok==0) {
						if(check.count<=lastcount+1 
								&& (!lastdate.equals(main.hoe.dateof(check)))) {
							break;
						}
						else if(lastdate.equals(main.hoe.dateof(check))){
							if(i!=0) {
								//br = new BufferedReader(new FileReader("res/chat"+Integer.toString(last-i)+".json"));
								//JsonArray lastarray = (JsonArray) parser.parse(br).getAsJsonArray();
								//for(int j = 0; j < lastarray.size(); j++) {
								//	main.messages.add(new Message(main, lastarray.get(j).getAsJsonObject()));
								//}
								main.hoe.lastupdate();
								break;
							}else if(i==0) {
								break;
							}
						}
						if(i==0) {
					    	System.out.println("latest count: "+check.count);
					    	main.latestcount = check.count;
					    	ld = main.hoe.dateof(check);
						}
					}
					update();
				}
			}
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/lastcount.txt"));
	    	writer.write(Integer.toString(main.latestcount));
	    	writer.close();
	    	main.saveLastDate(writer);

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
	
	public void read() {
		main.pairs.read();
		main.favourite.read();
		main.hoe.read();
		main.hours.read();
		main.bars.read();
		main.oddeven.read();
	}
	
	public void update() {
		main.favourite.update();
		main.pairs.update();
		main.hoe.update();
		main.hours.update();
		main.bars.update();
		main.oddeven.update();
		messages = new ArrayList<Message>();
	}
	public void write() {
		main.bars.write();
		main.hoe.write();
		main.favourite.write();
		main.pairs.write();
		main.hours.write();
		main.oddeven.write();
	}
	

}
