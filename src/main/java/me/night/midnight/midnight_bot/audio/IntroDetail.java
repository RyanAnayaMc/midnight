package me.night.midnight.midnight_bot.audio;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IntroDetail {
	private String path;
	private int volume;
	private ExtraAction[] extraActions;
	private boolean adminOnly;
	private boolean IS_BLANK;
	
	/**
	 * Creates a new IntroDetail given JSON data
	 * @param jsonObj The JSON data to read
	 */
	public IntroDetail(JSONObject jsonObj) {
		try {
			path = jsonObj.getString("path");
			volume = jsonObj.getInt("volume");
			adminOnly = jsonObj.getBoolean("adminOnly");
			
			JSONArray extra = jsonObj.getJSONArray("extra");
			extraActions = new ExtraAction[extra.length()];
			
			for (int i = 0; i < extra.length(); i++) {
				switch (extra.getString(i)) {
				case "KICK":
					extraActions[i] = ExtraAction.KICK;
					break;
				}
			}
		} catch (JSONException e) {
			IS_BLANK = true;
		}
		
		IS_BLANK = false;
	}
	
	/**
	 * Creates a new IntroDetail
	 * @param p The path to the audio file
	 * @param vol The volume for the sound
	 * @param admin Whether or not this IntroDetail can only be changed by admins
	 * @param actions Any extra actions that will occur
	 */
	public IntroDetail(String p, int vol, boolean admin, ExtraAction...actions) {
		path = p;
		volume = Math.max(0, Math.min(vol, 500));
		extraActions = actions;
		adminOnly = admin;
		IS_BLANK = false;
	}
	
	/**
	 * Creates a blank IntroDetail what doesn't actually play an intro
	 */
	public IntroDetail() {
		IS_BLANK = true;
	}
	
	/**
	 * Returns this object as a JSONObject
	 */
	public JSONObject toJSON(int weight) {
		JSONArray jsonArr = new JSONArray();
		
		for (ExtraAction ea : extraActions)
			jsonArr.put(ea);
		
		JSONObject jsonObj = new JSONObject()
				.put("path", path)
				.put("volume", volume)
				.put("adminOnly", adminOnly)
				.put("extra", jsonArr)
				.put("weight", weight);
		
		return jsonObj;
	}
	
	public boolean isBlank() {
		return IS_BLANK;
	}
	
	public boolean isAdminOnly() {
		if (IS_BLANK)
			throw new EmptyIntroException();
		
		return adminOnly;
	}
	
	public String getPath() {
		if (IS_BLANK)
			throw new EmptyIntroException();
		
		return path;
	}
	
	public int getVolume() {
		if (IS_BLANK)
			throw new EmptyIntroException();
		
		return volume;
	}
	
	public ExtraAction[] getExtraActions() {
		if (IS_BLANK)
			throw new EmptyIntroException();
		
		return Arrays.copyOf(extraActions, extraActions.length);
	}
	
	protected enum ExtraAction {
		KICK
	}
	
	
}
