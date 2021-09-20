package me.night.midnight.midnight_bot.commands.moderation;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SetVcBan implements SlashCommand {
	@Override
	public void run(SlashCommandEvent e) {
		if (!e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			e.reply("❌ You must be an administrator to run this command!").queue();
			return;
		}
		
		GuildSettings gs = GuildSettingsHandler.getSettingsFor(e.getGuild());
		
		Role r = e.getOption("role").getAsRole();
		
		gs.setVoiceBanRole(r);
		
		e.reply("✅ Successfully set the voice ban role for this server!").queue();
	}
}
