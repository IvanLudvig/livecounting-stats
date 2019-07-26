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

public class Pincus extends Stat {
	
	int counts[][];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public Pincus(Main main) {
		this.main = main;
		counts = new int[main.n][4];
	}
	
	public void update() {
		for(Message message : main.messages) {
			String str = Integer.toString(message.count);
			if(message.body.contains("{:'-D")||message.body.contains("{ :'-D")) {
				counts[main.users.indexOf(message.author)][0]+=1;
				counts[main.users.indexOf(message.author)][1]+=1;
			}
			if(message.body.contains("{:}")) {
				counts[main.users.indexOf(message.author)][0]+=1;
				counts[main.users.indexOf(message.author)][2]+=1;
			}
			if(message.body.contains("{:'(")) {
				counts[main.users.indexOf(message.author)][0]+=1;
				counts[main.users.indexOf(message.author)][3]+=1;
			}
		}
	}
	
	public void write() {
		for(String str : main.users) {
			if((counts[main.users.indexOf(str)][0]+counts[main.users.indexOf(str)][1]+counts[main.users.indexOf(str)][2])>0) {
				lines.add(new Line(str, counts[main.users.indexOf(str)]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.counts[0]));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/pincus.txt"));
	    	writer.write("| # |Username |Total |{:'-D  |{:} |{:'( ");
			writer.newLine();
	    	writer.write("|---|---|---|---|---|---|");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getPincusString(lines.indexOf(line)+1));
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
				l.counts[0]+=line.counts[0];
				l.counts[1]+=line.counts[1];
				l.counts[2]+=line.counts[2];
				l.counts[3]+=line.counts[3];
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
		    reader = new BufferedReader(new FileReader("output/pincus.txt"));
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
