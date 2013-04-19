//@author A0104650U
// Erik Bodin 

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskOrganizerTest {

	@Test
	public void testGetTasks() throws Exception {

		// Since TaskOrganizer is a Singleton, this will cause some problems
		// with running the normal program using
		// the same instance of TaskOrganizer and same storage file. Therefore,
		// I have commented this out.

		/*
		 * TaskOrganizer taskOrganizer = TaskOrganizer.getInstance();
		 * taskOrganizer.addNontimedTask("C", 1);
		 * taskOrganizer.addNontimedTask("A", 3);
		 * taskOrganizer.addNontimedTask("B", 2);
		 * 
		 * String[][] testNontimed = taskOrganizer.getNontimedTasks(3,-1);
		 * assert(testNontimed.length==3);
		 * assert(testNontimed[0][1].equals("A"));
		 * assert(testNontimed[1][1].equals("B"));
		 * assert(testNontimed[2][1].equals("C"));
		 * assert(testNontimed[0][2].equals("3"));
		 * assert(testNontimed[1][2].equals("2"));
		 * assert(testNontimed[2][2].equals("1"));
		 * 
		 * taskOrganizer.editNontimed(1, "A new", 1);
		 * assert(testNontimed[0][1].equals("B"));
		 * System.out.println(testNontimed[0][1].equals("B"));
		 * taskOrganizer.editNontimed(3, "H", 3);
		 * assert(testNontimed[0][1].equals("H"));
		 * 
		 * 
		 * taskOrganizer.addTimedTask("A", 20121127, 800, 900);
		 * taskOrganizer.addTimedTask("B", 20121224, 700, 800);
		 * taskOrganizer.addTimedTask("C", 19621119, 1500, 1900);
		 * 
		 * String[][] testTimed = taskOrganizer.getTimedTasks(19000101,-1);
		 * assert(testTimed.length==3); assert(testTimed[0][1].equals("C"));
		 * assert(testTimed[1][1].equals("A"));
		 * assert(testTimed[2][1].equals("B"));
		 * assert(testTimed[0][2].equals("19621119"));
		 * assert(testTimed[1][2].equals("2012117"));
		 * assert(testTimed[2][2].equals("20121224"));
		 */

	}

	@Test
	public void test2() {
		TaskOrganizer taskOrganizer = TaskOrganizer.getInstance();
		assert (taskOrganizer.isValidTime(2359));
		assert (taskOrganizer.isValidTime(0000));
		assert (!taskOrganizer.isValidTime(2400));
		assert (taskOrganizer.isValidTime(0001));
		assert (!taskOrganizer.isValidTime(-0001));

		assert (!taskOrganizer.isValidDate(1950));
		assert (taskOrganizer.isValidDate(19901127));
		assert (taskOrganizer.isValidDate(19880912));
		assert (taskOrganizer.isValidDate(19980828));
		assert (!taskOrganizer.isValidDate(2100325));
		assert (!taskOrganizer.isValidDate(-1920325));
		assert (taskOrganizer.isValidDate(18990101));
		assert (taskOrganizer.isValidDate(20080229)); // correct if leap year
		assert (!taskOrganizer.isValidDate(20090229));// false if not leap year
	}

}
