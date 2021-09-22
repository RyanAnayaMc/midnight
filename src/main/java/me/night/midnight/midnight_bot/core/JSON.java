package me.night.midnight.midnight_bot.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.JSONObject;

public class JSON {
	public static class Writer {
		private JSONObject jsonObj;
		
		/**
		 * Creates a new JSON.Writer from the given JSONObject
		 * @param obj The JSONObject ot use
		 */
		public Writer(JSONObject obj) {
			jsonObj = obj;
		}
		
		public void write(String directory, String filename) {
			File outputFile = new File(directory);
			outputFile.mkdirs();
			outputFile = new File(directory + filename);
			PrintWriter pw = null;
			
			try {
				if (!outputFile.exists())
					outputFile.createNewFile();
				pw = new PrintWriter(outputFile);
			} catch (IOException e) {
				System.out.println("Failed opening and creating file! Exiting...");
				System.exit(0);
			}
			
			pw.write(jsonObj.toString());
			pw.close();
		}
	}
	
	public static class Reader {
		private String jsonString;
		private JSONObject jsonObj;
		
		/**
		 * Creates a new JSON.Reader from the given path
		 * @param jsonPath The path to the JSON file
		 * @throws FileNotFoundException when the given jsonPath does not give a valid file
		 */
		public Reader(String jsonPath) throws FileNotFoundException {
			// Read json file
			File jsonFile = new File(jsonPath);
			
			// Open json file
			Scanner jsonScanner = new Scanner(jsonFile);
			
			jsonString = "";
			
			while (jsonScanner.hasNext())
				jsonString += jsonScanner.nextLine();
			
			jsonObj = new JSONObject(jsonString);
			
			jsonScanner.close();
		}
		
		public String getJsonString() {
			return jsonString;
		}
		
		public JSONObject getJsonObj() {
			return jsonObj;
		}
	}
	

}
