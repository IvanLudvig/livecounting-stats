package ivanludvig.livecounting.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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

public class FirstCounts extends Stat {

	int counts[];
	String messages[];
	String date[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;
	SimpleDateFormat sdf;
	String dpusers = "";

	public FirstCounts(Main main) {
		this.main = main;
		counts = new int[main.n];
		messages = new String[main.n];
		date = new String[main.n];
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
		dpusers = readDpUsers();
	}
	
	@Override
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(message.body!=null) {
					counts[main.users.indexOf(message.author)]=message.count;
					messages[main.users.indexOf(message.author)]=message.body;
					date[main.users.indexOf(message.author)]=dateof(message);
				}
			}
		}
	}
	
	
	@Override
	public void write() {
		for(int i = 0; i<main.users.size(); i++) {
			if((messages[i]!=null) && (date[i]!=null)) {
				addLine(new Line(main.users.get(i), messages[i].replaceAll("\\\\n", "  "), date[i], counts[i]));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/firstcounts_full.txt"));
	    	writer.write("|User |First Count | Date");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getFirstString());
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    } 
	    
	    
	    
	    lines = new ArrayList<Line>();
		for(int i = 0; i<main.users.size(); i++) {
			if((messages[i]!=null) && (date[i]!=null)) {
				if((counts[i]<9800000)||(counts[i]>10300000)) {
					addLine(new Line(main.users.get(i), messages[i].replaceAll("\\\\n", "  "), date[i], counts[i]));
				}else {
					if(dpusers.contains(main.users.get(i))){
						addLine(new Line(main.users.get(i), messages[i].replaceAll("\\\\n", "  "), date[i], counts[i]));
					}
				}
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/firstcounts.txt"));
	    	writer.write("|User |First Count | Date");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getFirstString());
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
		    reader = new BufferedReader(new FileReader("output/firstcounts.txt"));
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
	
	public String readDpUsers() {
		String str = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("res/lastChatFile.txt"));
			str = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public String dateof(Message message) {
		Date date = new Date((Long.valueOf(message.date)-3600)*1000);
		return sdf.format(date);
	}

}
