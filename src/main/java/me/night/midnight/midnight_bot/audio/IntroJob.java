package me.night.midnight.midnight_bot.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.night.midnight.midnight_bot.audio.core.GuildMusicManager;
import me.night.midnight.midnight_bot.audio.core.PlayerManager;
import me.night.midnight.midnight_bot.core.MainBot;
import net.dv8tion.jda.api.entities.Guild;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class IntroJob implements Job {
	@Override
	public void execute(JobExecutionContext c) throws JobExecutionException {
		JobDataMap jobData = c.getJobDetail().getJobDataMap();

		// Get required data
		Guild g = MainBot.getJDA().getGuildById(jobData.getLong("gid"));

		// Stop the current track
		GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(g);
		AudioTrack currentTrack = musicManager.player.getPlayingTrack();
		currentTrack.setPosition(currentTrack.getDuration() - 2);
	}
}
