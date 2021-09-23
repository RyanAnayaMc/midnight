package me.night.midnight.midnight_bot.commands;

import java.util.List;

import me.night.midnight.midnight_bot.core.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Migrate implements SlashCommand {

	@Override
	public void run(SlashCommandEvent e) {
		GuildVoiceState voiceState = e.getMember().getVoiceState();
		
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			e.reply("❌ You are not a moderator!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
		if (voiceState.getChannel() == null) {
			e.reply("❌ You are not in a voice channel!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
		VoiceChannel channel = null;
		try {
			channel = (VoiceChannel) e.getOption("channel").getAsGuildChannel();
		} catch (ClassCastException ex) {
			e.reply("❌ The specified channel was not a voice channel!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
		List<Member> members = voiceState.getChannel().getMembers();
		
		for (Member m : members)
			e.getGuild().moveVoiceMember(m, channel).queue();
		
		e.reply("✅ Moved " + members.size() + " members to " + channel.getName())
			.setEphemeral(true)
			.queue();
	}
}
