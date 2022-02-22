package me.night.midnight.midnight_bot.core.settings;

import me.night.midnight.midnight_bot.audio.IntroDetail;
import me.night.midnight.midnight_bot.core.BotSettings;
import me.night.midnight.midnight_bot.core.JSON;
import me.night.midnight.midnight_bot.core.Logger;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * A wrapping of a JSONObject to make it easier to grab certain attributes
 * 
 * @author night
 */

public class GuildSettings {
	private JSONObject jsonObj;
	protected final long GUILD_ID;
	private JDA jda;
	
	/**
	 * Creates a default GuildSettings object for the given GUILD_ID, or open one if present
	 * @param g the Guild to make the object for
	 */
	public GuildSettings(Guild g) {
		jda = g.getJDA();
		GUILD_ID = g.getIdLong();
		
		try {
			JSON.Reader guildSettings = new JSON.Reader(BotSettings.GUILD_DATA_DEFAULT + GUILD_ID + ".json");
			jsonObj = guildSettings.getJsonObj();
		}
		catch (FileNotFoundException e ) {
			// Make a blank settings object
			Logger.log("Creating guild settings json for guild " + g.getName() + " [" + GUILD_ID + "]");
			jsonObj = getBlankConfig();
			write();
		}

	}

	//region Helper Methods

	/**
	 * Writes the GuildSettings object to a JSON file
	 */
	private void write() {
		// Writes this JSON file
		JSON.Writer writer = new JSON.Writer(jsonObj);
		writer.write(BotSettings.GUILD_DATA_DEFAULT,  GUILD_ID + ".json");
	}

	/**
	 * Retreives a JSONArray based on a sequence of keys
	 * Fixes the JSON if any keys don't exist
	 * @param dflt The default value if not found
	 * @param keys The keys, in order, to the wanted key
	 * @return The JSONArray associated with the keys
	 */
	private JSONArray retrieveArray(JSONArray dflt, String...keys) {
		JSONObject json = retrieveParentJsonObject(keys);
		
		String lastKey = keys[keys.length - 1];
		JSONArray item = dflt;
		
		try {
			item = json.getJSONArray(lastKey);
		} catch (JSONException e) {
			json.put(lastKey, dflt);
		}
		
		write();
		return item;
	}
	
	/**
	 * Retrieves a long based on a sequence of keys
	 * Fixes the JSON if any keys don't exist
	 * @param dflt The default value if not found
	 * @param keys The keys, in order, to the wanted key
	 * @return The long value associated with the keys
	 */
	private long retrieveLong(long dflt, String...keys) {
		JSONObject json = retrieveParentJsonObject(keys);
		
		// json is now the last nested json
		String lastKey = keys[keys.length - 1];
		long item = dflt;
		try {
			item = json.getLong(lastKey);
		} catch (JSONException e) {
			json.put(lastKey, dflt);
		}
		
		write();
		return item;
	}
	
	/**
	 * Sets a long based on a sequence of keys, creating JSON objects as needed
	 * @param value The long value to put
	 * @param keys The keys in order to the wanted key
	 */
	private void setLong(long value, String...keys) {
		JSONObject json = retrieveParentJsonObject(keys);
		
		String lastKey = keys[keys.length - 1];
		json.put(lastKey, value);
		write();
	}
	
	private void setJSONArray(JSONArray newJson, String...keys) {
		JSONObject json = retrieveParentJsonObject(keys);
		
		String lastKey = keys[keys.length - 1];
		json.put(lastKey, newJson);
		write();
	}
	
	private JSONObject retrieveParentJsonObject(String[] keys) {
		JSONObject json = jsonObj;
		
		for (int i = 0; i < keys.length - 1; i++) {
			try {
				json = json.getJSONObject(keys[i]);
			} catch (JSONException e) {
				json.put(keys[i], new JSONObject());
				json = json.getJSONObject(keys[i]);
			}
		}
		
		return json;
	}
	
