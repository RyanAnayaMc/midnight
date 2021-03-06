package me.night.midnight.midnight_bot.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Logger {
	private static boolean setup = false;
	private static PrintWriter pw;
	
	public static void setupLogger() {
		if (setup) {
			log("Logger is already set up! Cannot set up again!");
			return;
		}
		
		Calendar logDate = Calendar.getInstance();
		int year = logDate.get(Calendar.YEAR);
		int mo = logDate.get(Calendar.MONTH) + 1;
		int day = logDate.get(Calendar.DATE);
		int hr = logDate.get(Calendar.HOUR);
		int min = logDate.get(Calendar.MINUTE);
		int sec = logDate.get(Calendar.SECOND);
		
		String logFileName = String.format("botlog_%4d_%2d_%2d_%2d_%02d_%02d.log", year, mo, day, hr, min, sec);
		String logFilePath = BotSettings.LOG_DIRECTORY_DEFAULT + logFileName;
		new File(BotSettings.LOG_DIRECTORY_DEFAULT).mkdirs();
		
		File logFile = new File(logFilePath);
		System.out.println(logFilePath);
		if (logFile.exists())
			logFile.delete();
		
		try {
			logFile.createNewFile();
			pw = new PrintWriter(logFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error creating log file! Exiting...");
			System.exit(1);
		}
		
		setup = true;
	}
	
	public static void log(String event) {
		// Get prefix data
		Calendar logDate = Calendar.getInstance();
		int year = logDate.get(Calendar.YEAR);
		int mo = logDate.get(Calendar.MONTH) + 1;
		int day = logDate.get(Calendar.DATE);
		int hr = logDate.get(Calendar.HOUR);
		int min = logDate.get(Calendar.MINUTE);
		int sec = logDate.get(Calendar.SECOND);
		
		String time = String.format("%d/%d/%d %d:%02d:%02d ", year, mo, day, hr, min, sec);
		
		pw.write(time + event + "\n");
		System.out.println(time + event);
 	}
}
