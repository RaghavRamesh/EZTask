//@author A0104650U
// Erik Bodin 

public class PriorityTask extends Task implements Comparable<PriorityTask> {

	private int prio;

	public PriorityTask(String d, int p) {
		super(d); // pass desc to super constructor
		prio = p;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(int p) {
		prio = p;
	}

	@Override
	public int compareTo(PriorityTask otherTask) {
		if (prio > otherTask.prio) {
			return -1;
		}
		if (prio < otherTask.prio) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getValue() {
		return prio;
	}

}
