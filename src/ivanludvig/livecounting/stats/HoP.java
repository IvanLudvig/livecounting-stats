package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;

public class HoP extends Stat {

	Main main;
	ArrayList<Line> lines = new ArrayList<Line>();
	int assists, gets, counts, kparts, days;
	
	public HoP(Main main, int assists, int gets, int counts, int kparts, int days) {
		this.main = main;
		this.assists = assists;
		this.gets = gets;
		this.counts = counts;
		this.kparts = kparts;
		this.days = days;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void write() {
		for(Line line : main.stats.get(counts).lines) {
			int user = main.users.indexOf(line.user);
			lines.add(new Line(line.user, line.count, main.stats.get(assists).counts[user],
					main.stats.get(gets).counts[user],main.stats.get(kparts).counts[user], 
					main.stats.get(days).counts[user]));
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.score));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/hop.txt"));
	    	writer.write(
	    	"|# | Username | Score | Counts | Gets | Assists | Gets + Assists | Ks Participated |Days Participated |Gets Ratio |Assists Ratio |Combined Ratio");
			writer.newLine();
	    	writer.write("|:--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |---- ");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<200) {
					writer.write(line.getHopString(lines.indexOf(line)+1));
					writer.newLine();
				}else {
					break;
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}

}
