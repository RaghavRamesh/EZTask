//@author A0104650U
// Erik Bodin

public class Logic {

	private static final String FILENAME_LOG = "log.txt";
	private static final String SHUTDOWN_QUIT = "exit";
	private static final String SHUTDOWN_EXIT = "quit";

	private static TaskOrganizer taskOrganizer;
	private static UI ui;
	private static Parser parser;
	private static Logger logger;
	private static int startDate;
	private static int startPrio;
	private static String undoString;

	private static String executeInput(String input) throws Exception {
		InputData inputData = parser.parseUserInput(input);
		Command command = inputData.command;
		boolean isTimed;
		String[][] tasks;
		int date, startTime, endTime, prio, id;
		String desc;
		String executionInfo = null;
		switch (command) {
		case ADD: // add
			isTimed = inputData.data.taskIsTimed;
			desc = inputData.data.desc;
			if (isTimed) {
				TimedTaskData timedTaskData = (TimedTaskData) inputData.data;

				date = timedTaskData.date;
				startTime = timedTaskData.startTime;
				endTime = timedTaskData.endTime;
				undoString = taskOrganizer.addTimedTask(desc, date, startTime,
						endTime);

				tasks = taskOrganizer.getTimedTasks(startDate, -1);
				System.out.println("LENGTH: " + tasks.length);
				for (String[] s : tasks) {
					System.out.println(s[0] + " " + s[1] + " " + s[2] + " "
							+ s[3] + " " + s[4] + " " + s[5]);
				}
				ui.displayCalList(tasks);
			} else {
				NontimedTaskData nontimedTaskData = (NontimedTaskData) inputData.data;

				prio = nontimedTaskData.prio;

				undoString = taskOrganizer.addNontimedTask(desc, prio);

				tasks = taskOrganizer.getNontimedTasks(startPrio, -1);

				ui.displayPrioList(tasks);
			}
			executionInfo = desc + " successfully added.";
			break;

		case EDIT: // edit
			isTimed = inputData.data.taskIsTimed;
			desc = inputData.data.desc;
			id = inputData.data.id;

			if (isTimed) {
				TimedTaskData timedTaskData = (TimedTaskData) inputData.data;

				date = timedTaskData.date;
				startTime = timedTaskData.startTime;
				endTime = timedTaskData.endTime;

				undoString = taskOrganizer.editTimed(id, desc, date, startTime,
						endTime);
				tasks = taskOrganizer.getTimedTasks(startDate, -1);

				// ///REMOVE THIS
				for (String[] a : tasks) {
					System.out.println(a[0] + " " + a[1] + " " + a[2] + " "
							+ a[3] + " " + a[4] + " " + a[5]);
				}

				ui.displayCalList(tasks);
			} else {
				NontimedTaskData nontimedTaskData = (NontimedTaskData) inputData.data;

				prio = nontimedTaskData.prio;
				undoString = taskOrganizer.editNontimed(id, desc, prio);

				tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
				ui.displayPrioList(tasks);
			}
			executionInfo = "Edit successful.";
			break;

		case DELETE: // delete
			isTimed = inputData.data.taskIsTimed;
			id = inputData.data.id;
			if (isTimed) {
				desc = taskOrganizer.getTaskDesc(true, id);
				undoString = taskOrganizer.removeTimedTask(id);
				tasks = taskOrganizer.getTimedTasks(startDate, -1);
				ui.displayCalList(tasks);
			} else {
				desc = taskOrganizer.getTaskDesc(false, id);
				undoString = taskOrganizer.removeNontimedTask(id);
				tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
				ui.displayPrioList(tasks);
			}
			executionInfo = desc + " successfully removed.";
			break;

		case UNDO: // undo
			executionInfo = executeInput(undoString);
			break;

		case GOTO: // goto
			isTimed = inputData.data.taskIsTimed;

			if (isTimed) {
				TimedTaskData timedTaskData = (TimedTaskData) inputData.data;

				startDate = timedTaskData.date;
				ui.setViewDate(String.valueOf(startDate));
				tasks = taskOrganizer.getTimedTasks(startDate, -1);

				ui.displayCalList(tasks);
			} else {
				NontimedTaskData nontimedTaskData = (NontimedTaskData) inputData.data;

				startPrio = nontimedTaskData.prio;
				ui.setViewPrio(String.valueOf(startPrio));
				tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
				ui.displayPrioList(tasks);
			}
			break;

		case SEARCH: // search
			String keywords = inputData.data.keywords;

			tasks = taskOrganizer.getNontimedTasksOnKeywords(keywords);
			ui.displayPrioList(tasks);

			tasks = taskOrganizer.getTimedTasksOnKeyWords(keywords);
			ui.displayCalList(tasks);
			break;

		case COMPLETE: // complete task
			isTimed = inputData.data.taskIsTimed;
			id = inputData.data.id;
			if (isTimed) {
				undoString = taskOrganizer.completeTimedTask(id);
				tasks = taskOrganizer.getTimedTasks(startDate, -1);
				ui.displayCalList(tasks);
				desc = taskOrganizer.getTaskDesc(isTimed, id);
			} else {
				undoString = taskOrganizer.completeNontimedTask(id);
				tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
				ui.displayPrioList(tasks);
				desc = taskOrganizer.getTaskDesc(isTimed, id);
			}
			executionInfo = desc + " completed.";
			break;
		case INCOMPLETE: // complete task
			isTimed = inputData.data.taskIsTimed;
			id = inputData.data.id;
			if (isTimed) {
				undoString = taskOrganizer.incompleteTimedTask(id);
				tasks = taskOrganizer.getTimedTasks(startDate, -1);
				ui.displayCalList(tasks);
				desc = taskOrganizer.getTaskDesc(isTimed, id);

			} else {
				undoString = taskOrganizer.incompleteNontimedTask(id);
				tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
				ui.displayPrioList(tasks);
				desc = taskOrganizer.getTaskDesc(isTimed, id);
			}
			executionInfo = desc + " incompleted.";
			break;

		case CLEAR: // clear all completed tasks
			taskOrganizer.clearCompletedTasks();
			tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
			ui.displayPrioList(tasks);
			tasks = taskOrganizer.getTimedTasks(startDate, -1);
			ui.displayCalList(tasks);
			executionInfo = "All completed tasks succesfully cleared.";
			break;
		default:
			break;
		}
		return executionInfo;
	}

