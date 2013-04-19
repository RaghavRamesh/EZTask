//@author A0104650U
// Erik Bodin 

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testParsing() throws Exception {

		Parser parser = Parser.getInstance();

		InputData inputData = parser.parseUserInput("add hello1 ; high");
		assert (inputData.command.equals(Command.ADD));
		assert (inputData.data.desc.equals("hello1"));
		assert (!inputData.data.taskIsTimed);
		NontimedTaskData nonTimedTaskData = (NontimedTaskData) inputData.data;
		assert (nonTimedTaskData.prio == 3);

		inputData = parser
				.parseUserInput("add hello2 ; 4 sep 2015 ; now - 10 pm");
		assert (inputData.command.equals(Command.ADD));
		assert (inputData.data.desc.equals("hello2"));
		assert (inputData.data.taskIsTimed);
		TimedTaskData timedTaskData = (TimedTaskData) inputData.data;
		assert (timedTaskData.date == 20150904);
		assert (timedTaskData.endTime == 2200);

		inputData = parser.parseUserInput("dELete c 5");
		assert (inputData.command.equals(Command.DELETE));
		assert (inputData.data.taskIsTimed);
		assert (inputData.data.id == 5);

		inputData = parser.parseUserInput("dELete p 22");
		assert (inputData.command.equals(Command.DELETE));
		assert (!inputData.data.taskIsTimed);
		assert (inputData.data.id == 22);

		inputData = parser
				.parseUserInput("edit 25 ; hello2 ; 2022 jan 24  ; 8 am - 1921");
		assert (inputData.command.equals(Command.EDIT));
		assert (inputData.data.desc.equals("hello2"));
		assert (inputData.data.id == 25);
		assert (inputData.data.taskIsTimed);
		timedTaskData = (TimedTaskData) inputData.data;
		assert (timedTaskData.date == 20220124);
		assert (timedTaskData.startTime == 800);
		assert (timedTaskData.endTime == 1921);

		inputData = parser.parseUserInput("edit 15 ; hello! ; low   ");
		assert (inputData.command.equals(Command.EDIT));
		assert (inputData.data.desc.equals("hello!"));
		assert (inputData.data.id == 15);
		assert (!inputData.data.taskIsTimed);
		nonTimedTaskData = (NontimedTaskData) inputData.data;
		assert (nonTimedTaskData.prio == 1);

		inputData = parser.parseUserInput("goto c 20121127");
		assert (inputData.command.equals(Command.GOTO));
		assert (inputData.data.taskIsTimed);
		timedTaskData = (TimedTaskData) inputData.data;
		assert (timedTaskData.date == 20121127);

		inputData = parser.parseUserInput("goto p medium");
		assert (inputData.command.equals(Command.GOTO));
		assert (!inputData.data.taskIsTimed);
		nonTimedTaskData = (NontimedTaskData) inputData.data;
		assert (nonTimedTaskData.prio == 2);

		inputData = parser.parseUserInput("searcH hello + 5");
		assert (inputData.data.keywords.equals(" hello + 5"));

		inputData = parser.parseUserInput("uNdo");
		assert (inputData.command.equals(Command.UNDO));

	}
}
