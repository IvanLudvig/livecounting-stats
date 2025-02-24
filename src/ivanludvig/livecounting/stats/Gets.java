package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class Gets extends Stat {
	
	Main main;
	ArrayList<Message> dm = new ArrayList<Message>();
	//int count = 0;
	int current = 0;

	public Gets(Main main) {
		this.main = main;
		counts = new int[main.n];
	}
	
	@Override
	public void update() {
		for(Message message : main.messages) {
			//if(message.ok==0) {
				//if(message.count<10) {
					//System.out.println("THIS "+message.count+" "+message.date);
				//}
			//}
			String str = Integer.toString(message.count);
			/*
			if(message.ok==0) {
				if(message.count>10) {
					if(Math.abs(message.count - current)>2) {
						System.out.println((current-message.count)+": "+current+" "+message.count+" |"+message.date);
						
					}
					current = message.count;
				}
			}
			*/
			if((message.ok == 0) && (str.length()>2)) { 
				if( str.substring(str.length()-3, str.length()).equals("000")) {
					counts[main.users.indexOf(message.author)]+=1;
					
					/* CODE TO FIND DOUBLES
					for(Message m : dm) {
						if((message.body.equals(m.body))&&(message.date.equals(m.date))) {
							System.out.println("DOUBLE "+message.count);
							break;
						}
					}
					dm.add(message);
					count +=1;
					if(count == 300) {
						for(int i = 0; i<200; i++) {
							dm.remove(0);
						}
						count = 100;
					}
					*/
				}

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
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/gets.txt"));
	    	writer.write("| # |Username |Gets");
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
