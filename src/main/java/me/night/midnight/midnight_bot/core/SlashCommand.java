package me.night.midnight.midnight_bot.core;

// Basis for SlashCommands

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public interface SlashCommand {
	/**
	 * Gets the CommandData for the object
	 * @return the CommandData
	 */
	public CommandData getCommandData();
	
	/**
	 * Returns the name of this SlashCommand
	 */
	public String getName();
	
	/**
	 * Runs the command. Do not include permission checks here.
	 * @param e The SlashCommandEvent context for hte command
	 */
	public void run(SlashCommandEvent e);
	
	/**
	 * Checks if the given Member can run the command
	 * @param m	The member to check permissions for
	 * @param reply The ReplyAction to use to give an error message
	 * @return Whether or not the user can run the command
	 */
	public boolean checkPermissions(Member m, ReplyAction reply);
	
	/**
	 * Executes the command, checking permissions first
	 * @param e The SlashCommandEvent
	 */
	public default void execute(SlashCommandEvent e) {
		if (checkPermissions(e.getMember(), e.deferReply(true)))
			run(e);
	}

}
