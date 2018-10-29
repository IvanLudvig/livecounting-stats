package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class AverageCounts {
	int counts[];
	int first[];
	int onek[];
	int date;
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Line> lines1k = new ArrayList<Line>();
	Main main;

	public AverageCounts(Main main) {
		this.main = main;
		counts = new int[main.n];
		first = new int[main.n];
		onek = new int[main.n];
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok==0) {
				counts[main.users.indexOf(message.author)]+=1;
				date = Integer.parseInt(message.date);
				if(counts[main.users.indexOf(message.author)]==1) {
					first[main.users.indexOf(message.author)]=date;
				}else if (counts[main.users.indexOf(message.author)]==1000) {
					onek[main.users.indexOf(message.author)]=date;
				}
			}
		}
	}
	
	public void write() {
		for(String str : main.users) {
			if(first[main.users.indexOf(str)]!=0) {
				int delta=date-first[main.users.indexOf(str)];
				int days=delta/60/60/24;
				if(days==0) {
					days=1;
				}
				lines.add(new Line(str, 
						Math.round(((double)counts[main.users.indexOf(str)]/days)*100.0)/100.0));
			}
			if(onek[main.users.indexOf(str)]!=0) {
				int delta=date-onek[main.users.indexOf(str)];
				int days=delta/60/60/24;
				if(days==0) {
					days=1;
				}
				lines1k.add(new Line(str, 
						Math.round(((double)counts[main.users.indexOf(str)]/days)*100.0)/100.0));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
		Collections.sort(lines1k, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines1k);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/countsperday.txt"));
	    	writer.write("| # |Username |Counts per day ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<100) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
	    	writer = new BufferedWriter(new FileWriter("output/countsperdayfrom1k.txt"));
	    	writer.write("| # |Username |Counts per day ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines1k) {
				if(lines1k.indexOf(line)<100) {
					writer.write(line.getKPString(lines1k.indexOf(line)+1));
					writer.newLine();
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}

}

