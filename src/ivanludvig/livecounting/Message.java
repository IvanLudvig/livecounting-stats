package ivanludvig.livecounting;

import com.google.gson.JsonElement;

public class Message {
	
	public String body;
	public String author;
	public String date;
	public String stricken = "";
	public String countstr = "";
	public int count = -1;
	public int ok = 0;
	
	public Message(Main main, JsonElement data) {
		body = data.getAsJsonObject().getAsJsonPrimitive("body").toString();
		body = body.substring(1, body.length()-1);
		if(data.isJsonObject()) {
			if(data.getAsJsonObject().get("author").isJsonPrimitive()) {
				author = data.getAsJsonObject().getAsJsonPrimitive("author").toString();
				author = author.substring(1, author.length()-1);
				if(!main.users.contains(author)) {
					main.users.add(author);
				} 
			}else {
				ok = 1;
			}
		}else {
			ok = 1;
		}
		stricken = data.getAsJsonObject().getAsJsonPrimitive("stricken").toString();
		if(stricken.equals("true")) {
			ok = 1;
		}
		date = data.getAsJsonObject().getAsJsonPrimitive("created_utc").toString();
		if(body==null || author==null || date==null) {
			ok=1;
		}
		if(ok == 0) {
			if(date.contains(".")) {
				date = date.substring(0, date.length()-2);
			}
			if(!main.users.contains(author)) {
				main.users.add(author);
			}
			count();
		}
	}
	
	public void count() {
		String numstr = "";
		for(int i = 0; i<body.length(); i++) {
			if(Character.isDigit(body.charAt(i))) {
				numstr+=body.charAt(i);
				countstr+=body.charAt(i);
			}else if(body.charAt(i)==','||body.charAt(i)=='.'||body.charAt(i)==' ') {
				countstr+=body.charAt(i);
				continue;
			}else {
				break;
			}
		}
		if(!numstr.equals("") && numstr.length()<9) {
			count = Integer.parseInt(numstr);
		}else {
			count = 0;
			ok = 1;
		}
	}

}