	/**
	 * Returns a blank GuildSettings JSONObject
	 * @return a blank JSONObject for guild settings
	 */
	private JSONObject getBlankConfig() {
		// Create the guild object
		JSONObject guildSettings = new JSONObject()
				.put("textCmds", 
						 new JSONObject()
							.put("simple", new JSONArray())
							.put("complex", new JSONArray())
							.put("regex", new JSONArray())
					)
				.put("userIntros",
						new JSONObject())
				.put("userOutros",
						new JSONObject())
				.put("usernameChangers",
						new JSONArray())
				.put("msglogmods", 
						new JSONArray())
				.put("imageCmds",
						new JSONObject()
							.put("classic",
									new JSONArray())
							.put("slash",
									new JSONArray())
					)
				.put("modprefs", new JSONObject()
						.put("textBanRole", 0L)
						.put("imgBanRole", 0L)
						.put("vcBanRole", 0L));
		
		return guildSettings;
	}
	
	/**
	 * Returns the role for text bans in the server
	 * @return The role. If no set or not found, returns null.
	 * @deprecated Discord's Timeout feature works better in most cases.
	 */
	//endregion

	//region Moderation Commands (Deprecated)
	@Deprecated
	public Role getTextBanRole() {
		long id = retrieveLong(0, "modprefs", "textBanRole");
		
		return jda.getGuildById(GUILD_ID).getRoleById(id);
	}
	
	/**
	 * Returns the role for image bans in the server
	 * @return The role. If no set or not found, returns null.
	 * @deprecated Discord's Timeout feature works better in most cases.
	 */
	@Deprecated
	public Role getImageBanRole() {
		long id = retrieveLong(0, "modprefs", "imgBanRole");
		
		return jda.getGuildById(GUILD_ID).getRoleById(id);
	}
	
	/**
	 * Returns the role for voice bans in the server
	 * @return The role. If no set or not found, returns null.
	 * @deprecated Discord's Timeout feature works better in most cases.
	 */
	@Deprecated
	public Role getVoiceBanRole() {
		long id = retrieveLong(0, "modprefs", "vcBanRole");
		
		return jda.getGuildById(GUILD_ID).getRoleById(id);
	}
	
	/**
	 * Updates the role for text bans and updates the config.
	 * @param r The new Role for text bans.
	 * @deprecated Discord's Timeout feature works better in most cases.
	 */
	@Deprecated
	public void setTextBanRole(Role r) {
		long id = r.getIdLong();
		
		setLong(id, "modprefs", "textBanRole");
	}

	/**
	 * Updates the role for image bans and updates the config.
	 * @param r The new Role for image bans.
	 * @deprecated Discord's Timeout feature works better in most cases.
	 */
	@Deprecated
	public void setImageBanRole(Role r) {
		long id = r.getIdLong();
		
		setLong(id, "modprefs", "imgBanRole");
	}
	
	/**
	 * Updates the role for voice bans and updates the config.
	 * @param r The new Role for voice bans.
	 * @deprecated Discord's Timeout feature works better in most cases.
	 */
	@Deprecated
	public void setVoiceBanRole(Role r) {
		long id = r.getIdLong();
		
		setLong(id, "modprefs", "vcBanRole");
	}
	//endregion

	//region Message Log commands
	/**
	 * Retrieves an array of Members that can view the guild's message log
	 * @return
	 */
	public Member[] getMsgLogMods() {
		JSONArray jsonArr = retrieveArray(new JSONArray(), "msglogmods");
		
		Member[] members = new Member[jsonArr.length()];
		for (int i = 0; i < jsonArr.length(); i++) {
			long id = jsonArr.getLong(i);
			Member mem = jda.getGuildById(GUILD_ID).retrieveMemberById(id).complete();
			members[i] = mem;
		}
		
		return members;
	}
	
