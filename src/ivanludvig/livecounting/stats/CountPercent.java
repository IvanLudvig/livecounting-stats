package ivanludvig.livecounting.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class CountPercent {
	
	int counts[];
	int words[];
	int total[];
	int chars[];
	int countchars[];
	ArrayList<Line> lines = new ArrayList<Line>();
	Main main;

	public CountPercent(Main main) {
		this.main = main;
		counts = new int[main.n];
		words = new int[main.n];
		total = new int[main.n];
		chars = new int[main.n];
		countchars = new int[main.n];
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.author!=null && message.body!=null) {
				words[main.users.indexOf(message.author)]+=wordCount(message);
				total[main.users.indexOf(message.author)]+=1;
				chars[main.users.indexOf(message.author)]+=message.body.length();
				if(message.count>=0) {
					counts[main.users.indexOf(message.author)]+=1;
					chars[main.users.indexOf(message.author)]-=message.countstr.length();
					countchars[main.users.indexOf(message.author)]+=message.countstr.length();
				}
			}

		}
	}
	
	public void write() {
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>=45000) {
				lines.add(new Line(str,
						Math.round(((double)counts[main.users.indexOf(str)]/(words[main.users.indexOf(str)])*100)*100.0)/100.0));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		//Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/countpercent/wordspercent.txt"));
	    	writer.write("| # |Username |Percentage ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<100) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}else {
					break;
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    } 
	    write2();
	}
	
	public void write2() {
		lines = new ArrayList<Line>();
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>=45000) {
				lines.add(new Line(str,
						Math.round(((double)words[main.users.indexOf(str)]/(total[main.users.indexOf(str)]))*100.0)/100.0));
				//System.out.println(Math.round(((double)words[main.users.indexOf(str)]/(total[main.users.indexOf(str)]))*100.0)/100.0);
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/countpercent/wordspermessage.txt"));
	    	writer.write("| # |Username |Words per message ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<100) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}else {
					break;
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    } 
	    write3();
	}
	
	public void write3() {
		lines = new ArrayList<Line>();
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>=45000) {
				lines.add(new Line(str,
						Math.round(((double)countchars[main.users.indexOf(str)]/(chars[main.users.indexOf(str)])*100)*100.0)/100.0));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		//Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/countpercent/charspercent.txt"));
	    	writer.write("| # |Username |Percentage ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<100) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}else {
					break;
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    } 
	    write4();
	}
	
	public void write4() {
		lines = new ArrayList<Line>();
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>=45000) {
				lines.add(new Line(str,
						Math.round(((double)chars[main.users.indexOf(str)]/(total[main.users.indexOf(str)]))*100.0)/100.0));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/countpercent/charspermessage.txt"));
	    	writer.write("| # |Username |Chars per message ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<100) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
					writer.newLine();
				}else {
					break;
				}
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    } 
	    write5();
	}
	
	public void write5() {
		lines = new ArrayList<Line>();
		for(String str : main.users) {
			if(counts[main.users.indexOf(str)]>45000) {
				lines.add(new Line(str,
						Math.round(((double)words[main.users.indexOf(str)]/(total[main.users.indexOf(str)]))*100.0)/100.0));
			}
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		//Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/countpercent/least-wordspermessage.txt"));
	    	writer.write("| # |Username |Words per message ");
			writer.newLine();
	    	writer.write("|---|---|---");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<100) {
					writer.write(line.getKPString(lines.indexOf(line)+1));
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
	
	public void addLine(Line line) {
		int exists = 0;
		for(Line l : lines) {
			if(l.user.equals(line.user)) {
				l.count+=line.count;
				exists = 1;
				break;
			}
		}
		if(exists == 0 && line.count>0) {
			lines.add(line);
		}
	}
	
	
	public int wordCount(Message message) {
	    int words;
	    String str = message.body;
	    str = str.replace(message.countstr, "");
	    str = str.replace("\n", "");
	    String [] sentence = str.split("\\w\\s+");
	    words = sentence.length;
	    if(words>=0) {
	    	return words;
	    }else {
	    	return 0;
	    }
	}

}
