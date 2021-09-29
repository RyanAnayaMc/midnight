package me.night.midnight.midnight_bot.commands.moderation;

import me.night.midnight.midnight_bot.core.Emojis;

// SlashCommand to set the Message Ban role on the server
// Requires administrator

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettings;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class SetMsgBan implements SlashCommand {
	private String name = "setmsgban";
	
	@Override
	public void run(SlashCommandEvent e) {
		GuildSettings gs = GuildSettingsHandler.getSettingsFor(e.getGuild());
		
		Role r = e.getOption("role").getAsRole();
		
		gs.setTextBanRole(r);
		e.reply(Emojis.SUCCESS + " Successfully set the message ban role for this server!").queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("setmsgban", "Sets the role used by /msgban")
				.addOption(OptionType.ROLE, "role", "The message ban role.", true);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		if (!m.hasPermission(Permission.ADMINISTRATOR)) {
			reply.setContent(Emojis.ERROR + " You must be an administrator to run this command!")
				.setEphemeral(true)
				.queue();
			
			return false;
		}
		
		return true;
	}

	@Override
	public String getName() {
		return name;
	}
}
