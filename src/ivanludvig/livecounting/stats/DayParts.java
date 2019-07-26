package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class DayParts extends Stat {
	
	Main main;
	byte current[];

	public DayParts(Main main) {
		this.main = main;
		counts = new int[main.n];
		current = new byte[main.n];
		dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
	
	@Override
	public void update() {
		String currentdate="";
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(!dateof(message).equals(currentdate)) {
					for(String user : main.users) {
						if(current[main.users.indexOf(user)]>0) {
							counts[main.users.indexOf(user)]+=1;
						}
					}
					current = new byte[main.n];
					currentdate=dateof(message);
				}
				current[main.users.indexOf(message.author)]=1;
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
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/days.txt"));
	    	writer.write("| # |Username |Days ");
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
	
	DateTimeFormatter dtf;
	public String dateof(Message message) {
		Date date = new Date((Long.valueOf(message.date))*1000);
		ZonedDateTime time = date.toInstant().atZone(ZoneId.of("America/New_York"));
		return dtf.format(time);
	}

}
