package me.night.midnight.midnight_bot.audio.core;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;

public class PlayerManager {
	private static PlayerManager instance;
	private final AudioPlayerManager PLAYER_MANAGER;
	private final Map<Long, GuildMusicManager> MUSIC_MANAGERS;
	
	private PlayerManager() {
		this.MUSIC_MANAGERS = new HashMap<>();
		this.PLAYER_MANAGER = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
		AudioSourceManagers.registerLocalSource(PLAYER_MANAGER);
	}
	
	public synchronized GuildMusicManager getGuildMusicManager(Guild g)
	{
		long guildId = g.getIdLong();
		GuildMusicManager musicManager = MUSIC_MANAGERS.get(guildId);
		
		if (musicManager == null) {
			musicManager = new GuildMusicManager(PLAYER_MANAGER);
			MUSIC_MANAGERS.put(guildId, musicManager);
		}
		
		g.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
	}
	
	public void loadAndPlay(Guild g, String trackUrl) {
		GuildMusicManager musicManager = getGuildMusicManager(g);
		
		PLAYER_MANAGER.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				// c.sendMessage(songInfo(track, "✔ Song Queued", false).build()).queue();
				play(musicManager, track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				
			}
			
			@Override
			public void noMatches() {
				// c.sendMessage("❌ Nothing found at `" + trackUrl + "`!").queue();
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				// c.sendMessage("❌ Could not play: `" + exception.getMessage() + "`").queue();
			}
		});
	}
	
	private void play(GuildMusicManager musicManager, AudioTrack track) {
		musicManager.scheduler.queue(track);
	}
	
	public static synchronized PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		
		return instance;
	}
	
	/*
	public static EmbedBuilder songInfo(AudioTrack track, String title, boolean showCurrentTime) {
		EmbedBuilder info = new EmbedBuilder();
		info.setColor(0xAA00FF);
		info.setAuthor("『 Night Bot ♪ 』", null, BotParameters.getBotAvatarUrl());
		info.setTitle(title);
		info.addField("🎵 Name", track.getInfo().title, false);
		info.addField("👤 Channel", track.getInfo().author, true);
		if (showCurrentTime)
			info.addField("⌚ Time", msToProperTime(track.getPosition()) + " / " + msToProperTime(track.getInfo().length), true);
		else
			info.addField("⌚ Duration", msToProperTime(track.getInfo().length), true);
		info.setThumbnail("http://img.youtube.com/vi/" + track.getIdentifier() + "/0.jpg");
		
		return info;
	}*/
	
	public static String msToProperTime(long timeMs) {
		String time = "";
		long seconds = (timeMs / 1000) % 60;
		long minute = (timeMs / 60000) % 60;
		long hour = (timeMs / 3600000) % 24;
		
		time = String.format("%02d:%02d:%02d", hour, minute, seconds);
		
		return time;
	}
}
