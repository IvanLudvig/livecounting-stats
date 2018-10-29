package ivanludvig.livecounting.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class Pee {
	
	int counts[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public Pee(Main main) {
		this.main = main;
		counts = new int[main.n];
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.author!=null) {
				if(message.body.contains(" pee") || message.body.contains("pee ") || 
						message.body.contains("Pee ")|| message.body.contains(" Pee")) {
					counts[main.users.indexOf(message.author)]+=1;
				}
			}

		}
	}
	
	public void write() {
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>0) {
				lines.add(new Line(str, counts[main.users.indexOf(str)]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/pee.txt"));
	    	writer.write("| # |Username |Count ");
			writer.newLine();
	    	writer.write("|---|---|---");
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
	
	public void addLine(Line line) {
		int exists = 0;
		for(Line l : lines) {
			if(l.user.equals(line.user)) {
				l.count+=line.count;
				exists = 1;
				break;
			}
		}
		if(exists == 0 && line.count>0) {
			lines.add(line);
		}
	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/pee.txt"));
		    String line;
		    line = reader.readLine();
		    line = reader.readLine();
		    while ((line = reader.readLine()) != null) {
		        addLine(new Line(line));
		    }

		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
	}
}
