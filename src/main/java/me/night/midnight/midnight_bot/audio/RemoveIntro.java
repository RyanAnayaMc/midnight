package me.night.midnight.midnight_bot.audio;

import java.io.File;
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

public class RemoveIntro implements SlashCommand {
	private String name = "removeintro";
	
	@Override
	public CommandData getCommandData() {
	return new CommandData(name, "Removes an intro.")
				.addOption(OptionType.INTEGER, "index", "The intro to remove.");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> intros = settings.getIntrosFor(e.getMember().getId());
		
		int intro = (int) (e.getOption("index").getAsLong() - 1);
		
		if (intro < 0 || intro >= intros.length()) {
			e.reply("❌ That intro does not exist. Check your intros with `/listintros`").setEphemeral(true).queue();
			return;
		}
		
		if (intros.getByTrueIndex(intro).isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply("❌ You must be an administrator to remove that intro!").setEphemeral(true).queue();
			return;
		}
		
		new File(intros.getByTrueIndex(intro).getPath()).delete();
				
		intros.remove(intro);
		
		e.reply("✅ Successfully removed intro.").setEphemeral(true).queue();
		
		settings.setIntrosFor(e.getMember().getId(), intros);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
	
}
