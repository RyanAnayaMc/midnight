package me.night.midnight.midnight_bot.audio;

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

public class EditOutro implements SlashCommand {
	private String name = "editoutro";
	
	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Edit the volume and weight of an outro")
					.addOption(OptionType.INTEGER, "index", "The outro to edit", true)
					.addOption(OptionType.INTEGER, "newvol", "The new volume of the outro")
					.addOption(OptionType.INTEGER, "newweight", "The new weight of the outro");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		boolean errors = false;
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> intros = settings.getOutrosFor(e.getMember().getId());
		
		int index = (int) e.getOption("index").getAsLong() - 1;
		
		if (index > intros.length() - 1 || index < 0) {
			e.reply("❌ Error. Invalid index specified.").setEphemeral(true).queue();
			return;
		}
		
		IntroDetail intro = intros.getByTrueIndex(index);
		if (intro.isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply("❌ You must be an administrator to edit that intro!").setEphemeral(true).queue();
			return;
		}
		
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
		
		intros.remove(index);
		IntroDetail newIntro = new IntroDetail(intro.getPath(), volume, intro.isAdminOnly(), intro.ignoresTimeLimit(), intro.getExtraActions());
		
		intros.add(index, newIntro, weight);
		
		settings.setOutrosFor(e.getMember().getId(), intros);
		
		if (errors)
			e.reply("⚠ Successfully edited intro, but some errors have occurred.").setEphemeral(true).queue();
		else
			e.reply("✅ Successfully edited intro.").setEphemeral(true).queue();
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
}
