package me.night.midnight.midnight_bot.core;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommand {
	public void run(SlashCommandEvent e);
}
