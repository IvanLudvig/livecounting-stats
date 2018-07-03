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
	}
	
	public void getJson() throws IOException {
		BufferedReader br = null;
		int latestcount = 0;
		try {
			br = new BufferedReader(new FileReader("res/last.txt"));
			int last = Integer.parseInt(br.readLine());
			br.close();
			br = new BufferedReader(new FileReader("res/lastcount.txt"));
			int lastcount = Integer.parseInt(br.readLine());
			br.close();
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
					if(check.ok==0) {
						if(check.count<=lastcount+1) {
							break;
						}
						if(i==0) {
					    	System.out.println("latest count: "+check.count);
					    	latestcount = check.count;
						}
					}else {
						while(check.ok==1) {
							n++;
							check = new Message(main, array.get(n).getAsJsonObject());
						}
					}
					update();
				}
			}
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/lastcount.txt"));
	    	writer.write(Integer.toString(latestcount));
	    	writer.close();

		} finally {
			br.close();
		}
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
