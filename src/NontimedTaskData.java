//@author A0104650U
// Erik Bodin 

public class NontimedTaskData extends TaskData {
	int prio;

	NontimedTaskData(boolean isTimed, int idnr, String desc, int taskprio) {
		super(isTimed, idnr, desc);
		prio = taskprio;
	}

}
