//@author Santosh A01014577

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


public class LoggerTest {

	@Test
	public void test() throws IOException {
		Logger logger = new Logger("yes.txt");
		logger.logString("add who");
		logger.logString("what is this");
		logger.logString("test one");
		logger.logString("test two");		
	}

}
