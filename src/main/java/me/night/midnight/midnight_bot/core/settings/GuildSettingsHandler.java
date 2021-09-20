package me.night.midnight.midnight_bot.core.settings;

import java.util.ArrayList;
import java.util.List;

import me.night.midnight.midnight_bot.core.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class GuildSettingsHandler {
	private static List<GuildSettings> guildSettings = new ArrayList<GuildSettings>();
	
	/**
	 * Should be run when bot starts. Caches all the guild settings to be used
	 * by the program. Will not work without calling this method.
	 * 
	 * @param jda The ready JDA instance to retrieve guild settings from
	 */
	public static void retrieveSettings(JDA jda) {
		for (Guild g : jda.getGuilds())
			guildSettings.add(new GuildSettings(g));
		Logger.log("Successfully loaded the guild settings for all servers!");
	}
	
	/**
	 * Gets the GuildSettings for a guild with the specified id
	 * @param id The ID of the guild to get settings for
	 * @return The GuildSettings object, or null if not found
	 */
	public static GuildSettings getSettingsFor(long id) {
		for (GuildSettings gs : guildSettings)
			if (gs.GUILD_ID == id)
				return gs;
		
		return null;
	}
	
	/**
	 * Gets the GuildSettings for a specified guild
	 * @param g The Guild to get the settings for
	 * @return The GuildSettings object, or null if not found
	 */
	public static GuildSettings getSettingsFor(Guild g) {
		return getSettingsFor(g.getIdLong());
	}
}
