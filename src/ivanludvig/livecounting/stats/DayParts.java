package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class DayParts extends Stat {
	
	int counts[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public DayParts(Main main) {
		this.main = main;
		counts = new int[main.n];
	}
	
	@Override
	public void update() {
		String currentdate="";
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(!main.dateof(message).equals(currentdate)) {
					counts[main.users.indexOf(message.author)]+=1;
					currentdate=main.dateof(message);
				}
			}
		}
	}
	
	@Override
	public void write() {
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]!=0) {
				lines.add(new Line(str, counts[main.users.indexOf(str)]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/dayparts.txt"));
			for(Line line : lines) {
				writer.write(line.getTableString(lines.indexOf(line)));
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    } 
	}

}
