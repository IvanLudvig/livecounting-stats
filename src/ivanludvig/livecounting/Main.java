package ivanludvig.livecounting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import ivanludvig.livecounting.stats.Counts;
import ivanludvig.livecounting.stats.FavouriteCounter;
import ivanludvig.livecounting.stats.HoE;
import ivanludvig.livecounting.stats.Pairs;
import ivanludvig.livecounting.stats.SixSixSix;


public class Main {
	
	static Main main;
	public ArrayList<Message> messages = new ArrayList<Message>();
	public ArrayList<String> users = new ArrayList<String>();
	Counts counts;
	SixSixSix six;
	Pairs pairs;
	FavouriteCounter favourite;
	HoE hoe;
	int latestcount = 0;
	String lastdate = "0";
	
	public static void main(String args[]) throws IOException {
		main = new Main();
		main.pairs = new Pairs(main);
		main.favourite = new FavouriteCounter(main);
		main.hoe = new HoE(main);
		main.read();
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
							main.hoe.lastupdate();
							if(i!=last) {
								br = new BufferedReader(new FileReader("res/chat"+Integer.toString(last-i)+".json"));
								System.out.println("reading res/chat"+(last-i)+".json...");
								JsonArray lastarray = (JsonArray) parser.parse(br).getAsJsonArray();
								System.out.println("messages in file: "+lastarray.size());
								for(int j = 0; j < lastarray.size(); j++) {
									main.messages.add(new Message(main, lastarray.get(j).getAsJsonObject()));
								}
								main.hoe.lastupdate();
								break;
							}
						}
						if(i==0) {
					    	System.out.println("latest count: "+check.count);
					    	latestcount = check.count;
					    	lastdate = main.hoe.dateof(check);
						}
					}
					update();
				}
			}
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/lastcount.txt"));
	    	writer.write(Integer.toString(latestcount));
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
    	writer.write(lastdate);
    	writer.close();
	}
	
	public void read() {
		main.pairs.read();
		main.favourite.read();
		main.hoe.read();
	}
	
	public void update() {
		main.favourite.update();
		main.pairs.update();
		main.hoe.update();
		messages = new ArrayList<Message>();
	}
	public void write() {
		main.hoe.write();
		main.favourite.write();
		main.pairs.write();
	}
	

}
