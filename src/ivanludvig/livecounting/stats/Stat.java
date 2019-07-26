package ivanludvig.livecounting.stats;

import java.util.ArrayList;

public abstract class Stat {

	public ArrayList<Line> lines = new ArrayList<Line>();
	public int counts[];
	public abstract void update();
	public abstract void write();
}
