package ivanludvig.livecounting.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class OddEven {
	
	int odd[];
	int even[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;
	
	public OddEven(Main main) {
		this.main = main;
		odd = new int[1800];
		even = new int[1800];
	}
	
	public void update() {
		Collections.reverse(main.messages);
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(message.count % 2 == 0) {
					even[main.users.indexOf(message.author)]+=1;
				}else {
					odd[main.users.indexOf(message.author)]+=1;
				}
			}
		}
		Collections.reverse(main.messages);
	}
	
	
	public void write() {
		for(int i = 0; i<main.users.size(); i++) {
			addLine(new Line(main.users.get(i), odd[i], even[i]));
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/oddeven.txt"));
	    	writer.write("| # |Username |Total counts |% Odd |% Even ");
			writer.newLine();
	    	writer.write("|---|---|---|---|---|");
			writer.newLine();
			for(Line line : lines) {
				if(line.count>=6) {
					writer.write(line.getOEString(lines.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
			writer = new BufferedWriter(new FileWriter("output/full/oddevenfull.txt"));
			for(Line line : lines) {
				if(line.count>0) {
					writer.write(line.getOEFullString(lines.indexOf(line)+1));
					writer.newLine();
				}
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
				l.odd+=line.odd;
				l.even+=line.even;
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
		    reader = new BufferedReader(new FileReader("output/full/oddevenfull.txt"));
		    String line;
		    while ((line = reader.readLine()) != null) {
		        lines.add(new Line(line));
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
