//@author A0104650U
// Erik Bodin 

import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {
	private final String COMMAND_ADD = "ADD";
	private final String COMMAND_DELETE = "DELETE";
	private final String COMMAND_GOTO = "GOTO";
	private final String COMMAND_EDIT = "EDIT";
	private final String COMMAND_UNDO = "UNDO";
	private final String COMMAND_SEARCH = "SEARCH";
	private final String COMMAND_COMPLETE = "COMPLETE";
	private final String COMMAND_CLEAR = "CLEAR";
	private final String COMMAND_INCOMPLETE = "INCOMPLETE";
	private final String MESSAGE_INVALIDCOMMAND_EXCEPTION = "Invalid command";
	private final String MESSAGE_INVALIDPRIO_EXCEPTION = "Prio must be high, medium or low. Alt 3, 2 or 1.";
	private final String MESSAGE_UNKNOWNTYPE_EXCEPTION = "Unknown type";
	private final String MESSAGE_INVALIDINPUT_EXCEPTION = "Invalid input";
	private final String MESSAGE_IDNOTINTEGER_EXCEPTION = "Id must be a integer";
	private final String MESSAGE_INVALIDDATE_EXCEPTION = "Invalid date";
	private final String MESSAGE_INVALIDTIME_EXCEPTION = "Invalid time";
	private final String TYPE_TIMED = "C";
	private final String TYPE_NONTIMED = "P";
	private final String PRIORITY_HIGH = "HIGH";
	private final String PRIORITY_MEDIUM = "MEDIUM";
	private final String PRIORITY_LOW = "LOW";

	private static final String[] MONTHS = { "JANUARY", "FEBRUARI", "MARS",
			"APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER",
			"NOVEMBER", "DECEMBER" };
	private static final String[] KEYWORDS_DATE = { "TODAY", "", " ", "NOW",
			"THIS", "CURRENT" };
	private static final String[] KEYWORDS_TIME = { "", " ", "NOW", "THIS",
			"CURRENT" };

	private static Parser instance = null;

	private Parser() {
		// protect init
	}

	public static Parser getInstance() {
		if (instance == null) {
			instance = new Parser();
		}
		return instance;
	}

	public InputData parseUserInput(String userInput) throws Exception {
		int sepMark = userInput.indexOf(" ");
		String commandString;
		if (sepMark != -1) {
			commandString = userInput.substring(0, sepMark);
		} else {
			commandString = userInput;
		}
		if (COMMAND_ADD.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_ADD);
			return dataForAdd(userInput);
		} else if (COMMAND_EDIT.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_EDIT);
			return dataForEdit(userInput);
		} else if (COMMAND_DELETE.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_DELETE);
			return dataForRemove(userInput);
		} else if (COMMAND_GOTO.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_GOTO);
			return dataForGoto(userInput);
		} else if (COMMAND_UNDO.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_UNDO);
			return dataForUndo();
		} else if (COMMAND_SEARCH.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_SEARCH);
			return dataForSearch(userInput);
		} else if (COMMAND_COMPLETE.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_COMPLETE);
			return dataForComplete(userInput);
		} else if (COMMAND_INCOMPLETE.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString,
					COMMAND_INCOMPLETE);
			return dataForIncomplete(userInput);
		} else if (COMMAND_CLEAR.contains(commandString.toUpperCase())) {
			userInput = userInput.replaceFirst(commandString, COMMAND_CLEAR);
			return dataForClear();
		}
		throw new Exception(MESSAGE_INVALIDCOMMAND_EXCEPTION);
	}

	private InputData dataForAdd(String userInput) throws Exception {
		String[] parts;
		String[] inputs;
		String commandString;
		String descString;

		try {
			parts = userInput.split(";"); // seperate on ;
			inputs = parts[0].split(" ", 2); // seperate command from desc
			commandString = inputs[0];
			descString = inputs[1];
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}
		int id = -1; // no id specified or needed
		boolean isTimed;
		InputData inputData;
		TaskData taskData = null;

		String desc = parseDesc(descString);
		Command command = parseCommand(commandString);

		if (parts.length == 3) { // if timed task
			String dateString = parts[1];
			String[] timeParts = parts[2].split("-");
			String startTimeString = timeParts[0];

			int date = parseDate(dateString);
			int startTime = parseTime(startTimeString);
			int endTime = startTime; // default value if not specified. makes it
										// a deadline task

			if (timeParts.length > 1) { // if a end time specified
				String endTimeString = timeParts[1];
				endTime = parseTime(endTimeString);
				if (endTime == startTime) {
					endTime = (startTime + 100) % 2400;
				}
			}

			isTimed = true;

			taskData = new TimedTaskData(isTimed, id, desc, date, startTime,
					endTime);
		}

		else if (parts.length == 2) { // if nontimed task
			String prioString = parts[1];

			int prio = parsePrio(prioString);
			isTimed = false;

			taskData = new NontimedTaskData(isTimed, id, desc, prio);
		} else {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}
		inputData = new InputData(command, taskData);
		return inputData; // return exceptions if somethings wrong
	}

	private InputData dataForEdit(String userInput) throws Exception {
		String[] parts;
		String[] inputs;
		String commandString;
		String idString;
		String descString;
		try {
			parts = userInput.split(";");
			inputs = parts[0].split(" ", 2);
			commandString = inputs[0];
			idString = inputs[1];
			descString = parts[1];
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}

		boolean isTimed;
		InputData inputData;
		TaskData taskData = null;

		Command command = parseCommand(commandString);
		String desc = parseDesc(descString);
		int id = parseId(idString);

		if (parts.length == 4) { // if timed task
			String dateString = parts[2];

			String[] timeParts = parts[3].split("-");
			String startTimeString = timeParts[0];

			int startTime = parseTime(startTimeString);
			int endTime = startTime; // default value if not specified. makes it
										// a deadline task

			if (timeParts.length > 1) { // if a end time specified
				String endTimeString = timeParts[1];
				endTime = parseTime(endTimeString);
				if (endTime == startTime) {
					endTime = (startTime + 100) % 2400;
				}
			}

			int date = parseDate(dateString);
			isTimed = true;

			taskData = new TimedTaskData(isTimed, id, desc, date, startTime,
					endTime);
		}

		else if (parts.length == 3) { // if nontimed task
			String prioString = parts[2];

			int prio = parsePrio(prioString);
			isTimed = false;

			taskData = new NontimedTaskData(isTimed, id, desc, prio);
		} else {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}

		inputData = new InputData(command, taskData);
		return inputData; // return exce if somethings wrong
	}

	private InputData dataForRemove(String userInput) throws Exception {
		String idString;
		String commandString;
		String type;
		try {
			String[] parts = userInput.split(";");
			String[] inputs = parts[0].split(" ", 3);
			commandString = inputs[0];
			type = inputs[1];
			idString = inputs[2];
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}
		String desc = null; // is not specified or needed
		Command command = parseCommand(commandString);
		int id = parseId(idString);
		boolean isTimed = parseIsTimed(type);
		InputData inputData;
		TaskData taskData = new TaskData(isTimed, id, desc);
		inputData = new InputData(command, taskData);
		return inputData;
	}

	private InputData dataForGoto(String userInput) throws Exception {
		String[] parts;
		String[] inputs;
		String commandString;
		String type;
		String prioString;
		String dateString;

		try {
			parts = userInput.split(";");
			inputs = parts[0].split(" ", 3);
			commandString = inputs[0];
			type = inputs[1];
			prioString = inputs[2];
			dateString = inputs[2];
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}

		int idnr = -1; // not specified and not needed
		String desc = null; // not specified and not needed

		InputData inputData;
		TaskData taskData = null;

		Command command = parseCommand(commandString);
		boolean isTimed = parseIsTimed(type);

		if (isTimed) {
			int startTime = -1; // not specified and not needed
			int endTime = -1; // not specified and not needed
			int date = parseDate(dateString);

			taskData = new TimedTaskData(isTimed, idnr, desc, date, startTime,
					endTime);
		} else {
			int prio = parsePrio(prioString);

			taskData = new NontimedTaskData(isTimed, idnr, desc, prio);
		}

		inputData = new InputData(command, taskData);
		return inputData;
	}

	private InputData dataForComplete(String userInput) throws Exception {
		String[] parts;
		String[] inputs;
		String commandString;
		String type;
		String idString;
		try {
			parts = userInput.split(";");
			inputs = parts[0].split(" ", 3);
			commandString = inputs[0];
			type = inputs[1];
			idString = inputs[2];
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}

		String desc = null; // is not specified or needed

		Command command = parseCommand(commandString);
		int id = parseId(idString);
		boolean isTimed = parseIsTimed(type);

		InputData inputData;
		TaskData taskData = new TaskData(isTimed, id, desc);
		inputData = new InputData(command, taskData);
		return inputData;
	}

	private InputData dataForIncomplete(String userInput) throws Exception {
		String[] parts;
		String[] inputs;
		String commandString;
		String type;
		String idString;
		try {
			parts = userInput.split(";");
			inputs = parts[0].split(" ", 3);
			commandString = inputs[0];
			type = inputs[1];
			idString = inputs[2];
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}
		String desc = null; // is not specified or needed

		Command command = parseCommand(commandString);
		int id = parseId(idString);
		boolean isTimed = parseIsTimed(type);

		InputData inputData;
		TaskData taskData = new TaskData(isTimed, id, desc);
		inputData = new InputData(command, taskData);
		return inputData;
	}

	private InputData dataForClear() throws Exception {
		Command command = parseCommand(COMMAND_CLEAR);
		TaskData taskData = new TaskData(false, -1, null); // no data is needed
		InputData inputData = new InputData(command, taskData);
		return inputData;
	}

	private InputData dataForUndo() throws Exception {
		Command command = parseCommand(COMMAND_UNDO);
		TaskData taskData = new TaskData(false, -1, null); // no data is needed
		InputData inputData = new InputData(command, taskData);
		return inputData;
	}

	private InputData dataForSearch(String userInput) throws Exception {
		String[] parts;
		int separationIndex;
		String commandString;
		String keywords;
		try {
			parts = userInput.split(" ");
			separationIndex = userInput.indexOf(" ");
			commandString = userInput.substring(0, separationIndex);
			keywords = userInput.substring(separationIndex, userInput.length());
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDINPUT_EXCEPTION);
		}

		Command command = parseCommand(commandString);

		TaskData taskData = new TaskData(false, -1, null);
		taskData.keywords = keywords;
		InputData inputData = new InputData(command, taskData);
		return inputData;
	}

	private int parseDate(String dateString) throws Exception {
		int date;
		dateString = dateString.trim();

		String[] dateStringParts = dateString.split(" ");
		int month = getCurrentMonth(); // initial value
		int day = getCurrentDay(); // initial value
		int year = getCurrentYear(); // initial value
		boolean foundAValue = false;
		for (int mon = 1; mon <= MONTHS.length; mon++) {
			for (String dateStringPart : dateStringParts) {
				dateStringPart = dateStringPart.toUpperCase();
				if (MONTHS[mon - 1].contains(dateStringPart)) {
					month = mon;
					foundAValue = true;
				}
				if (isInt(dateStringPart)) {
					int number = Integer.parseInt(dateStringPart);
					if (number >= 1 && number <= 31) {
						day = number;
						foundAValue = true;
					}
					if (number > 31 & number <= 9999) {
						year = number;
						foundAValue = true;
					}
				}
			}
		}
		if (foundAValue) {
			String yearString = String.format(String.format("%%0%dd", 4), year);
			String monthString = String.format(String.format("%%0%dd", 2),
					month);
			String dayString = String.format(String.format("%%0%dd", 2), day);

			dateString = yearString + monthString + dayString;
		}

		if (isInArray(dateString, KEYWORDS_DATE)) {
			date = getCurrentDate();
		} else {
			try {
				date = Integer.parseInt(dateString);
			} catch (Exception e) {
				throw new Exception(MESSAGE_INVALIDDATE_EXCEPTION);
			}
		}
		return date;
	}

	private int parseTime(String timeString) throws Exception {
		int time = 0;
		timeString = timeString.trim();
		timeString = timeString.toUpperCase();
		if (isInArray(timeString, KEYWORDS_TIME)) {
			time = getCurrentTime();
		} else if (timeString.contains("AM") || timeString.contains("PM")) {
			try {
				String[] parts = timeString.split(" ", 2);
				int hour = Integer.parseInt(parts[0]);
				int extraHours = 0;
				if (parts[1].contains("PM")) {
					extraHours = 12;
				}
				hour += extraHours;
				time = hour * 100;
			} catch (Exception e) {
				// ignore this
			}
		} else {
			try {
				time = Integer.parseInt(timeString);
			} catch (Exception e) {
				throw new Exception(MESSAGE_INVALIDTIME_EXCEPTION);
			}
		}
		return time;
	}

	private int parseId(String idString) throws Exception {
		idString = idString.trim();
		int id;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception e) {
			throw new Exception(MESSAGE_IDNOTINTEGER_EXCEPTION);
		}
		return id;
	}

	private Command parseCommand(String commandString) throws Exception {
		Command command = null;
		commandString = commandString.trim();
		try {
			command = Command.valueOf(commandString.toUpperCase());
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDCOMMAND_EXCEPTION);
		}
		return command;
	}

	private int parsePrio(String prioString) throws Exception {
		prioString = prioString.trim();
		prioString = prioString.toUpperCase();
		if (PRIORITY_HIGH.contains(prioString)) {
			prioString = "3";
		} else if (PRIORITY_MEDIUM.contains(prioString)) {
			prioString = "2";
		} else if (PRIORITY_LOW.contains(prioString)) {
			prioString = "1";
		}

		int prio;
		try {
			prio = Integer.parseInt(prioString);
		} catch (Exception e) {
			throw new Exception(MESSAGE_INVALIDPRIO_EXCEPTION);
		}
		return prio;
	}

	private String parseDesc(String descString) {
		String desc = descString.trim();
		return desc;
	}

	private boolean parseIsTimed(String type) throws Exception {
		type = type.trim();

		boolean isTimed;
		if (type.equalsIgnoreCase(TYPE_TIMED)) {
			isTimed = true;
		} else if (type.equalsIgnoreCase(TYPE_NONTIMED)) {
			isTimed = false;
		} else {
			throw new Exception(MESSAGE_UNKNOWNTYPE_EXCEPTION);
		}
		return isTimed;
	}

	// @author Raghav A0091578R
	public static int getCurrentDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// format
																		// date
		Date now = new Date();
		String strDate = sdfDate.format(now);
		strDate = strDate.replace("-", "");
		return Integer.parseInt(strDate);
	}

	private static int getCurrentYear() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");// format date
		Date now = new Date();
		String strDate = sdfDate.format(now);
		strDate = strDate.replace("-", "");
		return Integer.parseInt(strDate);
	}

	private static int getCurrentMonth() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("MM");// format date
		Date now = new Date();
		String strDate = sdfDate.format(now);
		strDate = strDate.replace("-", "");
		return Integer.parseInt(strDate);
	}

	private static int getCurrentDay() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd");// format date
		Date now = new Date();
		String strDate = sdfDate.format(now);
		strDate = strDate.replace("-", "");
		return Integer.parseInt(strDate);
	}

	private static int getCurrentTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");// format date
		Date now = new Date();
		String strDate = sdfDate.format(now);
		strDate = strDate.replace(":", "");
		return Integer.parseInt(strDate);
	}

	private boolean isInt(String input) {
		boolean isInt;
		try {
			Integer.parseInt(input);
			isInt = true;
		} catch (Exception e) {
			isInt = false;
		}
		return isInt;
	}

	private boolean isInArray(String string, String[] array) {
		string = string.toUpperCase();
		for (String s : array) {
			if (s.contains(string)) {
				return true;
			}
		}
		return false;
	}

}
