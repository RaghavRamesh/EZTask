/*
 * @author Raghav Ramesh A0091578R
 */

import static org.junit.Assert.*;

import org.junit.Test;


public class UITest {

	UI ui = UI.getInstance();
	

	@Test
	public void testConvertDateToUserFriendlyFormat() {
		
		//first and last in range
		assertEquals(ui.convertDateToUserFriendlyFormat("19000101"), "01-Jan-1900");
		assertEquals(ui.convertDateToUserFriendlyFormat("21001231"), "31-Dec-2100");
		//rest of the months
		assertEquals(ui.convertDateToUserFriendlyFormat("20000229"), "29-Feb-2000");
		assertEquals(ui.convertDateToUserFriendlyFormat("19940331"), "31-Mar-1994");
		assertEquals(ui.convertDateToUserFriendlyFormat("19450430"), "30-Apr-1945");
		assertEquals(ui.convertDateToUserFriendlyFormat("19880531"), "31-May-1988");
		assertEquals(ui.convertDateToUserFriendlyFormat("19870630"), "30-Jun-1987");
		assertEquals(ui.convertDateToUserFriendlyFormat("19940731"), "31-Jul-1994");
		assertEquals(ui.convertDateToUserFriendlyFormat("19640831"), "31-Aug-1964");
		assertEquals(ui.convertDateToUserFriendlyFormat("19920930"), "30-Sep-1992");
		assertEquals(ui.convertDateToUserFriendlyFormat("19581031"), "31-Oct-1958");
		assertEquals(ui.convertDateToUserFriendlyFormat("20121130"), "30-Nov-2012");
	}
    
	@Test
	public void testSetCommandLine() {

		ui.setCommandLine("add Discussion with groupmates; today; 9 pm");
		assertEquals(ui.getCommandLine(), "add Discussion with groupmates; today; 9 pm");
		ui.setCommandLine("add Meeting; today; now");
		assertEquals(ui.getCommandLine(), "add Meeting; today; now");
		ui.setCommandLine("add Movie time; 7 oct; now");
		assertEquals(ui.getCommandLine(), "add Movie time; 7 oct; now");
		ui.setCommandLine("add K9Orchards supper; sep 5; 1200");
		assertEquals(ui.getCommandLine(), "add K9Orchards supper; sep 5; 1200");
	}
	
	@Test
	
	public void testConvertTimeToUserFriendlyFormat() {
		
		//extreme cases
		assertEquals(ui.convertTimeToUserFriendlyFormat("0000"), "0:00 am");
		assertEquals(ui.convertTimeToUserFriendlyFormat("2359"), "11:59 pm");
		
		//checking 24 hr and 12 hr clocks
		assertEquals(ui.convertTimeToUserFriendlyFormat("0130"), "1:30 am");
		assertEquals(ui.convertTimeToUserFriendlyFormat("2134"), "9:34 pm");
		
		//mid point
		assertEquals(ui.convertTimeToUserFriendlyFormat("1200"), "12:00 pm");
		
	}
	
	@Test
	public void testGetUserInput() {
		
		ui.setCommandLine("add Meeting; today; now");
		assertEquals(ui.getUserInput(), "");
	}
	
	//@Test
	//public void testClearLog()
	
}
