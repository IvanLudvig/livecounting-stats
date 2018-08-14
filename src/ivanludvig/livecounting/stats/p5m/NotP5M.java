package ivanludvig.livecounting.stats.p5m;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;
import ivanludvig.livecounting.stats.Line;

public class NotP5M {
	int counts[];
	ArrayList<String> lines = new ArrayList<String>();
	Main main;

	public NotP5M(Main main) {
		this.main = main;
		counts = new int[2800];
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(message.count<=5000000) {
					counts[main.users.indexOf(message.author)]+=1;
				}
			}
		}
	}
	
	public void write() {
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>=3000) {
				lines.add(str);
			}
		}
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/p5m/notp5mlist.txt"));
			for(String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}
}
