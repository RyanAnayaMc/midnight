package me.night.midnight.midnight_bot.autoreply;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class AutomatedResponseImpl extends ListenerAdapter implements AutomatedResponse {
	private boolean useReply = false;
	
	@Override 
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		run(e.getMessage(), useReply);
	}
}
