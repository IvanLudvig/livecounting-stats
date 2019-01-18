package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class KParts extends Stat {
	
	int last[];
	//int all[];
	//int part[];
	//double percentage[];
	int top = 0;
	Main main;

	public KParts(Main main) {
		this.main = main;
		counts = new int[main.n];  //13000 - 13M 
		last = new int[main.n];
		//all = new int[2800];
		//part = new int[2800];
		//percentage = new double[2800];
	}
	
	@Override
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(top==0) {
					top=(message.count/1000)+1;
				}
				if(((message.count/1000)+1)<=top) {
					if(last[main.users.indexOf(message.author)]!=(message.count/1000)+1) {
						counts[main.users.indexOf(message.author)]+=1;
						last[main.users.indexOf(message.author)]=message.count/1000+1;
					}
				}
			}
		}
	}
	
	@Override
	public void write() {
		for(String user : main.users) {
			int all = top - last[main.users.indexOf(user)] +1;
			lines.add(new Line(user, Math.round(((double)counts[main.users.indexOf(user)]/all*100)*100.0)/100.0));
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/kparts.txt"));
	    	writer.write("| # |Username |Percentage ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(line.percentage>=2) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	    lines.clear();
		for(String user : main.users) {
			int all = top - last[main.users.indexOf(user)] +1;
			lines.add(new Line(user,counts[main.users.indexOf(user)]));
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/kpartstotal.txt"));
	    	writer.write("| # |Username |K's Participated ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(line.count>=5) {
					writer.write(line.getTableString(lines.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}
}
