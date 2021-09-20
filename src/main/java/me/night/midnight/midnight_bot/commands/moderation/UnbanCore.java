package me.night.midnight.midnight_bot.commands.moderation;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

import me.night.midnight.midnight_bot.core.Logger;
import me.night.midnight.midnight_bot.core.MainBot;
import net.dv8tion.jda.api.entities.Member;

public class UnbanCore {
	/**
	 * Schedules an event to remove a role-based ban from a Member
	 * @param m The Member to unban
	 * @param b The type of ban to remove
	 * @param triggerDate The date to remove the ban
	 * @throws SchedulerException when the scheduler failed to schedule the unban
	 */
	public static void scheduleUnbanEvent(Member m, BanType b, Date triggerDate) throws SchedulerException {
		JobDetail unban = newJob(UnbanJob.class)
				.withIdentity("unban" + m.getId(), b.toString())
				.usingJobData("uid", m.getIdLong())
				.usingJobData("gid", m.getGuild().getIdLong())
				.usingJobData("bantype", b.toInt())
				.build();
		
		SimpleTrigger unbanTrigger = (SimpleTrigger) newTrigger()
				.withIdentity("unbanTrig" + m.getId(), b.toString())
				.startAt(triggerDate)
				.forJob("unban" + m.getId(), b.toString())
				.build();
			
		Logger.log("Scheduled role removal for " + m.getNickname() + " at " + triggerDate.toString());
		MainBot.getScheduler().scheduleJob(unban, unbanTrigger);
	}
}
