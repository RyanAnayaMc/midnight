package me.night.midnight.midnight_bot.commands.moderation;

import org.quartz.SchedulerException;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ImgBan implements SlashCommand {

	@Override
	public void run(SlashCommandEvent e) {
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			e.reply("❌ You must be a moderator to run this command!").queue();
			return;
		}
		
		Role r = GuildSettingsHandler.getSettingsFor(e.getGuild()).getImageBanRole();
		
		try {
			BanCommand.ban(e, r, BanType.IMG_BAN);
			e.reply("✅ Successfully added a image ban to " + e.getOption("user").getAsMember().getAsMention() + " for " + e.getOption("hours").getAsString() +
					" hours and "+ e.getOption("minutes").getAsString() + " minutes.").queue();
		} catch (SchedulerException e1) {
			e.reply("⚠ Successfully added a image ban to " + e.getOption("user").getAsMember().getAsMention() + ", but scheduling the unban failed." +
					" You will need to manually unban this user.").queue();
		}
	}

}
