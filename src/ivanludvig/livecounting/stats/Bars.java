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

public class Bars extends Stat{
	
	int counts[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;
	String date = "0";
	String lastauthor = "";
	int lasthour = 1000;
	

	public Bars(Main main) {
		this.main = main;
		counts = new int[main.n];
		try {
			lasthour = readLastHour();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if((Integer.parseInt(main.getESThour(message))!=lasthour)&&(!lastauthor.equals(""))) {
					counts[main.users.indexOf(lastauthor)]+=1;
					lasthour = Integer.parseInt(main.getESThour(message));
				}
				lastauthor=message.author;
			}
		}
	}
	
	
	public void write() {
		for(int i = 0; i<main.users.size(); i++) {
			addLine(new Line(main.users.get(i), counts[i]));
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
			writeLastHour();
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/bars.txt"));
	    	writer.write("| # |Username |Bars");
			writer.newLine();
	    	writer.write("|---|------|---------------");
			writer.newLine();
			for(Line line : lines) {
				if(line.count>=5) {
					writer.write(line.getTableString(lines.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
			writer = new BufferedWriter(new FileWriter("output/full/barsfull.txt"));
			for(Line line : lines) {
				if(line.count>0) {
					writer.write(line.getTableString(lines.indexOf(line)+1));
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
		    reader = new BufferedReader(new FileReader("output/full/barsfull.txt"));
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
	
	public int readLastHour() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("res/lasthour.txt"));
		int hour = reader.read();
		reader.close();
	    return hour;
	}
	
	public void writeLastHour() throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter("res/lasthour.txt"));
		writer.write(Integer.toString(lasthour));
		writer.close();
	}
	
	/*
	public int hourof(Message message) {
		Date date = new Date((Long.valueOf(message.date)-21600)*1000);
		return Integer.parseInt(sdf.format(date));
	}
	*/

}
