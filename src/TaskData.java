//@author A0104650U
// Erik Bodin 

public class TaskData {
	String desc;
	int id;
	boolean taskIsTimed;
	String keywords;

	TaskData(boolean timedTask, int idnr, String description) {
		desc = description;
		id = idnr;
		taskIsTimed = timedTask;

	}
}