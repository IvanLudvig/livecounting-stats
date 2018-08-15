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

public class DayStreak {
	int counts[];
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Streak> streaks = new ArrayList<Streak>();
	SimpleDateFormat sdf;
	Main main;
	String date = "0";
	int n = 0;
	

	public DayStreak(Main main) {
		this.main = main;
		counts = new int[1800];
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
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
				if(dateof(message).equals(date)) {
					counts[main.users.indexOf(message.author)]+=1;
				}else {
					//System.out.println(date+" e "+dateof(message)+" diff");
					for(Streak streak : streaks) {
						try {
							streak.update(counts[main.users.indexOf(streak.user)], date, 1);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					date = dateof(message);
					counts = new int[1800];
					counts[main.users.indexOf(message.author)]+=1;
				}
			}
		}
	}
	
	public void lastupdate() {
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(dateof(message).equals(date)) {
					counts[n]+=1;
				}else {
					for(Streak streak : streaks) {
						try {
							streak.update(counts[main.users.indexOf(message.author)], date, 1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					date = dateof(message);
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
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/daystreak.txt"));
	    	writer.write("|# |User |Streak |Last Day  ");
			writer.newLine();
	    	writer.write("|---|---|---|---|");
			writer.newLine();
			for(Line line : lines) {
				writer.write(line.getStreakString(lines.indexOf(line)+1));
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
			if(l.user!=null && line.user!=null) {
				if(l.user.equals(line.user)) {
					System.out.println(l.user+" "+line.user);
					l.count+=line.count;
					System.out.println(line.user);
					exists = 1;
					break;
				}
			}
		}
		if(exists == 0) {
			if(line.count>=10) {
				lines.add(line);
			}
		}
	}
	
	public void read() {
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader("output/daystreak.txt"));
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

	public String dateof(Message message) {
		Date date = new Date((Long.valueOf(message.date)-21600)*1000);
		return sdf.format(date);
	}
	
}
