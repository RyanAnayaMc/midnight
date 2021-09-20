package me.night.midnight.midnight_bot.commands.moderation;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import me.night.midnight.midnight_bot.core.Logger;
import me.night.midnight.midnight_bot.core.MainBot;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class UnbanJob implements Job {
	public void execute(JobExecutionContext c) {
		JobDataMap jobData = c.getJobDetail().getJobDataMap();
		
		Guild g = MainBot.getJDA().getGuildById(jobData.getLongValue("gid"));
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(g);
	
		Role r = null;
		
		switch(jobData.getInt("bantype")) {
		case 0:
			r = settings.getTextBanRole();
			break;
		case 1:
			r = settings.getImageBanRole();
			break;
		case 2:
			r = settings.getVoiceBanRole();
			break;
		}
		
		g.removeRoleFromMember(jobData.getLong("uid"), r).queue();
		Logger.log("Removed role " + r.getName() + " from user " + jobData.getLong("uid") + " in guild " + g.getName());
	}
}
