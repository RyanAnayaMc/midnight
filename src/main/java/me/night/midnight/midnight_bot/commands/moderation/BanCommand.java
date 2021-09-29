package me.night.midnight.midnight_bot.commands.moderation;

// Applies the ban given from one of the ban SlashCommands

import java.util.Calendar;

import org.quartz.SchedulerException;

import me.night.midnight.midnight_bot.core.Emojis;
import me.night.midnight.midnight_bot.core.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class BanCommand {
	/**
	 * Applies a ban and schedules an unban to a user
	 * @param e The SlashCommandEvent that triggered the command
	 * @param r The ban role to use
	 * @param b The type of ban this is
	 * @throws SchedulerException
	 */
	public static void ban(SlashCommandEvent e, Role r, BanType b) throws SchedulerException {
		Guild g = e.getGuild();
		Member m = e.getOption("user").getAsMember();
		
		long hours = e.getOption("hours").getAsLong();
		long minutes = e.getOption("minutes").getAsLong();
		
		// Validate time given
		if (hours < 0 || hours > 168) {
			e.reply(Emojis.ERROR + " Hours must be a number between 0 and 168 inclusive!");
			return;
		}
		if (minutes < 0 || minutes > 59) {
			e.reply(Emojis.ERROR + "  Minutes must be a number between 0 and 59 inclusive!");
			return;
		}
		
		// Get the time until the unban occurs
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, (int) hours);
		calendar.add(Calendar.MINUTE, (int) minutes);
	
		// Administer the ban to the user
		g.addRoleToMember(m, r).queue();
		Logger.log("Added role " + r.getName() + " to " + m.getNickname() + " in server " + g.getName() + ".");
		
		
		// Scheudle the unban for the user
		UnbanCore.scheduleUnbanEvent(m, b, calendar.getTime());
	}
}
