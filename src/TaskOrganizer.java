//@author A0104650U
// Erik Bodin 

import java.util.ArrayList;
import java.util.Collections;

public class TaskOrganizer {

	private final String MESSAGE_IDINRANGE_EXCEPTION = "Id must be in range";
	private final String MESSAGE_INVALIDDATE_EXCEPTION = "Invalid date";
	private final String MESSAGE_INVALIDSTARTTIME_EXCEPTION = "Invalid start time";
	private final String MESSAGE_INVALIDENDTIME_EXCEPTION = "Invalid end time";
	private final String MESSAGE_INVALIDPRIO_EXCEPTION = "Prio must be high, medium or low. Alt 3, 2 or 1.";
	private final String MESSAGE_PRIO_IDINRANGE_EXCEPTION = "Id out of range in ToDo-list";
	private final String MESSAGE_CAL_IDINRANGE_EXCEPTION = "Id out of range in calendar";
	private final String MESSAGE_INVALIDNROFTASKS_EXCEPTION = "Invalid number of tasks";
	private final String FILENAME_CALENDARLIST = "calendarList.txt";
	private final String FILENAME_PRIORITYLIST = "priorityList.txt";

	private final int[] DAYS_IN_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };

	private ArrayList<PriorityTask> priorityList;
	private ArrayList<CalendarTask> calendarList;
	private Storer storage;

	static private TaskOrganizer instance = null;

	private TaskOrganizer() {
		priorityList = new ArrayList<PriorityTask>();
		calendarList = new ArrayList<CalendarTask>();
		storage = new Storer();
		loadPrioTasks();
		loadCalTasks();
	}

	public static TaskOrganizer getInstance() {
		if (instance == null) {
			instance = new TaskOrganizer();
		}
		return instance;
	}

	public String getTaskDesc(boolean isTimed, int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		String desc;
		if (isTimed) {
			if (index < 0 || index >= calendarList.size()) {
				throw new Exception(MESSAGE_CAL_IDINRANGE_EXCEPTION);
			}
			desc = calendarList.get(index).getDesc();
		} else {
			if (index < 0 || index >= priorityList.size()) {
				throw new Exception(MESSAGE_PRIO_IDINRANGE_EXCEPTION);
			}
			desc = priorityList.get(index).getDesc();
		}
		return desc;
	}

	public String completeTimedTask(int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (index < 0 || index >= calendarList.size()) {
			throw new Exception(MESSAGE_IDINRANGE_EXCEPTION);
		}

		// undostring
		String undoString = "INCOMPLETE c " + (int) (index + 1);

		calendarList.get(index).complete();
		storeCalTasks();

		return undoString;
	}

	public String completeNontimedTask(int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (index < 0 || index >= priorityList.size()) {
			throw new Exception(MESSAGE_IDINRANGE_EXCEPTION);
		}

		// undostring
		String undoString = "INCOMPLETE p " + (int) (index + 1);

		priorityList.get(index).complete();
		storePrioTasks();
		return undoString;
	}

	public String incompleteTimedTask(int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (index < 0 || index >= calendarList.size()) {
			throw new Exception(MESSAGE_IDINRANGE_EXCEPTION);
		}

		// undostring
		String undoString = "COMPLETE c " + (int) (index + 1);

		calendarList.get(index).incomplete();
		storeCalTasks();
		return undoString;
	}

	public String incompleteNontimedTask(int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (index < 0 || index >= priorityList.size()) {
			throw new Exception(MESSAGE_IDINRANGE_EXCEPTION);
		}

		// undostring
		String undoString = "COMPLETE p " + (int) (index + 1);

		priorityList.get(index).incomplete();
		storePrioTasks();
		return undoString;
	}

	public void clearCompletedTasks() {
		for (int i = 0; i < priorityList.size(); i++) {
			Task task = priorityList.get(i);
			if (task.isCompleted() == true) {
				priorityList.remove(task);
				i = -1; // start from beginning
			}
		}
		for (int i = 0; i < calendarList.size(); i++) {
			Task task = calendarList.get(i);
			if (task.isCompleted() == true) {
				calendarList.remove(task);
				i = -1; // start from beginning
			}
		}
		storeCalTasks();
		storePrioTasks();
		Collections.sort(priorityList);
		Collections.sort(calendarList);
	}

	public String addNontimedTask(String desc, int prio) throws Exception {
		if (prio > 3 || prio < 1) {
			throw new Exception(MESSAGE_INVALIDPRIO_EXCEPTION);
		}
		PriorityTask task = new PriorityTask(desc, prio);
		priorityList.add(task);
		Collections.sort(priorityList);

		// undostring
		int index = priorityList.indexOf(task) + 1;
		String undoString = "DELETE p " + index;

		storePrioTasks();

		return undoString;
	}

	public String addTimedTask(String desc, int date, int startTime, int endTime)
			throws Exception {
		if (!isValidDate(date)) {
			throw new Exception(MESSAGE_INVALIDDATE_EXCEPTION);
		}
		if (!isValidTime(startTime)) {
			throw new Exception(MESSAGE_INVALIDSTARTTIME_EXCEPTION);
		}
		if (!isValidTime(endTime)) {
			throw new Exception(MESSAGE_INVALIDENDTIME_EXCEPTION);
		}
		CalendarTask task = new CalendarTask(desc, startTime, endTime, date);
		calendarList.add(task);
		Collections.sort(calendarList);

		// undostring
		int index = calendarList.indexOf(task) + 1;
		String undoString = "DELETE c " + index;
		storeCalTasks();
		return undoString;
	}

	public String removeTimedTask(int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (index < 0 || index >= calendarList.size()) {
			throw new Exception(MESSAGE_CAL_IDINRANGE_EXCEPTION);
		}

		// undostring
		CalendarTask task = calendarList.get(index);
		String undoString = "ADD " + task.getDesc() + ";" + task.getDate()
				+ ";" + task.getStartTime();

		calendarList.remove(index);
		storeCalTasks();
		return undoString;
	}

	public String removeNontimedTask(int id) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (index < 0 || index >= priorityList.size()) {
			throw new Exception(MESSAGE_PRIO_IDINRANGE_EXCEPTION);
		}

		// undostring
		PriorityTask task = priorityList.get(index);
		String undoString = "ADD " + task.getDesc() + ";" + task.getPrio();

		priorityList.remove(index);
		storePrioTasks();
		return undoString;
	}

	public String editNontimed(int id, String desc, int prio) throws Exception {
		int index = id - 1; // adjust id to corresponding index
		if (prio > 3 || prio < 1) {
			throw new Exception(MESSAGE_INVALIDPRIO_EXCEPTION);
		}
		if (index < 0 || index >= priorityList.size()) {
			throw new Exception(MESSAGE_PRIO_IDINRANGE_EXCEPTION);
		}
		PriorityTask task = priorityList.get(index);

		String oldDesc = task.getDesc();
		int oldPrio = task.getPrio();

		task.setDesc(desc);
		task.setPrio(prio);
		Collections.sort(priorityList);

		// undostring
		int i = priorityList.indexOf(task) + 1;
		String undoString = "EDIT " + i + ";" + oldDesc + ";" + oldPrio;

		storePrioTasks();

		return undoString;
	}

	public String editTimed(int id, String desc, int date, int startTime,
			int endTime) throws Exception {
		int index = id - 1; // adjust id to corresponding index

		if (index < 0 || index >= calendarList.size()) {
			throw new Exception(MESSAGE_CAL_IDINRANGE_EXCEPTION);
		}
		if (!isValidDate(date)) {
			throw new Exception(MESSAGE_INVALIDDATE_EXCEPTION);
		}
		if (!isValidTime(startTime)) {
			throw new Exception(MESSAGE_INVALIDSTARTTIME_EXCEPTION);
		}
		if (!isValidTime(endTime)) {
			throw new Exception(MESSAGE_INVALIDENDTIME_EXCEPTION);
		}

		CalendarTask task = calendarList.get(index);

		String oldDesc = task.getDesc();
		int oldDate = task.getDate();
		int oldStartTime = task.getStartTime();
		int oldEndTime = task.getEndTime();

		task.setDesc(desc);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setDate(date);
		Collections.sort(calendarList);

		// undostring
		int i = calendarList.indexOf(task) + 1;
		String undoString = "EDIT " + i + ";" + oldDesc + ";" + oldDate + ";"
				+ oldStartTime + ";" + oldEndTime;

		storeCalTasks();

		return undoString;
	}

	public String[][] getNontimedTasksOnKeywords(String keywords) {
		String[] words = keywords.split("\\+");
		ArrayList foundIndexesOfTasks = new ArrayList();

		for (int i = 0; i < priorityList.size(); i++) {
			PriorityTask task = priorityList.get(i);
			boolean foundTask = false;
			for (String word : words) {
				word = word.trim();
				String[] subwords = word.split("\\*");
				String desc = task.getDesc();
				String prio = String.valueOf(task.getPrio());
				String prioAlt = desc;
				if (prio.equals("3")) {
					prioAlt = "high";
				} else if (prio.equals("2")) {
					prioAlt = "medium";
				} else if (prio.equals("1")) {
					prioAlt = "low";
				}
				String id = String.valueOf((int) (i + 1));
				if (isIn(desc, desc, prioAlt, id, prio, subwords)) {
					if (!foundTask) { // if haven't found the task before add it
										// to found tasks
						foundIndexesOfTasks.add(i);
					}
					foundTask = true;
				}
			}
		}

		String[][] tasks = new String[foundIndexesOfTasks.size()][4];

		for (int i = 0; i < foundIndexesOfTasks.size(); i++) {
			int taskIndex = (Integer) foundIndexesOfTasks.get(i);
			PriorityTask task = priorityList.get(taskIndex);
			taskIndex += 1; // adjust index back
			tasks[i][0] = Integer.toString(taskIndex);
			tasks[i][1] = task.getDesc();
			tasks[i][2] = Integer.toString(task.getPrio());
			tasks[i][3] = Boolean.toString(task.isCompleted());
		}
		return tasks;
	}

	public String[][] getTimedTasksOnKeyWords(String keywords) {
		String[] words = keywords.split("\\+");
		ArrayList foundIndexesOfTasks = new ArrayList();

		for (int i = 0; i < calendarList.size(); i++) {
			CalendarTask task = calendarList.get(i);
			boolean foundTask = false;
			for (String word : words) {
				word = word.trim();
				String[] subwords = word.split("\\*");
				String desc = task.getDesc();
				String startTime = String.valueOf(task.getStartTime());
				String endTime = String.valueOf(task.getEndTime());
				String date = String.valueOf(task.getDate());
				String id = String.valueOf((int) (i + 1));
				if (isIn(desc, id, startTime, endTime, date, subwords)) {
					if (!foundTask) {
						foundIndexesOfTasks.add(i);
					}
					foundTask = true;
				}
			}
		}

		String[][] tasks = new String[foundIndexesOfTasks.size()][6];

		for (int i = 0; i < foundIndexesOfTasks.size(); i++) {
			int taskIndex = (Integer) foundIndexesOfTasks.get(i);
			CalendarTask task = calendarList.get(taskIndex);
			taskIndex += 1; // adjust index back

			tasks[i][0] = Integer.toString(taskIndex);
			tasks[i][1] = task.getDesc();
			tasks[i][2] = String.format(String.format("%%0%dd", 6),
					task.getDate());
			tasks[i][3] = String.format(String.format("%%0%dd", 4),
					task.getStartTime());
			tasks[i][4] = String.format(String.format("%%0%dd", 4),
					task.getEndTime());
			tasks[i][5] = Boolean.toString(task.isCompleted());
		}
		return tasks;
	}

	private static boolean isIn(String a, String b, String c, String d,
			String e, String[] words) {
		a = a.toUpperCase();
		b = b.toUpperCase();
		c = c.toUpperCase();
		d = d.toUpperCase();
		for (String word : words) {
			word = word.trim();
			word = word.toUpperCase();
			if (!a.contains(word) && !b.contains(word) && !c.contains(word)
					&& !d.contains(word) && !e.contains(word)) {
				return false;
			}
		}
		return true;
	}

	public String[][] getNontimedTasks(int startPriority, int nrOfTasks)
			throws Exception {
		if (startPriority > 3 || startPriority < 1) {
			throw new Exception(MESSAGE_INVALIDPRIO_EXCEPTION);
		}
		if (nrOfTasks < -1) {
			throw new Exception(MESSAGE_INVALIDNROFTASKS_EXCEPTION);
		}
		return getTasks(false, startPriority, nrOfTasks, priorityList);
	}

	public String[][] getTimedTasks(int startDate, int nrOfTasks)
			throws Exception {
		if (!isValidDate(startDate)) {
			throw new Exception(MESSAGE_INVALIDDATE_EXCEPTION);
		}
		if (nrOfTasks < -1) {
			throw new Exception(MESSAGE_INVALIDNROFTASKS_EXCEPTION);
		}
		return getTasks(true, startDate, nrOfTasks, calendarList);
	}

	private String[][] getTasks(boolean timed, int startValue, int nrOfTasks,
			ArrayList list) {

		int nrOfArgs;
		if (timed) {
			nrOfArgs = 6;
		} else {
			nrOfArgs = 4;
		}

		int startIndex = 0;
		boolean condition;
		for (int i = 0; i < list.size(); i++) {
			if (timed) {
				condition = (((CalendarTask) list.get(i)).getValue() >= startValue);
			} else {
				condition = (((PriorityTask) list.get(i)).getValue() <= startValue);
			}
			if (condition) {
				startIndex = i;
				break;
			}
		}

		// if nrOfTasks is specified as -1, get all
		if (nrOfTasks == -1) {
			nrOfTasks = list.size() - startIndex;
		}

		String[][] tasks = new String[nrOfTasks][nrOfArgs];
		for (int i = 0; i < nrOfTasks; i++) {
			int index = startIndex + i;
			if ((startIndex + i) < list.size()) { // do not go out of range in
													// priorityList
				int taskIndex = index + 1; // adjust index back
				tasks[i][0] = Integer.toString(taskIndex); // convert index(id)
															// of task to String
				tasks[i][1] = ((Task) list.get(index)).getDesc(); // already a
																	// String
				if (timed) {
					int date = ((CalendarTask) list.get(index)).getDate();
					int startTime = ((CalendarTask) list.get(index))
							.getStartTime();
					int endTime = ((CalendarTask) list.get(index)).getEndTime();
					tasks[i][2] = String.format(String.format("%%0%dd", 6),
							date);
					tasks[i][3] = String.format(String.format("%%0%dd", 4),
							startTime);
					tasks[i][4] = String.format(String.format("%%0%dd", 4),
							endTime);
					Task task = (Task) list.get(index);
					tasks[i][5] = String.valueOf(task.isCompleted());
				} else {
					tasks[i][2] = Integer.toString(((PriorityTask) list
							.get(index)).getPrio()); // convert prio/date of
														// task to String
					Task task = (Task) list.get(index);
					tasks[i][3] = String.valueOf(task.isCompleted());
				}
			}
		}
		return tasks;
	}

	public boolean isValidTime(int t) {
		if (t < 0 || t > 2359) {
			return false;
		}
		Integer tInt = t;
		String time = tInt.toString();
		int length = time.length();
		String h = "-1";
		String m = "-1";
		if (length == 3) {
			h = time.substring(0, 1);
			m = time.substring(1, 3);
		} else if (length == 4) {
			h = time.substring(0, 2);
			m = time.substring(2, 4);
		}
		int hours = Integer.valueOf(h);
		int minutes = Integer.valueOf(m);
		if (hours > 23) {
			return false;
		}
		if (minutes > 59) {
			return false;
		}
		return true;
	}

	public boolean isValidDate(int d) {
		if (d < 00000101 || d > 99991231) {
			return false;
		}
		Integer dInt = d;
		String date = dInt.toString();
		int length = date.length();

		if (length != 8) {
			return false;
		}

		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		int years = Integer.valueOf(year);
		int months = Integer.valueOf(month);
		int days = Integer.valueOf(day);
		if (years > 2100) {
			return false;
		}
		if (months > 12) {
			return false;
		}
		if (isLeapYear(years)) {
			DAYS_IN_MONTH[1] = 29;
		}
		if (days > DAYS_IN_MONTH[months - 1]) {
			DAYS_IN_MONTH[1] = 28;// reset
			return false;
		}
		DAYS_IN_MONTH[1] = 28;// reset
		return true;
	}

	private boolean isLeapYear(int year) {
		if (year % 400 == 0) {
			return true;
			// assert false;
		} else if (year % 100 == 0) {
			return false;

		} else if (year % 4 == 0) {
			return true;
		}
		return false;
	}

	private void loadPrioTasks() {
		String[][] tasks = storage.readFile(FILENAME_PRIORITYLIST);
		if (tasks != null) {
			for (String[] task : tasks) {
				String desc = task[1];
				int prio = Integer.parseInt(task[2]);
				PriorityTask atask = new PriorityTask(desc, prio);
				priorityList.add(atask);
				if (Boolean.valueOf(task[0])) {
					atask.complete();
				}
			}
			Collections.sort(priorityList);
		}
	}

	private void storePrioTasks() {
		String[][] tasks = new String[priorityList.size()][3];
		int cnt = 0;
		for (PriorityTask task : priorityList) {
			tasks[cnt][0] = String.valueOf(task.isCompleted());
			tasks[cnt][1] = String.valueOf(task.getDesc());
			tasks[cnt][2] = String.valueOf(task.getPrio());
			cnt++;
		}
		storage.writeFile(tasks, FILENAME_PRIORITYLIST);
	}

	private void loadCalTasks() {
		String[][] tasks = storage.readFile(FILENAME_CALENDARLIST);
		if (tasks != null) {
			for (String[] task : tasks) {
				String desc = task[1];
				int date = Integer.parseInt(task[2]);
				int startTime = Integer.parseInt(task[3]);
				int endTime = Integer.parseInt(task[4]);
				CalendarTask atask = new CalendarTask(desc, startTime, endTime,
						date);
				calendarList.add(atask);
				if (Boolean.valueOf(task[0])) {
					atask.complete();
				}
			}
			Collections.sort(calendarList);
		}
	}

	private void storeCalTasks() {
		String[][] tasks = new String[calendarList.size()][5];
		int cnt = 0;
		for (CalendarTask task : calendarList) {
			tasks[cnt][0] = String.valueOf(task.isCompleted());
			tasks[cnt][1] = String.valueOf(task.getDesc());
			tasks[cnt][2] = String.valueOf(task.getDate());
			tasks[cnt][3] = String.valueOf(task.getStartTime());
			tasks[cnt][4] = String.valueOf(task.getEndTime());
			cnt++;
		}
		storage.writeFile(tasks, FILENAME_CALENDARLIST);
	}

}
