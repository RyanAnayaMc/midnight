package me.night.midnight.midnight_bot.autoreply;

import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.entities.Message;

public class SimpleAutoReply extends AutomatedResponseImpl {
	private List<String> triggers;
	private List<String> responses;
	
	@Override
	/**
	 * Checks to see if the message matches the trigger
	 */
	public boolean checkConditions(Message msg) {
		for (String s : triggers)
			if (msg.getContentDisplay().contains(s))
				return true;
		return false;
	}

	@Override
	public String getResponse() {
		int index = new Random().nextInt(responses.size());
		return responses.get(index);
	}
}
