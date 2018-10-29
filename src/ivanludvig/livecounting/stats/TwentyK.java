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

public class TwentyK {

	
	int counts[];
	String dates[];
	ArrayList<Line> lines = new ArrayList<Line>();
	SimpleDateFormat sdf;
	Main main;
	String date = "0";
	int n = 0;
	

	public TwentyK(Main main) {
		this.main = main;
		counts = new int[main.n];
		dates = new String[main.n];
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(dateof(message).equals(date)) {
					counts[n]+=1;
				}else {
					date = dateof(message);
					dates[n]=date;
					n+=1;
					counts[n]+=1;
				}
			}
		}
	}
	
	public void lastupdate() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(dateof(message).equals(date)) {
					counts[n]+=1;
				}else {
					date = dateof(message);
					break;
				}
			}
		}
	}
	
	public void write() {

		for(int i = 0; i<=n; i++) {
			System.out.print(dates[i]+ "  ");
			System.out.println(counts[i]);
			if(counts[i]>=20000) {
				addLine(new Line(dates[i], counts[i]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/20k.txt"));
	    	writer.write("|Date |Total counts ");
			writer.newLine();
	    	writer.write("|---|---");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getDayString());
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
		if(exists == 0) {
			lines.add(line);
		}
	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/20k.txt"));
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

	public String dateof(Message message) {
		Date date = new Date((Long.valueOf(message.date)-21600)*1000);
		return sdf.format(date);
	}
	
}
