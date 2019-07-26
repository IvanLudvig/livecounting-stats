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

	public Gets(Main main) {
		this.main = main;
		counts = new int[main.n];
	}
	
	@Override
	public void update() {
		for(Message message : main.messages) {
			String str = Integer.toString(message.count);
<<<<<<< HEAD
			if((message.ok == 0) && (str.length()>2)) { 
				if( str.substring(str.length()-3, str.length()).equals("000")) {
					counts[main.users.indexOf(message.author)]+=1;
					
				}
			}
			
=======
			if(message.ok == 0 && str.length()>2) { 
				if( str.substring(str.length()-3, str.length()).equals("000")) {
					counts[main.users.indexOf(message.author)]+=1;
				}
			}
>>>>>>> 2aa91cd0b8c54b85852367d0626fbfbe8e1bb94d
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