	/**
	 * Returns whether or not a given user can view the message log
	 * @param id The ID of the user to check
	 * @return Whether or not the user can view the message log
	 */
	public boolean isMsgLogMod(long id) {
		JSONArray jsonArr = retrieveArray(new JSONArray(), "msglogmods");
		for (int i = 0; i < jsonArr.length(); i++) {
			long uid = jsonArr.getLong(i);
			if (id == uid)
				return true;
		}
		return false;
	}
	
	/**
	 * Lets a user view the message log
	 * @param id The user to add
	 * @return Whether or not the user was added
	 */
	public boolean addMsgLogMod(long id) {
		if (isMsgLogMod(id))
			return false;
		
		JSONArray jsonArr = retrieveArray(new JSONArray(), "msglogmods");
		jsonArr.put(id);
		
		jsonObj.put("msglogmods", jsonArr);
		write();
		
		return true;
	}
	
	/**
	 * Removes a user from the message log
	 * @param id The user to remove
	 * @return Whether or not the user was removed
	 */
	public boolean removeMsgLogMod(long id) {
		JSONArray jsonArr = retrieveArray(new JSONArray(), "msglogmods");
		boolean retVal = false;
		
		for (int i = 0; i < jsonArr.length(); i++) {
			if (jsonArr.getLong(i) == id) {
				jsonArr.remove(i);
				retVal = true;
			}
		}
		
		write();
		
		return retVal;
	}
	//endregion

	//region User Intros

	/**
	 * @return The maximum duration of a user intro on this server in milliseconds.
	 */
	public long getMaxIntroLength() {
		long maxLength = retrieveLong(10000, "userIntros", "maxIntroLength");
		return maxLength;
	}

	/**
	 * Gets a weighted list of IntroDetails for a user
	 * @param userId
	 * @return
	 */
	public WeightedList<IntroDetail> getIntrosFor(String userId) {
		WeightedList<IntroDetail> intros = new WeightedList<IntroDetail>();
		JSONArray userIntrosJson = retrieveArray(new JSONArray(), "userIntros", userId);
		
		for (int i = 0; i < userIntrosJson.length(); i++) {
			JSONObject intro = userIntrosJson.getJSONObject(i);
			IntroDetail introDetail = new IntroDetail(intro);
			int weight = intro.getInt("weight");
			intros.add(introDetail, weight);
		}
		
		return intros;
	}
	
	/**
	 * Gets a weighted list of IntroDetails for outros for a user
	 * @param userId The user to get the outros for
	 * @return The outros of the user
	 */
	public WeightedList<IntroDetail> getOutrosFor(String userId) {
		WeightedList<IntroDetail> outros = new WeightedList<IntroDetail>();
		JSONArray userOutrosJson = retrieveArray(new JSONArray(), "userOutros", userId);
		
		for (int i = 0; i < userOutrosJson.length(); i++) {
			JSONObject outro = userOutrosJson.getJSONObject(i);
			IntroDetail outroDetail = new IntroDetail(outro);
			int weight = outro.getInt("weight");
			outros.add(outroDetail, weight);
		}
		
		return outros;
	}
	
	/**
	 * Sets the intros for a user
	 * @param userId The user to set the intros for
	 * @param intros The intros to add
	 */
	public void setIntrosFor(String userId, WeightedList<IntroDetail> intros) {
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < intros.length(); i++) {
			int weight = intros.getWeightOf(i);
			jsonArray.put(intros.getByTrueIndex(i).toJSON(weight));
		}
		
		setJSONArray(jsonArray, "userIntros", userId);
	}
	
	/**
	 * Sets the outros for a user
	 * @param userId The user to set the outros for
	 * @param outros The outros to add
	 */
	public void setOutrosFor(String userId, WeightedList<IntroDetail> outros) {
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < outros.length(); i++) {
			int weight = outros.getWeightOf(i);
			jsonArray.put(outros.getByTrueIndex(i).toJSON(weight));
		}
		
		setJSONArray(jsonArray, "userOutros", userId);
	}
	//endregion
}
