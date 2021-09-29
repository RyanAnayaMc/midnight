package me.night.midnight.midnight_bot.core;

// Class that handles logging of events

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Logger {
	private static boolean setup = false;
	private static PrintWriter pw;
	
	public static void setupLogger() {
		// Make sure the logger is not set up already
		if (setup) {
			log("Logger is already set up! Cannot set up again!");
			return;
		}
		
		// Get date data
		Calendar logDate = Calendar.getInstance();
		int year = logDate.get(Calendar.YEAR);
		int mo = logDate.get(Calendar.MONTH) + 1;
		int day = logDate.get(Calendar.DATE);
		int hr = logDate.get(Calendar.HOUR);
		int min = logDate.get(Calendar.MINUTE);
		int sec = logDate.get(Calendar.SECOND);
		
		// Create the log file directory if it does not exist
		String logFileName = String.format("botlog_%4d_%2d_%2d_%2d_%02d_%02d.log", year, mo, day, hr, min, sec);
		String logFilePath = BotSettings.LOG_DIRECTORY_DEFAULT + logFileName;
		new File(BotSettings.LOG_DIRECTORY_DEFAULT).mkdirs();
		
		// Create the log file
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
	
	/**
	 * Logs an event to file and console
	 * @param event
	 */
	public static void log(String event) {
		// Make sure bot is setup
		if (!setup) {
			System.out.println("Error logging event. Logger is not set up yet.");
			return;
		}
		
		// Get prefix data
		Calendar logDate = Calendar.getInstance();
		int year = logDate.get(Calendar.YEAR);
		int mo = logDate.get(Calendar.MONTH) + 1;
		int day = logDate.get(Calendar.DATE);
		int hr = logDate.get(Calendar.HOUR);
		int min = logDate.get(Calendar.MINUTE);
		int sec = logDate.get(Calendar.SECOND);
		
		// Get the time data for log
		String time = String.format("%d/%d/%d %d:%02d:%02d ", year, mo, day, hr, min, sec);
		
		// Write the log event to file and print it
		pw.write(time + event + "\n");
		System.out.println(time + event);
 	}
}
