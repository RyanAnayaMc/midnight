package me.night.midnight.midnight_bot.commands;

// SlashCommand to move all users in a voice chat to another

import java.util.List;

import me.night.midnight.midnight_bot.core.Emojis;
import me.night.midnight.midnight_bot.core.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class Migrate implements SlashCommand {
	private String name = "migrate";
	
	@Override
	public void run(SlashCommandEvent e) {
		// Make sure user is in a voice chat
		GuildVoiceState voiceState = e.getMember().getVoiceState();
		
		if (voiceState.getChannel() == null) {
			e.reply(Emojis.ERROR + " You are not in a voice channel!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
		// Make sure the channel supplied is a voice channel
		VoiceChannel channel = null;
		try {
			channel = (VoiceChannel) e.getOption("channel").getAsGuildChannel();
		} catch (ClassCastException ex) {
			e.reply(Emojis.ERROR + " The specified channel was not a voice channel!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
		// Move all the members
		List<Member> members = voiceState.getChannel().getMembers();
		
		for (Member m : members)
			e.getGuild().moveVoiceMember(m, channel).complete(); // queue() causes problems here when moving more than 5 users
		
		e.reply(Emojis.SUCCESS + " Moved " + members.size() + " members to " + channel.getName())
			.setEphemeral(true)
			.queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Moves all users in your voice chat to another voice chat.")
				.addOption(OptionType.CHANNEL, "channel", "The voice channel to move to.", true);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		if (!m.hasPermission(Permission.MESSAGE_MANAGE)) {
			reply.setContent(Emojis.ERROR + " You are not a moderator!")
				.setEphemeral(true)
				.queue();
			return false;
		}
		return true;
	}
}
