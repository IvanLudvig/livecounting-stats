package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class KParts {
	
	int counts[][];
	//int all[];
	//int part[];
	//double percentage[];
	int top = 0;
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public KParts(Main main) {
		this.main = main;
		counts = new int[2800][20000];
		//all = new int[2800];
		//part = new int[2800];
		//percentage = new double[2800];
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(top==0) {
					top=(message.count/1000)+1;
				}
				if((message.count/1000)+1<=top) {
					counts[main.users.indexOf(message.author)][(message.count/1000)+1]+=1;
				}
			}
		}
	}
	
	public void write() {
		for(String str : main.users) {
			for(int i = 1; i<=top; i++) {
				//System.out.println(i);
				if(counts[main.users.indexOf(str)][i]>0) {
					int all = 0;
					int part = 0;
					for(int j = i; j<=top; j++) {
						all+=1;
						if(counts[main.users.indexOf(str)][j]>0) {
							part+=1;
						}
					}
					//percentage[main.users.indexOf(str)]=Math.round((double)part/all*100);
					lines.add(new Line(str, Math.round(((double)part/all*100)*100.0)/100.0));
					break;
					//lines.add(new Line(str, counts[main.users.indexOf(str)]));
				}
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/kparts.txt"));
	    	writer.write("| # |Username |Percentage ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(line.percentage>2) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}
}