	public static void main(String[] args) throws Exception {
		parser = Parser.getInstance();
		taskOrganizer = TaskOrganizer.getInstance();
		logger = new Logger(FILENAME_LOG);
		ui = UI.getInstance();

		startDate = Parser.getCurrentDate(); // default value
		startPrio = 3; // default value

		String[][] tasks;
		tasks = taskOrganizer.getTimedTasks(startDate, -1);
		ui.displayCalList(tasks);
		tasks = taskOrganizer.getNontimedTasks(startPrio, -1);
		ui.displayPrioList(tasks);

		boolean oldInput = false;
		while (true) {
			String input = ui.getUserInput();
			if (!input.equals("") && !oldInput) {
				logger.logString(input);
				try {
					String executionInfo = executeInput(input);
					if (executionInfo != null) {
						ui.displayLog(executionInfo);
						logger.logString(executionInfo);
					}
					input = ""; // reset input to prevent extra iterations
				} catch (Exception e) {
					// display error
					ui.displayLog("- " + e.getMessage());
					logger.logString(e.getMessage());
				}
				oldInput = true;
			} else {
				oldInput = false;
			}
			if (input.equalsIgnoreCase(SHUTDOWN_EXIT)
					|| input.equalsIgnoreCase(SHUTDOWN_QUIT)) {
				System.exit(0); // shutdown
			}
		}

	}
}
