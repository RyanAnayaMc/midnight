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
		WeightedList<IntroDetail> outros = settings.getOutrosFor(e.getMember().getId());
		
		int outro = (int) (e.getOption("index").getAsLong() - 1);
		
		if (outro < 0 || outro >= outros.length()) {
			e.reply("❌ That outro does not exist. Check your outros with `/listintros`").setEphemeral(true).queue();
			return;
		}
		
		if (outros.getByTrueIndex(outro).isAdminOnly() && !e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply("❌ You must be an administrator to remove that outro!").setEphemeral(true).queue();
			return;
		}
		
		new File(outros.getByTrueIndex(outro).getPath()).delete();
		
		outros.remove(outro);
		
		e.reply("✅ Successfully removed outro.").setEphemeral(true).queue();
		
		settings.setOutrosFor(e.getMember().getId(), outros);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		return true;
	}
}
