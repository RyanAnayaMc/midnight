package me.night.midnight.midnight_bot.commands;

import me.night.midnight.midnight_bot.core.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Ping implements SlashCommand {
	@Override
	public void run(SlashCommandEvent e) {
		long ping = e.getJDA().getGatewayPing();
		e.reply("Right now my ping to Discord is **" + ping + "ms**!").queue();
	}
}
