package me.night.midnight.midnight_bot.commands.moderation;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class ViewLog implements SlashCommand {
	private String name = "viewlog";
	
	@Override
	public void run(SlashCommandEvent e) {
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

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "View edited and deleted messages.");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		if (!m.hasPermission(Permission.MESSAGE_MANAGE)) {
			reply.setContent("❌ You must be a moderator to run this command!").queue();
			return false;
		}
		return true;
	}

}
