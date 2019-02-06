package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class Speed extends Stat {
	
	int top = 0;
	Main main;

	public Speed(Main main) {
		this.main = main;
		counts = new int[2000];
	}
	
	Date lastdate = new Date(1414613584);
	int last = 1;
	int up =0;
	@Override
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if((message.count>=last*100000)&&(up>=99000)) {
					counts[last]=(int)days(lastdate, date(message));
					lastdate = date(message);
					last++;
					System.out.println(message.count +" "+last+" "+date(message)+" "+lastdate+" "+counts[last-1]);
					up=0;
				}
			}
			up++; 
		}
	}
	
	@Override
	public void write() {
		for(int count : counts) {
			lines.add(new Line(count));
		}
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/kparts.txt"));
	    	writer.write("| # |Username |Percentage ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getStringSpeed(lines.indexOf(line)));
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}
	
	SimpleDateFormat sdf;
	public String dateof(Message message) {
		Date date = new Date((Long.valueOf(message.date)-3600)*1000);
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}
	
	public Date date(Message message) {
		Date date = new Date((Long.valueOf(message.date)-3600)*1000);
		return date;
	}
	
	public long days(Date d1, Date d2) {
		long diff = d2.getTime()- d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
}

