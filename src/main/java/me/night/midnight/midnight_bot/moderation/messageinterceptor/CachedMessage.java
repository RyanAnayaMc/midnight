package me.night.midnight.midnight_bot.moderation.messageinterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.night.midnight.midnight_bot.core.BotSettings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;

public class CachedMessage {
	private Message msg;
	private List<File> attachment;
	
	public CachedMessage(Message m) {
		msg = m;
		
		// Download attachments
		List<Attachment> attachments = m.getAttachments();
		attachment = new ArrayList<File>(attachments.size());
		
		for (Attachment a : attachments) {
			Date d = new Date();
			@SuppressWarnings("deprecation")
			String timestamp = "" + d.getMonth() + d.getDay() + d.getHours() + d.getMinutes();
			String filename = BotSettings.ATTACHMENT_CACHE_DEFAULT + "/" + m.getGuild().getId() + "/" + timestamp + "_" + a.getFileName();
			File file = new File(filename);
			file.mkdirs();
			
			if (file.exists())
				file.delete();
			
			a.downloadToFile(file)
				.thenAccept(f -> {
					attachment.add(f);
				});
		}
	}
	
	public Message getMessage() {
		return msg;
	}
	
	public List<File> getAttachments() {
		return List.copyOf(attachment);
	}
	
	public boolean hasAttachments() {
		return attachment.size() > 0;
	}
}
