package me.night.midnight.midnight_bot.audio;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.night.midnight.midnight_bot.core.Emojis;
import me.night.midnight.midnight_bot.core.Logger;
import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

/**
 * A {@link SlashCommand} to add a user outro. Accepts volume and weight as parameters.
 * @author night
 * @version 1.0.0
 * @see AddIntro
 * @see EditOutro
 * @see RemoveOutro
 * @see IntroDetail
 */
public class AddOutro implements SlashCommand {
	private String name = "addoutro";
	private EventWaiter waiter;
	private File intro;
	private final int MAX_SIZE = 2097152; // 2MB size limit
	
	public AddOutro(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void run(SlashCommandEvent e) {
		// Get list of intros
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> outros = settings.getOutrosFor(e.getMember().getId());
		
		// Request user to send audio file via DMs
		e.reply(Emojis.MAIL + " Check your DMs!").setEphemeral(true).queue();
		
		e.getUser().openPrivateChannel().queue(ev -> {
			ev.sendMessage("Send a `.mp3` file or a `.wav` file to use as your outro here.")
				.queue();
		});
		
		// Wait for user to send the file
		waiter.waitForEvent(PrivateMessageReceivedEvent.class, 
				ev -> {
					return e.getUser().getIdLong() == ev.getAuthor().getIdLong();
				}, 
				ev -> {
					// Make sure the message has attachments
					List<Attachment> attachments = ev.getMessage().getAttachments();
					if (attachments.size() == 0) {
						ev.getChannel().sendMessage(Emojis.ERROR + " Error! Message did not have any attachments. Cancelling...").queue();
						return;
					}
					
					// Only consider the first attachment
					Attachment a = attachments.get(0);
					
					// Make sure the attachment is of the right type
					if (!a.getFileExtension().equals("wav") && !a.getFileExtension().equals("mp3")) {
						ev.getChannel().sendMessage(Emojis.ERROR + " Error! Attached file was not a `wav` or `mp3` file!").queue();
						return;
					}
					
					// Make sure file is not too big
					if (a.getSize() > MAX_SIZE) {
						String fileSize = String.format("%.2f", (double) a.getSize() / (1024 * 1024));
						ev.getChannel().sendMessage(Emojis.ERROR + " Error! File is too large! You are limited to 2MB. This file was " + 
								fileSize + "MB!").queue();
						return;
					}
					
					// Download the file
					String directory = "audio\\" + e.getGuild().getId() + "\\" + e.getMember().getId() + "\\";
					new File(directory).mkdirs();
					
					intro = new File(directory + a.getFileName());
					a.downloadToFile(intro).thenAccept(file -> Logger.log("Downloaded outro file " + a.getFileName()));
					
					// Create the intro and add it
					IntroDetail newIntro = new IntroDetail(directory + a.getFileName(),
														(int) e.getOption("volume").getAsLong(),
														false);
					
					outros.add(newIntro, Math.max(1, (int) e.getOption("weight").getAsLong()));
					settings.setOutrosFor(e.getMember().getId(), outros);
					ev.getChannel().sendMessage(Emojis.SUCCESS + " Successfully added outro!").queue();
				},
				1, TimeUnit.MINUTES,
				() -> {
					e.getUser().openPrivateChannel().queue(ch -> {
						ch.sendMessage(Emojis.ERROR + " Error! Timed out.").queue();
					});
				});
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Add a new outro for this server.")
				.addOption(OptionType.INTEGER, "volume", "The volume to play the outro at.", true)
				.addOption(OptionType.INTEGER, "weight", "The weight for this outro. Higher weight means higher probability of this outro playing.", true);
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
