package me.night.midnight.midnight_bot.audio;

import me.night.midnight.midnight_bot.audio.core.GuildMusicManager;
import me.night.midnight.midnight_bot.audio.core.PlayerManager;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class UserIntro extends ListenerAdapter {
	private long userId;
	private long guildId;
	private WeightedList<IntroDetail> intros;
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
	}
	
	public void execute(GenericGuildVoiceEvent e, VoiceChannel vc) {
		// Check for right guild
		if (e.getGuild().getIdLong() != guildId)
			return;
		
		// TODO add check for valid channel
		
		// Check if it is the right user
		if (e.getMember().getIdLong() != userId)
			return;
		
		// Check if bot is in a voice channel
		AudioManager audioManager = e.getGuild().getAudioManager();
		GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(e.getGuild());
		
		if (audioManager.isConnected()) {
			if (musicManager.player.getPlayingTrack() != null &&
				!musicManager.scheduler.queue.isEmpty())
				return;
		}
		
		// TODO play the user audio
	}
}
