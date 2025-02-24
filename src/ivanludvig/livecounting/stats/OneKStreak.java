package ivanludvig.livecounting.stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ivanludvig.livecounting.Main;
import ivanludvig.livecounting.Message;

public class OneKStreak extends Stat {
	int counts[];
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Streak> streaks = new ArrayList<Streak>();
	Main main;
	String date = "0";
	int n = 0;
	

	public OneKStreak(Main main) {
		this.main = main;
		counts = new int[main.n];
	}
	
	public void update() {
		for(int i=0; i<main.users.size(); i++) {
			if(streaks.size()<=i) {
				streaks.add(new Streak(main.users.get(i)));
			}else if(streaks.get(i)==null) {
				streaks.add(new Streak(main.users.get(i)));
			}
		}
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(main.getESTDate(message).equals(date)) {
					counts[main.users.indexOf(message.author)]+=1;
				}else {
					//System.out.println(date+" e "+dateof(message)+" diff");
					for(Streak streak : streaks) {
						try {
							streak.update(counts[main.users.indexOf(streak.user)], date, 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					date = main.getESTDate(message);
					counts = new int[main.n];
					counts[main.users.indexOf(message.author)]+=1;
				}
			}
		}
	}
	
	public void lastupdate() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(main.getESTDate(message).equals(date)) {
					counts[n]+=1;
				}else {
					for(Streak streak : streaks) {
						try {
							streak.update(counts[main.users.indexOf(message.author)], date, 1000);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					date = main.getESTDate(message);
					break;
				}
			}
		}
	}
	
	public void write() {
		for(Streak streak : streaks) {
			addLine(streak.getBest());
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/1kstreak.txt"));
	    	writer.write("|# |User |Streak |Last Day  ");
			writer.newLine();
	    	writer.write("|---|---|---|---|");
			writer.newLine();
			for(Line line : lines) {
				if(lines.indexOf(line)<20) {
					writer.write(line.getStreakString(lines.indexOf(line)+1));
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
			if(l.user!=null && line.user!=null) {
				if(l.user.equals(line.user)) {
					//System.out.println(l.user+" "+line.user);
					l.count+=line.count;
					//System.out.println(line.user);
					exists = 1;
					break;
				}
			}
		}
		if(exists == 0) {
			if(line.count>=1) {
				lines.add(line);
			}
		}
	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/1kstreak.txt"));
		    String line;
		    line = reader.readLine();
		    line = reader.readLine();
		    while ((line = reader.readLine()) != null) {
		        lines.add(new Line(line));
		    }
		    reader.close();

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
