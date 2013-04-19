//@author A0104650U
//Erik Bodin 

public class CalendarTask extends Task implements Comparable<CalendarTask> {

	private int startTime;
	private int endTime;
	private int date;

	public CalendarTask(String d, int sTime, int eTime, int da) {
		super(d); // pass desc to super constructor
		startTime = sTime;
		endTime = eTime;
		date = da;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setStartTime(int t) {
		startTime = t;
	}

	public void setEndTime(int t) {
		endTime = t;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int d) {
		date = d;
	}

	@Override
	public int compareTo(CalendarTask otherTask) {
		if (date > otherTask.date) {
			return 1;
		}
		if (date < otherTask.date) {
			return -1;
		}
		if (startTime > otherTask.startTime) {
			return 1;
		}
		if (startTime < otherTask.startTime) {
			return -1;
		}
		return 0;
	}

	@Override
	public int getValue() {
		return date;
	}

}
