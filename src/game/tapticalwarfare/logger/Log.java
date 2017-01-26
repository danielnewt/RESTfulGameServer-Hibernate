package game.tapticalwarfare.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Log {
	
	public enum LogLevel{
		INFO, WARN, ERROR;
	}
	
	private final static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public static void logInfo(String message){
		log("INFO : " + message);
	}
	
	public static void logError(String message){
		log("***ERROR*** : " + message);
	}
	
	public static void logWarn(String message){
		log("*WARN* : " + message);
	}
	
	private static void log(String message){
		Date now = Calendar.getInstance().getTime();        
		String timestamp = df.format(now);
		System.out.println("[" + timestamp + "] " + message);
	}
}
