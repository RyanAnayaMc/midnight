package me.night.midnight.midnight_bot.core;

// Class that handles global settings for the bot

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

public class BotSettings {
	private String logDirectory;
	private String tokenPath;
	
	public static final String BOT_CONFIG_LOCATION = "data\\config.json";
	public static final String BOT_CONFIG_DIRECTORY = "data\\";
	public static final String BOT_CONFIG_FILENAME = "config.json";
	public static final String LOG_DIRECTORY_DEFAULT = "data\\logs\\";
	public static final String TOKEN_PATH_DEFAULT = "data\\token.dat";
	public static final String GUILD_DATA_DEFAULT = "data\\guild\\";
	public static final String ATTACHMENT_CACHE_DEFAULT = "data\\attachments\\";
	
	public void readSettings() {
		// Attempt to open the file
		JSON.Reader jsonReader = null;
		JSONObject config = null;
		try {
			jsonReader = new JSON.Reader(BOT_CONFIG_LOCATION);
			config = jsonReader.getJsonObj();
		} catch (FileNotFoundException e) {
			// Did not find the config file, so a new one will be made
			config = getDefaultConfig();
			JSON.Writer jsonWriter = new JSON.Writer(config);
			jsonWriter.write(BOT_CONFIG_DIRECTORY, BOT_CONFIG_FILENAME);
		}
		
		// Attempt to read the json file
		try {
			logDirectory = config.getString("logDirectory");
			tokenPath = config.getString("tokenPath");
		}
		catch (JSONException e) {
			System.out.println("config.json is damaged. Please repair or delete it.");
			System.exit(1);
		}
	}
	
	/**
	 * Gets a default JSONObject for the configuration
	 * @return the JSONObject for a detault configuration
	 */
	private JSONObject getDefaultConfig() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("tokenPath", TOKEN_PATH_DEFAULT)
			.put("logDirectory", LOG_DIRECTORY_DEFAULT);
		
		return jsonObj;
	}
	
}
