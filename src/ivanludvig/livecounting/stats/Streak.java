package ivanludvig.livecounting.stats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Streak {
	
	String user;
	int ccount;
	String cdate;
	String bdate;
	String ldate;
	String bldate;
	int bcount;
	SimpleDateFormat sdf;
	
	public Streak(String user) {
		ccount = 0;
		bcount = 0;
		cdate = "0";
		bdate = "0";
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
		this.user = user;
	}
	
	public Streak(String user, int bcount, String bldate) {
		this.bcount=bcount;
		this.bldate=bldate;
		sdf =  new SimpleDateFormat("dd/MM/yyyy");
		this.user = user;
	}
	
	/*
	public void update(int count, String date) {
		if(Integer.parseInt(this.date[n].substring(date.length()-4, date.length()))==Integer.parseInt(date.substring(date.length()-4, date.length()))) {
			if(Integer.parseInt(this.date[n].substring(3, 4))==Integer.parseInt(date.substring(3, 4))) {
				if(Integer.parseInt(this.date[n].substring(0, 1))-Integer.parseInt(date.substring(0, 1))==1) {
					
				}else {
					this.date = date;
					n+=1;
				}
			}else if(Integer.parseInt(this.date[n].substring(3, 4))-Integer.parseInt(date.substring(3, 4))==1) {
				if(Integer.parseInt(date.substring(0, 1))==30 || Integer.parseInt(date.substring(0, 1))==31 ||
					(Integer.parseInt(this.date[n].substring(3, 4))==2 &&(Integer.parseInt(this.date[n].substring(0, 1))==28 ||
					Integer.parseInt(this.date[n].substring(0, 1))==29 ) )	) {
					if(Integer.parseInt(this.date[n].substring(0, 1))==1) {
						
					}else {
						n+=1;
					}
				}else {
					n+=1;
				}
			}
		}else if(Integer.parseInt(this.date[n].substring(date.length()-4, date.length()))-Integer.parseInt(date.substring(date.length()-4, date.length()))==1) {
			if((Integer.parseInt(this.date[n].substring(3, 4))==1 && Integer.parseInt(this.date[n].substring(0, 1))==1)){
				
			}else {
				n+=1;
			}
		}else {
			n+=1;
		}
	}
	*/
	
	public void update(int count, String date, int threshold) throws ParseException {
		if(cdate.equals("0")) {
			cdate=date;
	    	ccount=1;
			bdate=date;
	    	bcount=1;
			ldate = date;
			bldate = date;
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
					if(ccount>bcount) {
						bcount = ccount;
						bdate = cdate;
						bldate = ldate;
					}
					cdate=date;
					ccount = 1;
					ldate = date;
			    }
			}else {
				if(ccount>bcount) {
					bcount = ccount;
					bdate = cdate;
					bldate = ldate;
				}
				cdate=date;
				ccount = 1;
				ldate = date;
			}
		}
	}
	
	public Line toLine() {
		return new Line(user, bcount, bldate);
	}
	
	public Line getBest() {
		if(ccount>bcount) {
			bcount = ccount;
			bdate = cdate;
			bldate = ldate;
		}
		return new Line(user, bcount, bldate);
	}

}
