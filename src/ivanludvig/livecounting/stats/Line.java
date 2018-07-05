package ivanludvig.livecounting.stats;

public class Line {
	
	public String user;
	public String user2;
	public int count;
	int counts[] = new int[25];
	
	public Line(String user, int count) {
		this.user = user;
		this.count = count;
	}
	
	public Line(String user1, String user2,  int count) {
		this.user = user1;
		this.user2 = user2;
		this.count = count;
	}
	
	public Line(String user1, int count[]) {
		this.user = user1;
		this.counts = count;
	}
	
	public Line(String line) {
		int[] u = new int[27];
		int y=0;
		int slash=0;
		for(int j = 0; j<line.length(); j++) {
			if(line.charAt(j)==('|')) {
				u[y]=j;
				y++;
			}
			if(line.charAt(j)==('/')) {
				slash++;
			}
		}
		if(y==4) {
			if(slash==2) {
				this.user=line.substring(u[1]+5, u[2]-1);
				this.count=Integer.parseInt(line.substring(u[2]+2, u[3]-1));
			}else if(slash==4) {
				this.user=line.substring(u[0]+5, u[1]-1);
				this.user2=line.substring(u[1]+5, u[2]-1);
				this.count=Integer.parseInt(line.substring(u[2]+2, u[3]-1));
			}
		}else if(y==27) {
			this.user=line.substring(u[0]+1, u[1]-1).trim();
			for(int i = 1; i<25; i++) {
				this.counts[i]=Integer.parseInt(line.substring(u[i]+2, u[i+1]-1).trim());
			}
		}
	}
	

	public String getTableString(int order) {
		return "| "+order+" | "+"/u/"+user+" | "+count+" | ";
	}
	
	public String getHourString() {
		String string = "|"+user;
		for(int h = 0; h<(22-user.length()); h++) {
			string+=" ";
		}
		string+="|";
		for(int i = 0; i<25; i++) {
			string+=" "+counts[i];
			for(int j = 0; j<(10-Integer.toString(counts[i]).length()); j++) {
				string+=" ";
			}
			string+=" |";
		}
		return string;
	}
	
	public String getStringFav() {
		return "| "+"/u/"+user+" | "+"/u/"+user2+" | " + count+" | ";
	}
	
	
}
