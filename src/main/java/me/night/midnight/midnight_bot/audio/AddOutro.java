package me.night.midnight.midnight_bot.audio;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

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
		
		e.reply("📨 Check your DMs!").setEphemeral(true).queue();
		
		e.getUser().openPrivateChannel().queue(ev -> {
			ev.sendMessage("Send a `.mp3` file or a `.wav` file to use as your outro here.")
				.queue();
		});
		
		waiter.waitForEvent(PrivateMessageReceivedEvent.class, 
				ev -> {
					return e.getUser().getIdLong() == ev.getAuthor().getIdLong();
				}, 
				ev -> {
					List<Attachment> attachments = ev.getMessage().getAttachments();
					if (attachments.size() == 0) {
						ev.getChannel().sendMessage("❌ Error! Message did not have any attachments. Cancelling...").queue();
						return;
					}
					
					Attachment a = attachments.get(0);
					
					if (!a.getFileExtension().equals("wav") && !a.getFileExtension().equals("mp3")) {
						ev.getChannel().sendMessage("❌ Error! Attached file was not a `wav` or `mp3` file!").queue();
						return;
					}
					
					if (a.getSize() > MAX_SIZE) {
						String fileSize = String.format("%.2f", (double) a.getSize() / (1024 * 1024));
						ev.getChannel().sendMessage("❌ Error! File is too large! You are limited to 2MB. This file was " + 
								fileSize + "MB!").queue();
						return;
					}
					
					String directory = "audio\\" + e.getGuild().getId() + "\\" + e.getMember().getId() + "\\";
					new File(directory).mkdirs();
					
					intro = new File(directory + a.getFileName());
					a.downloadToFile(intro).thenAccept(file -> Logger.log("Downloaded outro file " + a.getFileName()));
					
					IntroDetail newIntro = new IntroDetail(directory + a.getFileName(),
														(int) e.getOption("volume").getAsLong(),
														false);
					
					outros.add(newIntro, Math.max(1, (int) e.getOption("weight").getAsLong()));
					settings.setOutrosFor(e.getMember().getId(), outros);
					ev.getChannel().sendMessage("✅ Successfully added outro!").queue();
				},
				1, TimeUnit.MINUTES,
				() -> {
					e.getUser().openPrivateChannel().queue(ch -> {
						ch.sendMessage("❌ Error! Timed out.").queue();
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
