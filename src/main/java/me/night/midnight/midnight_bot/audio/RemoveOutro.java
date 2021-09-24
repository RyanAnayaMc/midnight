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

public class RemoveOutro implements SlashCommand {
	private String name = "removeoutro";
	
	@Override
	public CommandData getCommandData() {
	return new CommandData(name, "Removes an outro.")
				.addOption(OptionType.INTEGER, "index", "The outro to remove.");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run(SlashCommandEvent e) {
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		WeightedList<IntroDetail> intros = settings.getOutrosFor(e.getMember().getId());
		
		int intro = (int) (e.getOption("index").getAsLong() - 1);
		
		if (intro < 1 || intro >= intros.length()) {
			e.reply("❌ That outro does not exist. Check your outros with `/listintros`").setEphemeral(true).queue();
			return;
		}
		
		if (intros.getByTrueIndex(intro).isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply("❌ You must be an administrator to remove that outro!").setEphemeral(true).queue();
			return;
		}
		
		intros.remove(intro);
		
		e.reply("✅ Successfully removed outro.").setEphemeral(true).queue();
		
		settings.setOutrosFor(e.getMember().getId(), intros);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
}
