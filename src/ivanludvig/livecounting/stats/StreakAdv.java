package ivanludvig.livecounting.stats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StreakAdv{
	
	public String user;
	ArrayList<Streak> streaks = new ArrayList<Streak>();
	SimpleDateFormat sdf;
	int ccount;
	String cdate="0";
	String ldate;

	public StreakAdv(String user) {
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
		this.user = user;
	}
	
	
	public void update(int count, String date, int threshold) throws ParseException {
		if(cdate.equals("0")) {
			cdate=date;
			ldate = date;
		}else {
			if(count >= threshold ) {
				//System.out.println("date here "+date);
				Calendar c = Calendar.getInstance();
			    c.setTime(sdf.parse(cdate));
			    c.add(Calendar.DATE, -1);
			    if ((sdf.format(c.getTime()).toString()).equals(date)) {
			    	ccount+=1;
			    	cdate = date;
			    } else {
			    	if(ccount>=3) {
			    		streaks.add(new Streak(user, ccount, cdate));
			    	}
					cdate=date;
					ccount = 1;
					ldate = date;
			    }
			}else {
		    	if(ccount>=3) {
		    		streaks.add(new Streak(user, ccount, cdate));
		    	}
				cdate=date;
				ccount = 1;
				ldate = date;
			}
		}
	}
	
	public ArrayList<Streak> getData() {
		if(streaks.size()>0) {
			if(streaks.get(streaks.size()-1).bcount!=ccount && !streaks.get(streaks.size()-1).bdate.equals(ldate)) {
				if(ccount>=3) {
	    			streaks.add(new Streak(user, ccount, ldate));
	    		}
			}
		}
		return streaks;
	}

}
