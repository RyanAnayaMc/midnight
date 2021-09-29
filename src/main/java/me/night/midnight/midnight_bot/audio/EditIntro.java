package me.night.midnight.midnight_bot.audio;

import me.night.midnight.midnight_bot.core.Emojis;

/**
 * {@link SlashCommand} to edit an intro
 * 
 */

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
 * A {@link SlashCommand} to edit an intro's volume and weight. Has required parameter index and optional parameters newvol and newweight.
 * @author night
 * @version 1.0.0
 * @see EditOutro
 * @see AddIntro
 * @see RemoveIntro
 * @see IntroDetail
 */
public class EditIntro implements SlashCommand {
	private String name = "editintro";
	
	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Edit the volume and weight of an intro")
					.addOption(OptionType.INTEGER, "index", "The intro to edit", true)
					.addOption(OptionType.INTEGER, "newvol", "The new volume of the intro")
					.addOption(OptionType.INTEGER, "newweight", "The new weight of the intro");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		boolean errors = false;
		
		// Get user's intros
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> intros = settings.getIntrosFor(e.getMember().getId());
		
		// Get the specific intro
		int index = (int) e.getOption("index").getAsLong() - 1;
		
		if (index > intros.length() - 1 || index < 0) {
			e.reply(Emojis.ERROR + " Error. Invalid index specified.").setEphemeral(true).queue();
			return;
		}
		
		// Make sure it's not an admin only intro
		IntroDetail intro = intros.getByTrueIndex(index);
		if (intro.isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply(Emojis.ERROR + " You must be an administrator to edit that intro!").setEphemeral(true).queue();
			return;
		}
		
		// Update intro data
		int weight = intros.getWeightOf(index);
		int volume = intro.getVolume();
		
		if (e.getOption("newvol") != null) {
			try {
				volume = (int) e.getOption("newvol").getAsLong();
			} catch (Exception ex) {
				errors = true;
			}
		}
		
		if (e.getOption("newweight") != null) {
			try {
				weight = (int) e.getOption("newweight").getAsLong();
			} catch (Exception ex) {
				errors = true;
			}
		}
		
		// Remove the old intro and addd the updated one
		intros.remove(index);
		IntroDetail newIntro = new IntroDetail(intro.getPath(), volume, intro.isAdminOnly(), intro.getExtraActions());
		
		intros.add(index, newIntro, weight);
		
		settings.setIntrosFor(e.getMember().getId(), intros);
		
		// Report to user
		if (errors)
			e.reply(Emojis.WARN + " Edited intro, but some errors have occurred.").setEphemeral(true).queue();
		else
			e.reply(Emojis.SUCCESS + " Successfully edited intro.").setEphemeral(true).queue();
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}

}
