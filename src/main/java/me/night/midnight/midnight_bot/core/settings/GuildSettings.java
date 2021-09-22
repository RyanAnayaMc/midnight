package me.night.midnight.midnight_bot.core.settings;

import java.io.FileNotFoundException;

import org.json.JSONArray;
import org.json.JSONObject;

import me.night.midnight.midnight_bot.core.BotSettings;
import me.night.midnight.midnight_bot.core.JSON;
import me.night.midnight.midnight_bot.core.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

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
	 * @param GUILD_ID the ID of the guild to make the object for
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
				.put("vcCmds",
						new JSONArray())
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
	 */
	public Role getTextBanRole() {
		long id = jsonObj.getJSONObject("modprefs").getLong("textBanRole");
		return jda.getGuildById(GUILD_ID).getRoleById(id);
	}
	
	/**
	 * Returns the role for image bans in the server
	 * @return The role. If no set or not found, returns null.
	 */
	public Role getImageBanRole() {
		long id = jsonObj.getJSONObject("modprefs").getLong("imgBanRole");
		return jda.getGuildById(GUILD_ID).getRoleById(id);
	}
	
	/**
	 * Returns the role for voice bans in the server
	 * @return The role. If no set or not found, returns null.
	 */
	public Role getVoiceBanRole() {
		long id = jsonObj.getJSONObject("modprefs").getLong("vcBanRole");
		return jda.getGuildById(GUILD_ID).getRoleById(id);
	}
	
	/**
	 * Updates the role for text bans and updates the config.
	 * @param r The new Role for text bans.
	 */
	public void setTextBanRole(Role r) {
		long id = r.getIdLong();
		
		JSONObject modprefs = jsonObj.getJSONObject("modprefs");
		modprefs.put("textBanRole", id);
		jsonObj.put("modprefs", modprefs);
		
		write();
	}
	
	/**
	 * Updates the role for image bans and updates the config.
	 * @param r The new Role for image bans.
	 */
	public void setImageBanRole(Role r) {
		long id = r.getIdLong();
		
		JSONObject modprefs = jsonObj.getJSONObject("modprefs");
		modprefs.put("imgBanRole", id);
		jsonObj.put("modprefs", modprefs);
		
		write();
	}
	
	/**
	 * Updates the role for voice bans and updates the config.
	 * @param r The new Role for voice bans.
	 */
	public void setVoiceBanRole(Role r) {
		long id = r.getIdLong();
		
		JSONObject modprefs = jsonObj.getJSONObject("modprefs");
		modprefs.put("vcBanRole", id);
		jsonObj.put("modprefs", modprefs);
		
		write();
	}
	
	/**
	 * Retrieves an array of Members that can view the guild's message log
	 * @return
	 */
	public Member[] getMsgLogMods() {
		JSONArray jsonArr = jsonObj.getJSONArray("msglogmods");
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
		JSONArray jsonArr = jsonObj.getJSONArray("msglogmods");
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
		
		JSONArray jsonArr = jsonObj.getJSONArray("msglogmods");
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
		JSONArray jsonArr = jsonObj.getJSONArray("msglogmods");
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
	
	private void write() {
		// Writes this JSON file
		JSON.Writer writer = new JSON.Writer(jsonObj);
		writer.write(BotSettings.GUILD_DATA_DEFAULT,  GUILD_ID + ".json");
	}
}
