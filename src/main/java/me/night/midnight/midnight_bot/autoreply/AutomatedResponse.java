package me.night.midnight.midnight_bot.autoreply;

import net.dv8tion.jda.api.entities.Message;

public interface AutomatedResponse {
	/**
	 * Checks to see if the message matches this AutomatedResponse's conditions
	 * @return Whether the message matches this response
	 */
	public boolean checkConditions(Message msg);
	
	/**
	 * Returns a response to the message
	 * @return The response to send
	 */
	public String getResponse();
	
	/**
	 * Sends a response message to a text channel. Checks the conditions.
	 * @param msg The message to respond to
	 * @param useReply Whether to send the message as a reply
	 */
	public default void run(Message msg, boolean useReply) {
		if (checkConditions(msg)) {
			if (useReply)
				msg.reply(getResponse()).queue();
			else
				msg.getChannel().sendMessage(getResponse()).queue();
	
		}
	}
}
