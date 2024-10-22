package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class Counts extends Stat{
	
	Main main;

	public Counts(Main main) {
		this.main = main;
		counts = new int[main.n];
	}
	@Override
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				counts[main.users.indexOf(message.author)]+=1;
			}
		}
	}
	
	@Override
	public void write() {
		System.out.println("write counts");
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>=100) {
				lines.add(new Line(str, counts[main.users.indexOf(str)]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/counts.txt"));
	    	writer.write("| # |Username |Counts");
			writer.newLine();
	    	writer.write("|---|------|---------------");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getTableString(lines.indexOf(line)+1));
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}
}
