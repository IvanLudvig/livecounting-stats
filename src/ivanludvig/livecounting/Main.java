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
import ivanludvig.livecounting.stats.DayStreak;
import ivanludvig.livecounting.stats.FavouriteCounter;
import ivanludvig.livecounting.stats.FirstCounts;
import ivanludvig.livecounting.stats.HoE;
import ivanludvig.livecounting.stats.Hours;
import ivanludvig.livecounting.stats.KParts;
import ivanludvig.livecounting.stats.OddEven;
import ivanludvig.livecounting.stats.OneKStreak;
import ivanludvig.livecounting.stats.Pairs;
import ivanludvig.livecounting.stats.Pee;
import ivanludvig.livecounting.stats.Pincus;
import ivanludvig.livecounting.stats.TopStreaks;
import ivanludvig.livecounting.stats.TwentyK;
import ivanludvig.livecounting.stats.p5m.OneKDays;


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
	Pincus pincus;
	TwentyK twentyk;
	DayStreak daystreak;
	FirstCounts firstcounts;
	OneKStreak onekstreak;
	TopStreaks topstreaks;
	OneKDays onekdays;
	Pee pee;
	KParts kparts;
	//NotP5M notp5m;
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
		main.pincus = new Pincus(main);
		main.twentyk = new TwentyK(main);
		main.daystreak = new DayStreak(main);
		main.firstcounts = new FirstCounts(main);
		main.onekstreak = new OneKStreak(main);
		main.topstreaks=new TopStreaks(main);
		//main.notp5m=new NotP5M(main);
		main.onekdays=new OneKDays(main);
		main.pee = new Pee(main);
		main.kparts=new KParts(main);
		//main.read();
		main.reset();
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
								main.twentyk.lastupdate();
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
		main.favourite.read();           //daystreak and favourite stats should be run using the full data
		main.hoe.read();
		main.hours.read();
		main.bars.read();
		main.oddeven.read();
		main.pincus.read();
		main.twentyk.read();
		main.firstcounts.read();
		main.firstcounts.read();
		main.onekstreak.read();
		main.topstreaks.read();
	}
	
	public void update() {
		main.favourite.update();
		main.pairs.update();
		main.hoe.update();
		main.hours.update();
		main.oddeven.update();
		main.pincus.update();
		main.twentyk.update();
		main.daystreak.update();
		main.firstcounts.update();
		main.onekstreak.update();
		main.topstreaks.update();
		//main.notp5m.update();
		main.onekdays.update();
		main.pee.update();
		main.kparts.update();
		main.bars.update();             //bars are last
		messages = new ArrayList<Message>();
	}
	public void write() {
		main.bars.write();
		main.hoe.write();
		main.favourite.write();
		main.pairs.write();
		main.hours.write();
		main.oddeven.write();
		main.pincus.write();
		main.twentyk.write();
		main.daystreak.write();
		main.firstcounts.write();
		main.onekstreak.write();
		main.topstreaks.write();
		//main.notp5m.write();
		main.onekdays.write();
		main.pee.write();
		main.kparts.write();
	}
	

}
