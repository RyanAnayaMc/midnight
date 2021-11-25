package me.night.midnight.midnight_bot.commands.autoreply;

import me.night.midnight.midnight_bot.autoreply.SimpleAutoReply;
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

public class CreateSimpleAutoReplyCommand implements SlashCommand {
	private String name = "createsimpleautoreply";
	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Creates a new simple auto reply.")
				.addOption(OptionType.STRING, "responses",
						"The possible responses the bot can give. Separate multiple responses with \"%%\"",
						true)
				.addOption(OptionType.STRING, "triggers",
						"The phrases that trigger the bot. Separate multiple triggers with \"%%\"",
						true)
				.addOption(OptionType.BOOLEAN, "ignoreCase",
						"Whether or not the triggers ignore case.", true)
				.addOption(OptionType.BOOLEAN, "checkWholeWord",
						  "Whether or not the bot checks only for whole words.", true)
				.addOption(OptionType.BOOLEAN, "useReply",
						"Whether or not the bot sends the response as a reply.", true);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		// Get list of responses
		List<String> responses = new ArrayList<String>();
		String[] splits = e.getOption("responses").getAsString().split("%%");
		if (splits.length <= 0) {
			e.reply(Emojis.ERROR + " You did not supply any responses!").setEphemeral(true).queue();
			return;
		} else for (String str : splits)
			responses.add(str);

		// Get list of triggers
		List<String> triggers = new ArrayList<String>();
		splits = e.getOption("triggers").getAsString().split("%%");
		if (splits.length <= 0) {
			e.reply(Emojis.ERROR + " YOu did not supply any triggers!").setEphemeral(true).queue();
			return;
		} else for (String str : splits)
			triggers.add(str);

		// Get booleans
		boolean ignoreCase = e.getOption("ignoreCase").getAsBoolean();
		boolean checkWholeWord = e.getOption("checkWholeWord").getAsBoolean();
		boolean useReply = e.getOption("useReply").getAsBoolean();

		// Create the autoreply
		SimpleAutoReply autoReply = new SimpleAutoReply(triggers, responses, ignoreCase, checkWholeWord, useReply);

		// TODO add autoreply to the guild's settings
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
