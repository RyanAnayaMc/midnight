package me.night.midnight.midnight_bot.audio;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.Utility;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class ListIntros implements SlashCommand {
	private String name = "listintros";
	
	@Override
	public void run(SlashCommandEvent e) {
		// Determine user to get the intros for
		String id = e.getMember().getId();
		
		OptionMapping userOption = e.getOption("user");
		if (userOption != null) {
			Member member = e.getGuild().getMember(userOption.getAsUser());
			
			// Ensure that user has permission to use that argument
			if (!Utility.hasPermissionOver(e.getMember(), member)) {
				if (member.getIdLong() != e.getMember().getIdLong()) {
					e.reply("❌ You do not have privileges to use this command on that user.").setEphemeral(true).queue();
					return;
				}
			} else 
				id = member.getId();
		}
		
		// Get the list of intros for the user
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		
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
		IntroDetail intro = details.getByTrueIndex(index);
		String introFilename = intro.getPath();
		int startIndex = introFilename.length() - 1;
		
		while (introFilename.charAt(startIndex) != '\\') { startIndex--; }
		
		startIndex++;
		
		introFilename = introFilename.substring(startIndex);
		int volume = intro.getVolume();
		int weight = details.getWeightOf(index);
		
		eb.addField((index + 1) + " - " + introFilename, "🔊 " + volume + "%  ⚖" + weight + (intro.isAdminOnly() ? "  🔒" : ""), false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Lists all of your intros.")
				.addOption(OptionType.USER, "user", "Moderators and administrators only. See the intros for another user.", false);
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
