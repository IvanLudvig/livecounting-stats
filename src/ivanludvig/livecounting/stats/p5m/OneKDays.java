package ivanludvig.livecounting.stats.p5m;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
import ivanludvig.livecounting.stats.Line;
import ivanludvig.livecounting.stats.Streak;
import ivanludvig.livecounting.stats.StreakAdv;

public class OneKDays {
	int counts[];
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<StreakAdv> streaks = new ArrayList<StreakAdv>();
	ArrayList<String> notP5M = new ArrayList<String>();
	SimpleDateFormat sdf;
	Main main;
	String date = "0";
	int n = 0;
	int stop = 0;
	

	public OneKDays(Main main) {
		this.main = main;
		counts = new int[1800];
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
		readNotP5M();
	}
	
	public void update() {
		if(stop==0) {
		for(int i=0; i<main.users.size(); i++) {
			if(streaks.size()<=i) {
				streaks.add(new StreakAdv(main.users.get(i)));
			}else if(streaks.get(i)==null) {
				streaks.add(new StreakAdv(main.users.get(i)));
			}
		}
		for(Message message : main.messages) {
			if(message.ok == 0) {
				if(message.count<=5000000 && main.messages.indexOf(message)!=0 
						&& main.messages.indexOf(message)!=main.messages.size()-1) {
					if(main.messages.get(main.messages.indexOf(message)-1).count<=5000000 
							&& main.messages.get(main.messages.indexOf(message)-1).ok==0
							&& main.messages.get(main.messages.indexOf(message)+1).count<=5000000
							&& main.messages.get(main.messages.indexOf(message)+1).ok==0) {
						write();
						System.out.println(message.count);
						System.out.println("writing");
						stop=1;
						break;
					}
				}
				if(dateof(message).equals(date)) {
					counts[main.users.indexOf(message.author)]+=1;
				}else {
					//System.out.println(date+" e "+dateof(message)+" diff");
					for(StreakAdv streak : streaks) {
						try {
							streak.update(counts[main.users.indexOf(streak.user)], date, 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					date = dateof(message);
					counts = new int[main.n];
					counts[main.users.indexOf(message.author)]+=1;
				}
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
					for(StreakAdv streak : streaks) {
						try {
							streak.update(counts[main.users.indexOf(message.author)], date, 1000);
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
		ArrayList<Streak> all = new ArrayList<Streak>();
		lines = new ArrayList<Line>();
		for(StreakAdv streak : streaks) {
			int ok = 0;
			for(String user : notP5M) {
				if(streak.user.equals(user)) {
					ok=1;
					break;
				}
			}
			if(ok==0) {
				all.addAll(streak.getData());
			}
		}
		for(Streak streak : all) {
			lines.add(streak.toLine());
		}
		Collections.sort(lines, Comparator.comparingInt(Line -> Line.count));
		Collections.reverse(lines);
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("output/p5m/1kdays.txt"));
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
					System.out.println(l.user+" "+line.user);
					l.count+=line.count;
					System.out.println(line.user);
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
		    reader = new BufferedReader(new FileReader("output/p5m/1kdays.txt"));
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
	
	public void readNotP5M(){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("output/p5m/notp5mlist.txt"));
			String line;
		    while ((line = reader.readLine()) != null) {
		        notP5M.add(line);
		    }
		    reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

