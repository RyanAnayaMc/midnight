package me.night.midnight.midnight_bot.core;

// Class that handles interchanging local .json files and org.json.JSONObject objects

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
		 * @param obj The JSONObject to use
		 */
		public Writer(JSONObject obj) {
			jsonObj = obj;
		}
		
		/**
		 * Writes the JSONObject to a file
		 * @param directory The folder to save the file in
		 * @param filename The filename for the JSONObject
		 */
		public void write(String directory, String filename) {
			// Make the directory
			File outputFile = new File(directory);
			outputFile.mkdirs();
			
			// Make the file
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
			
			// Perform write operation
			pw.write(jsonObj.toString(5));
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
		
		/**
		 * Returns the raw String read from the JSON file
		 * @return
		 */
		public String getJsonString() {
			return jsonString;
		}
		
		/**
		 * Get the JSONObject read from the file
		 * @return
		 */
		public JSONObject getJsonObj() {
			return jsonObj;
		}
	}
	

}
