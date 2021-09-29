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

/**
 * A class that handles user intros. When a member joins, that user's intros are retreived as a
 * {@link WeightedList} and a random {@link IntroDetail} is retreived from it. The bot then joins
 * the intro plays. A similar process happens for outros.
 * @author night
 * @version 1.0.0
 * @see IntroDetail
 */
public class UserIntroHandler extends ListenerAdapter {
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		execute(e, e.getChannelJoined(), true);
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		execute(e, e.getChannelLeft(), false);
	}
	
	/**
	 * Plays a user intro or outro if possible
	 * @param e The event from when the user joined or left
	 * @param vc The voice chat that was joined or left
	 * @param isIntro Whether or not an intro will be played
	 */
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
