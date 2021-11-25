package me.night.midnight.midnight_bot.commands.autoreply;

import me.night.midnight.midnight_bot.autoreply.SimpleAutoReply;
import me.night.midnight.midnight_bot.autoreply.TargetedAutoReply;
import me.night.midnight.midnight_bot.core.Emojis;
import me.night.midnight.midnight_bot.core.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.ArrayList;
import java.util.List;

public class CreateTargetedAutoReplyCommand implements SlashCommand {
	private String name = "createtargetedautoreply";

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Creates a targeted auto reply from a simple auto reply.")
				.addOption(OptionType.INTEGER, "index",
						"The simple auto reply to upgrade.", true)
				.addOption(OptionType.BOOLEAN, "ignoreMode",
						"If true, designated users cannot trigger this autoreply. If false," +
								" only the designated users can trigger this autoreply.", true)
				.addOption(OptionType.USER, "user1", "The user designated for this autoreply.", true)
				.addOption(OptionType.USER, "user2", "The user designated for this autoreply.", false)
				.addOption(OptionType.USER, "user3", "The user designated for this autoreply.", false)
				.addOption(OptionType.USER, "user4", "The user designated for this autoreply.", false)
				.addOption(OptionType.USER, "user5", "The user designated for this autoreply.", false);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		// TODO Get the SimpleAutoReply
		int index = (int) e.getOption("index").getAsLong();
		SimpleAutoReply simpleAutoReply = null;

		// Get extra details
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(e.getOption("user1").getAsUser().getIdLong());

		// TODO write this code but better
		// Get optional additional user ids
		try {
			userIds.add(e.getOption("user2").getAsUser().getIdLong());
		} catch (NullPointerException ex) {}
		try {
			userIds.add(e.getOption("user3").getAsUser().getIdLong());
		} catch (NullPointerException ex) {}
		try {
			userIds.add(e.getOption("user4").getAsUser().getIdLong());
		} catch (NullPointerException ex) {}
		try {
			userIds.add(e.getOption("user5").getAsUser().getIdLong());
		} catch (NullPointerException ex) {}

		// Get ignore mode
		boolean ignoreMode = e.getOption("ignoreMode").getAsBoolean();

		// Create the new targeted autoreply
		TargetedAutoReply targetedAutoReply = new TargetedAutoReply(simpleAutoReply, userIds, ignoreMode);

		// TODO add the targeted autoreply to the server settings
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
