package me.night.midnight.midnight_bot.commands;

import me.night.midnight.midnight_bot.core.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class Ping implements SlashCommand {
	private String name = "ping";
	
	@Override
	public void run(SlashCommandEvent e) {
		long ping = e.getJDA().getGatewayPing();
		e.reply("Right now my ping to Discord is **" + ping + "ms**!").setEphemeral(true).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Pings the bot. Returns response time in ms.");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
}
