package me.night.midnight.midnight_bot.moderation.messageinterceptor;

import java.io.File;

import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
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
	
	@Override
	public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
		// Try to retrieve message from the cache
		CachedMessage msg = cache.retreiveMessageById(e.getMessageIdLong());
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Deleted Message");
		
		if (msg == null) {
			eb.setAuthor("Unknown Author")
				.addField("Server", e.getGuild().getName(), false)
				.addField("Channel", e.getChannel().getAsMention(), false)
				.addField("Message", "The message was not found in the cache.", false);
		} else {
			eb.setAuthor("Author: " + msg.getMessage().getMember().getEffectiveName())
				.addField("Server", e.getGuild().getName(), false)
				.addField("Channel", e.getChannel().getAsMention(), false)
				.addField("Message", msg.getMessage().getContentRaw(), false);
		}
		
		for (Member m : GuildSettingsHandler.getSettingsFor(e.getGuild()).getMsgLogMods()) {
			m.getUser().openPrivateChannel().queue(
					ch -> {
						ch.sendMessageEmbeds(eb.build()).queue();
						
						if (msg.hasAttachments())
							for (File f : msg.getAttachments())
								ch.sendFile(f).queue();
						
						ch.close().queue();
					});
		}
	}
	
	@Override
	public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
		// Try to retreive message from the cache
		CachedMessage msg = cache.retreiveMessageById(e.getMessageIdLong());
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Edited Message")
				.setAuthor("Author: " + e.getMember().getEffectiveName())
				.addField("Server", e.getGuild().getName(), false)
				.addField("Channel", e.getChannel().getAsMention(), false)
				.addField("Edited Message", e.getMessage().getContentRaw(), false);
		
		if (msg == null)
			eb.addField("Original Messgae", "The original message was not found in the cache.", false);
		else
			eb.addField("Original Message", msg.getMessage().getContentRaw(), false);
			
		for (Member m : GuildSettingsHandler.getSettingsFor(e.getGuild()).getMsgLogMods()) {
			m.getUser().openPrivateChannel().queue(
					ch -> {
						ch.sendMessageEmbeds(eb.build()).queue();
						ch.close().queue();
					});
		}
	}
}
