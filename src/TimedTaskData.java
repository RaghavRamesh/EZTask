//@author A0104650U
// Erik Bodin 

public class TimedTaskData extends TaskData {
	int date;
	int startTime;
	int endTime;

	TimedTaskData(boolean isTimed, int idnr, String desc, int taskDate,
			int taskStartTime, int taskEndTime) {
		super(isTimed, idnr, desc);
		date = taskDate;
		startTime = taskStartTime;
		endTime = taskEndTime;
	}

}