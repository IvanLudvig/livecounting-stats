package ivanludvig.livecounting.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class HoE extends Stat {
	
	int counts[];
	ArrayList<Line> lines3 = new ArrayList<Line>();
	ArrayList<Line> lines5 = new ArrayList<Line>();
	ArrayList<Line> lines10 = new ArrayList<Line>();
	int three[];
	int five[];
	int ten[];
	Main main;
	String date = "0";
	

	public HoE(Main main) {
		this.main = main;
		counts = new int[main.n];
		three = new int[main.n];
		five = new int[main.n];
		ten = new int[main.n];
		dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		//sdf =  new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public void update() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(dateof(message).equals(date)) {
					counts[main.users.indexOf(message.author)]+=1;
				}else {
					for(int i = 0; i<counts.length; i++) {
						if(counts[i]>=3000) {
							three[i]+=1;
							if(counts[i]>=5000) {
								five[i]+=1;
								if(counts[i]>=10000) {
									ten[i]+=1;
								}
							}
						}
					}
					counts = new int[main.n];
					date = dateof(message);
					counts[main.users.indexOf(message.author)]+=1;
				}
			}
		}
	}
	
	public void lastupdate() {
		int[] lastcounts = new int[main.n];
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(dateof(message).equals(date)) {
					lastcounts[main.users.indexOf(message.author)]+=1;
				}else {
					for(int i = 0; i<counts.length; i++) {
						if(lastcounts[i]<3000 && (lastcounts[i]+counts[i])>=3000) {
							three[i]+=1;
							if(lastcounts[i]<5000 && (lastcounts[i]+counts[i])>=5000) {
								five[i]+=1;
								if(lastcounts[i]<10000 && (lastcounts[i]+counts[i])>=10000) {
									ten[i]+=1;
								}
							}
						}
					}
					counts = new int[main.n];
					date = dateof(message);
					counts[main.users.indexOf(message.author)]+=1;
					break;
				}
			}
		}
	}
	
	public void write() {

		for(String str : main.users) {
			if(three[main.users.indexOf(str)]!=0) {
				lines3.add((new Line(str, three[main.users.indexOf(str)])));
				if(five[main.users.indexOf(str)]!=0) {
					lines5.add((new Line(str, five[main.users.indexOf(str)])));
					if(ten[main.users.indexOf(str)]!=0) {
						lines10.add((new Line(str, ten[main.users.indexOf(str)])));
					}
				}
			}
		}
		Collections.sort(lines3, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines3);
		Collections.sort(lines5, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines5);
		Collections.sort(lines10, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines10);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/ho3k.txt"));
	    	writer.write("| # |Username |Count");
			writer.newLine();
	    	writer.write("|---|------|---------------");
			writer.newLine();
			for(Line line : lines3) {
				writer.write(line.getTableString(lines3.indexOf(line)+1));
				writer.newLine();
			}
			writer.close();
	    	writer = new BufferedWriter(new FileWriter("output/hoe.txt"));
	    	writer.write("| # |Username |Count");
			writer.newLine();
	    	writer.write("|---|------|---------------");
			writer.newLine();
			for(Line line : lines5) {
				
				writer.write(line.getTableString(lines5.indexOf(line)+1));
				writer.newLine();
			}
			writer.close();
	    	writer = new BufferedWriter(new FileWriter("output/hos.txt"));
			for(Line line : lines10) {
				writer.write(line.getTableString(lines10.indexOf(line)+1));
				writer.newLine();
			}
			writer.close();
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	}
	
	public void addLine(Line line, int which) {
		if(which==3) {
			int exists3 = 0;
			for(Line l3 : lines3) {
				if(l3.user.equals(line.user)) {
					l3.count+=line.count;
					exists3 = 1;
					break;
				}
			}
			if(exists3 == 0) {
				lines3.add(line);
			}
		}
		if(which==5) {
			int exists5 = 0;
			for(Line l5 : lines5) {
				if(l5.user.equals(line.user)) {
					l5.count+=line.count;
					exists5 = 1;
					break;
				}
			}
			if(exists5 == 0) {
				lines5.add(line);
			}
		}
		if(which==10) {
			int exists10 = 0;
			for(Line l10 : lines10) {
				if(l10.user.equals(line.user)) {
					l10.count+=line.count;
					exists10 = 1;
					break;
				}
			}
			if(exists10 == 0) {
				lines10.add(line);
			}
		}

	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/ho3k.txt"));
		    String line;
		    while ((line = reader.readLine()) != null) {
		        lines3.add(new Line(line));
		    }
		    reader.close();
		    reader = new BufferedReader(new FileReader("output/hoe.txt"));
		    while ((line = reader.readLine()) != null) {
		        lines5.add(new Line(line));
		    }
		    reader.close();
		    reader = new BufferedReader(new FileReader("output/hos.txt"));
		    while ((line = reader.readLine()) != null) {
		        lines10.add(new Line(line));
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

	DateTimeFormatter dtf;
	public String dateof(Message message) {
		Date date = new Date((Long.valueOf(message.date))*1000);
		ZonedDateTime time = date.toInstant().atZone(ZoneId.of("America/New_York"));
		return dtf.format(time);
	}
	
}