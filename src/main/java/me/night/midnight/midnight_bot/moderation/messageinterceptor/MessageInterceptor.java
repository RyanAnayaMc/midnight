package me.night.midnight.midnight_bot.moderation.messageinterceptor;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageInterceptor extends ListenerAdapter {
	private MessageCache cache;
	
	public MessageInterceptor() {
		cache = new MessageCache(2000);
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		cache.cacheMessage(e.getMessage());
	}
	
	// TODO finish MessageInterceptor
}
