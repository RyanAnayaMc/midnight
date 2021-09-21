package me.night.midnight.midnight_bot.moderation.messageinterceptor;

import net.dv8tion.jda.api.entities.Message;

public class MessageCache {
	private CachedMessage[] messages;
	private  int index;
	
	/**
	 * Creates a MessageCache of a given size
	 * @param size The size of the MessageCache
	 */
	public MessageCache(int size) {
		messages = new CachedMessage[size];
		index = 0;
	}
	
	/**
	 * Creates a MessageCache that holds 1000 messages
	 */
	public MessageCache() {
		this(1000);
	}
	
	/**
	 * Adds a message to the MessageCache, saving its attachments
	 * @param msg The message to cache
	 */
	public void cacheMessage(Message msg) {
		int msgIndex = hasMessage(msg.getIdLong());
		
		if (msgIndex == -1) {
			messages[index] = new CachedMessage(msg);
			index++;
			
			if (index >= messages.length)
				index = 0;
		}
		else
			messages[msgIndex] = new CachedMessage(msg);
		
	}
	
	/**
	 * Returns a requested CachedMessage
	 * @param id The ID of the message to retrieve
	 * @return The CachedMessage, or null if not found.
	 */
	public CachedMessage retreiveMessageById(long id) {
		int msgIndex = hasMessage(id);
		if (msgIndex == -1)
			return null;
		else
			return messages[msgIndex];
	}
	
	/**
	 * Gets the index of a message with the given ID
	 * @param id The ID of the message to find
	 * @return The index of the message, or -1 if not found
	 */
	private int hasMessage(long id) {
		for (int i = 0; i < messages.length; i++) {
			if (messages[i] == null)
				return -1;
			if (messages[i].getMessage().getIdLong() == id)
				return i;
		}
		
		return -1;
	}
}
