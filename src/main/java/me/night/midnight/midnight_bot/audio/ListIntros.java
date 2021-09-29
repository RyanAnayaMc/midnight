package me.night.midnight.midnight_bot.audio;

import me.night.midnight.midnight_bot.core.Emojis;

// SlashCommand to list a user's intros

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

/**
 * A {@link SlashCommand} that lists all of a user's intros and outros in a MessageEmbed. Data includes filename,
 * volume, weight, and whether or not it is locked to admin edits only.
 * @author night
 * @version 1.0.0
 * @see IntroDetail
 */
public class ListIntros implements SlashCommand {
	private String name = "listintros";
	
	@Override
	public void run(SlashCommandEvent e) {
		// Get the list of intros for the user
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		String id = e.getMember().getId();
		WeightedList<IntroDetail> intros = settings.getIntrosFor(id);
		WeightedList<IntroDetail> outros = settings.getOutrosFor(id);
		
		// Create the embed to list details
		EmbedBuilder eb = new EmbedBuilder()
				.setAuthor(e.getGuild().getName())
				.setThumbnail(e.getGuild().getIconUrl())
				.setTitle("Intros and Outros");
		
		// Add intro details
		eb.addField("**__INTROS__**", "", false);
		
		if (intros.isEmpty())
			eb.addField("No Intros", "", false);
		else for (int i = 0; i < intros.length(); i++)
			addDetails(i, intros, eb);
		
		eb.addBlankField(false);
		eb.addField("**__OUTROS__**", "", false);
		
		// Add outro details
		if (outros.isEmpty())
			eb.addField("No Outros", "", false);
		else for (int i = 0; i < outros.length(); i++)
			addDetails(i, outros, eb);
		
		e.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
	
	private void addDetails(int index, WeightedList<IntroDetail> details, EmbedBuilder eb) {
		// Get the intro
		IntroDetail intro = details.getByTrueIndex(index);
		
		// Get the intro's filename
		String introFilename = intro.getPath();
		int startIndex = introFilename.length() - 1;
		
		while (introFilename.charAt(startIndex) != '\\') { startIndex--; }
		startIndex++;
		
		introFilename = introFilename.substring(startIndex);
		
		// Get the volume and weight of it
		int volume = intro.getVolume();
		int weight = details.getWeightOf(index);
		
		// Add the intro data to the embed
		eb.addField((index + 1) + " - " + introFilename, Emojis.VOLUME + " " + volume + "%  " + Emojis.WEIGHT + weight + (intro.isAdminOnly() ? "  " + Emojis.LOCK : ""), false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Lists all of your intros.");
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
