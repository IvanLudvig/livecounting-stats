package ivanludvig.livecounting.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ivanludvig.livecounting.Main;

public class FavouriteCounter extends Stat{
	int counts[][];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public FavouriteCounter(Main main) {
		this.main = main;
		counts = new int[main.n][main.n];
	}

	public void update() {
		for(int i = 0; i<main.messages.size()-1; i++) {
			String str = Integer.toString(main.messages.get(i).count);
			if(main.messages.get(i).ok == 0 && main.messages.get(i+1).ok==0) { 
				counts[main.users.indexOf(main.messages.get(i).author)][main.users.indexOf(main.messages.get(i+1).author)]+=1;
			}
		}
	}
	
	public void write() {
		for(int i = 0; i<main.users.size(); i++) {
			int num = 0;
			int k = 0;
			for(int j = 0; j<main.users.size(); j++) {
				if(counts[i][j]>num) {
					k = j;
					num = counts[i][j];
				}
			}
			addLine(new Line(main.users.get(i),main.users.get(k), num));
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/favourite.txt"));
	    	writer.write("|Counter |Favourite Counter |Counts ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getStringFav());
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
			if(l.user.equals(line.user) && l.user2.equals(line.user2)) {
				l.count+=line.count;
				exists = 1;
				break;
			}
		}
		if(exists == 0 && line.count>5) {
			lines.add(line);
		}
	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/favourite.txt"));
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
}
