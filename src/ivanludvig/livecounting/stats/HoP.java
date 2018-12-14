package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ivanludvig.livecounting.Main;

public class HoP extends Stat {

	Main main;
	ArrayList<Line> lines = new ArrayList<Line>();
	
	public HoP(Main main, int assists, int gets, int counts, int kparts, int days) {
		this.main = main;
		
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void write() {
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/count.txt"));
	    	writer.write("\r\n" + 
	    			"|# | Username | Score | Counts | Gets | Assists | Gets + Assists | Ks Participated |Days Participated |Gets Ratio |Assists Ratio |Combined Ratio");
			writer.newLine();
	    	writer.write("|:--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |---- ");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getTableString(lines.indexOf(line)));
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}

}
