package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class TenToHundredK extends Stat {
	
	int counts[];
	int tenk[];
	int hundredk[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public TenToHundredK(Main main) {
		this.main = main;
		counts = new int[main.n];
		tenk = new int[main.n];
		hundredk = new int[main.n];
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok==0) {
				counts[main.users.indexOf(message.author)]+=1;
				if(counts[main.users.indexOf(message.author)]==10000) {
					tenk[main.users.indexOf(message.author)]=Integer.parseInt(message.date);
				}else if (counts[main.users.indexOf(message.author)]==100000) {
					hundredk[main.users.indexOf(message.author)]=Integer.parseInt(message.date);
				}
			}
		}
	}
	
	public void write() {
		for(String str : main.users) {
			if(hundredk[main.users.indexOf(str)]!=0) {
				int delta=hundredk[main.users.indexOf(str)]-tenk[main.users.indexOf(str)];
				int days=delta/60/60/24;
				int hours=(delta-(days*60*60*24))/60/60;
				int minutes=(delta-((days*60*60*24)+(hours*60*60)))/60;
				//String delta = String.valueOf((hundreadk[main.users.indexOf(str)]-tenk[main.users.indexOf(str)]));
				if(minutes<10 && hours<10 ) {
					lines.add(new Line(str, delta,  days+" days "+"0"+hours+":"+"0"+minutes));
				}else if(minutes<10 && hours>=10) {
					lines.add(new Line(str, delta,  days+" days "+hours+":"+"0"+minutes));
				}else if(minutes>=10 && hours<10) {
					lines.add(new Line(str, delta,  days+" days "+"0"+hours+":"+minutes));
				}else {
					lines.add(new Line(str, delta,  days+" days "+hours+":"+minutes));
				}
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/10kto100k.txt"));
	    	writer.write("| # |Username |Time ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getTTString(lines.indexOf(line)+1));
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}

}
