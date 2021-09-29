package me.night.midnight.midnight_bot.audio;

import me.night.midnight.midnight_bot.core.Emojis;

// SlashCommand to remove an outro

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import me.night.midnight.midnight_bot.weighted.WeightedList;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

/**
 * A {@link SlashCommand} to remove an outro. Takes an index as a parameter.
 * @author night
 * @version 1.0.0
 * @see RemoveIntro
 * @see AddOutro
 * @see EditOutro
 * @see IntroDetail
 */
public class RemoveOutro implements SlashCommand {
	private String name = "removeoutro";
	
	@Override
	public CommandData getCommandData() {
	return new CommandData(name, "Removes an outro.")
				.addOption(OptionType.INTEGER, "index", "The outro to remove.", true);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		// Get user's intros
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> intros = settings.getOutrosFor(e.getMember().getId());
		
		// Get true index
		int intro = (int) (e.getOption("index").getAsLong() - 1);
		
		if (intro < 1 || intro >= intros.length()) {
			e.reply(Emojis.ERROR + " That outro does not exist. Check your outros with `/listintros`").setEphemeral(true).queue();
			return;
		}
		
		// Make sure it isn't admin only
		if (intros.getByTrueIndex(intro).isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply(Emojis.ERROR + " You must be an administrator to remove that outro!").setEphemeral(true).queue();
			return;
		}
		
		// Remove the intro
		intros.remove(intro);
		
		e.reply(Emojis.SUCCESS + " Successfully removed outro.").setEphemeral(true).queue();
		
		// Update outro data
		settings.setOutrosFor(e.getMember().getId(), intros);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
}
