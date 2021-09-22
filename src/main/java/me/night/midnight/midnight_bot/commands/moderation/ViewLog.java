package me.night.midnight.midnight_bot.commands.moderation;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ViewLog implements SlashCommand {
	@Override
	public void run(SlashCommandEvent e) {
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			e.reply("❌ You must be a moderator to run this command!").queue();
			return;
		}
		
		long id = e.getMember().getIdLong();
		
		GuildSettings settings = GuildSettingsHandler.getSettingsFor(e.getGuild());
		
		if (settings.isMsgLogMod(id)) {
			if (settings.removeMsgLogMod(id))
				e.reply("✅ You can no longer view the message logs for " + e.getGuild().getName() + ".")
					.setEphemeral(true)
					.queue();
			else
				e.reply("❌ Something went wrong. Please try again or contact the bot developer.")
					.setEphemeral(true)
					.queue();
		} else {
			if (settings.addMsgLogMod(id))
				e.reply("✅ You can now view message logs for " + e.getGuild().getName() + ".")
					.setEphemeral(true)
					.queue();
			else
				e.reply("❌ Something went wrong. Please try again or contact the bot developer.")
				.setEphemeral(true)
				.queue();
		}
	}

}
