package me.night.midnight.midnight_bot.audio;

import me.night.midnight.midnight_bot.core.Emojis;

// SlashCommand to remove a user's outro

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
 * A {@link SlashCommand} to remove an intro. Takes an index as a parameter.
 * @author night
 * @version 1.0.0
 * @see RemoveOutro
 * @see AddIntro
 * @see EditIntro
 * @see IntroDetail
 */
public class RemoveIntro implements SlashCommand {
	private String name = "removeintro";
	
	@Override
	public CommandData getCommandData() {
	return new CommandData(name, "Removes an intro.")
				.addOption(OptionType.INTEGER, "index", "The intro to remove.", true);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		// Get the intro data
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> intros = settings.getIntrosFor(e.getMember().getId());
		
		// Get true index
		int intro = (int) (e.getOption("index").getAsLong() - 1);
		
		// Try to remove the intro
		if (intro < 1 || intro >= intros.length()) {
			e.reply(Emojis.ERROR + " That intro does not exist. Check your intros with `/listintros`").setEphemeral(true).queue();
			return;
		}
		
		// Make sure the intro isn't admin only
		if (intros.getByTrueIndex(intro).isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply(Emojis.ERROR + " You must be an administrator to remove that intro!").setEphemeral(true).queue();
			return;
		}
		
		// Remove the intro
		intros.remove(intro);
		
		e.reply(Emojis.SUCCESS + " Successfully removed intro.").setEphemeral(true).queue();
		
		// Update intro data
		settings.setIntrosFor(e.getMember().getId(), intros);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
	
}
