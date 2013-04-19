//@author Santosh A01014577

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private FileWriter writer;
	private BufferedWriter buffWriter;
	private String fileName;
	private SimpleDateFormat sdfDate;
	private SimpleDateFormat sdfTime;
	Date now = new Date();
	
	public Logger(String fn) throws IOException{
		fileName = fn;
		sdfDate = new SimpleDateFormat("yyyy-MM-dd");//format date
		sdfTime = new SimpleDateFormat("hh-mm-ss");//format time		
	}
	
	public void logString(String string) throws IOException {
		writer = new FileWriter(fileName,true);
		buffWriter = new BufferedWriter(writer);
	    String strDate = sdfDate.format(now);
	    String strTime = sdfTime.format(now);
	    strTime = strTime.replace("-", ":");
		buffWriter.append("<"+strDate + " " + strTime +  "> " + string + "\n");
		buffWriter.close();
	}
}
