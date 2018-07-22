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
import java.util.Date;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class Hours {

	
	int counts[][];
	ArrayList<Line> lines = new ArrayList<Line>();
	SimpleDateFormat sdf;
	Main main;
	String date = "0";
	

	public Hours(Main main) {
		this.main = main;
		counts = new int[1800][26];
		sdf =  new SimpleDateFormat("HH");
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				counts[main.users.indexOf(message.author)][0]+=1;
				counts[main.users.indexOf(message.author)][hourof(message)+1]+=1;
			}
		}
	}
	
	
	public void write() {

		for(String str : main.users) {
			if(counts[main.users.indexOf(str)][0]!=0) {
				addLine(new Line(str, counts[main.users.indexOf(str)]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.counts[0]));
		Collections.reverse(lines);

	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/hours.txt"));
			writer.write("| Username             | Total      | Hour 00-01 | Hour 01-02 | Hour 02-03 | Hour 03-04 | Hour 04-05 | Hour 05-06 | Hour 06-07 | Hour 07-08 | Hour 08-09 | Hour 09-10 | Hour 10-11 | Hour 11-12 | Hour 12-13 | Hour 13-14 | Hour 14-15 | Hour 15-16 | Hour 16-17 | Hour 17-18 | Hour 18-19 | Hour 19-20 | Hour 20-21 | Hour 21-22 | Hour 22-23 | Hour23-24 |"); 
			writer.newLine();
			writer.write("|----------------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|-----------|");
			writer.newLine();
			
			for(Line line : lines) {
				if(line.counts[0]>=100) {
					writer.write(line.getHourString());
					writer.newLine();
				}
			}
			writer.close();
			writer = new BufferedWriter(new FileWriter("output/full/hoursfull.txt"));
			writer.write("| Username             | Total      | Hour 00-01 | Hour 01-02 | Hour 02-03 | Hour 03-04 | Hour 04-05 | Hour 05-06 | Hour 06-07 | Hour 07-08 | Hour 08-09 | Hour 09-10 | Hour 10-11 | Hour 11-12 | Hour 12-13 | Hour 13-14 | Hour 14-15 | Hour 15-16 | Hour 16-17 | Hour 17-18 | Hour 18-19 | Hour 19-20 | Hour 20-21 | Hour 21-22 | Hour 22-23 | Hour23-24 |"); 
			writer.newLine();
			writer.write("|----------------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|------------|-----------|");
			writer.newLine();
			
			for(Line line : lines) {
				writer.write(line.getHourString());
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
				for(int i = 0; i<25; i++) {
					l.counts[i]+=line.counts[i];
				}

				exists = 1;
				break;
			}
		}
		if(exists == 0) {
			if(line.counts[0]>=1) {
				lines.add(line);
			}
		}

	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/full/hoursfull.txt"));
		    String line;
		    line = reader.readLine();
		    line = reader.readLine();
		    while ((line = reader.readLine()) != null) {
		        lines.add(new Line(line));
		    }
		    reader.close();

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

	public int hourof(Message message) {
		Date date = new Date((Long.valueOf(message.date)-3600)*1000);
		return Integer.parseInt(sdf.format(date));
	}
	
}


