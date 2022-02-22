package me.night.midnight.midnight_bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.night.midnight.midnight_bot.audio.core.GuildMusicManager;
import me.night.midnight.midnight_bot.audio.core.PlayerManager;
import me.night.midnight.midnight_bot.core.Logger;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;

import java.util.Calendar;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class UserIntroHandler extends ListenerAdapter {
	public static final long MAX_DURATION_MS = 10000;
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		execute(e, e.getChannelJoined(), true);
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		execute(e, e.getChannelLeft(), false);
	}
	
	public void execute(GenericGuildVoiceEvent e, VoiceChannel vc, boolean isIntro) {
		// If user is bot, ignore
		if (e.getMember().getUser().isBot())
			return;
		
		// Get the guild settings
		Guild g = e.getGuild();
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(g);
		
		// TODO add check for valid channel
		
		// Get intros or outros for the user
		String id = e.getMember().getId();
		
		WeightedList<IntroDetail> sounds = isIntro ? settings.getIntrosFor(id) : settings.getOutrosFor(id);
		
		// Check if bot is in a voice channel
		AudioManager audioManager = e.getGuild().getAudioManager();
		GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(e.getGuild());
		
		if (audioManager.isConnected())
			return;
		
		// Check if user has intros
		if (sounds.isEmpty())
			return;
		
		// Get an intro/outro to play
		IntroDetail play = sounds.getRandom();
		
		if (play.isBlank())
			return;
		
		// Join the voice channel
		audioManager.openAudioConnection(vc);
		Logger.log("Joined voice chat " + vc.getName() + " in server " + e.getGuild().getName());
		
		// Set volume and play
		musicManager.player.setVolume(play.getVolume());
		PlayerManager.getInstance().loadAndPlay(e.getGuild(), play.getPath());

		// Enforce time limit
		if (!!play.ignoresTimeLimit() && musicManager.player.getPlayingTrack().getDuration() > settings.getMaxIntroLength()) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MILLISECOND, (int) settings.getMaxIntroLength());

			JobDetail stopIntro = newJob(IntroJob.class)
					.withIdentity("endintro" + id)
					.usingJobData("gid", e.getGuild().getIdLong())
					.build();

			SimpleTrigger introTrigger = (SimpleTrigger) newTrigger()
					.withIdentity("endintrotrigger" + id)
					.startAt(calendar.getTime())
					.forJob("endintro" + id)
					.build();
		}
		
		Logger.log("Playing " + (isIntro ? "intro" : "outro") + " for user " + e.getMember().getEffectiveName() +
				"\n" + play.getPath());
		
		// Run ending actions when over
		musicManager.player.addListener(new EndActions(g));
	}
	
	private class EndActions extends AudioEventAdapter {
		private Guild guild;
		
		private EndActions(Guild g) {
			guild = g;
		}
		
		@Override
		public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
			guild.getAudioManager().closeAudioConnection();
			
			PlayerManager.getInstance().getGuildMusicManager(guild).player.removeListener(this);
			
			Logger.log("Left voice chat.");
		}
	}
}
