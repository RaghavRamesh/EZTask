//@author A0104650U
// Erik Bodin 

public abstract class Task {

	private String desc;
	private boolean completed;

	public Task(String d) {
		desc = d;
		completed = false;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String d) {
		desc = d;
	}

	public void complete() {
		completed = true;
	}

	public void incomplete() {
		completed = false;
	}

	public boolean isCompleted() {
		return completed;
	}

	public abstract int getValue();

}
