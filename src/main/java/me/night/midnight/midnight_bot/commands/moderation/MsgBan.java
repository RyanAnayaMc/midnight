package me.night.midnight.midnight_bot.commands.moderation;

import org.quartz.SchedulerException;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class MsgBan implements SlashCommand {

	@Override
	public void run(SlashCommandEvent e) {
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			e.reply("❌ You must be a moderator to run this command!").queue();
			return;
		}
		
		Role r = GuildSettingsHandler.getSettingsFor(e.getGuild()).getTextBanRole();
		
		try {
			BanCommand.ban(e, r, BanType.TEXT_BAN);
			e.reply("✅ Successfully added a message ban to " + e.getOption("user").getAsMember().getAsMention() + " for " + e.getOption("hours").getAsString() +
					" hours and "+ e.getOption("minutes").getAsString() + " minutes.")
				.setEphemeral(true)
				.queue();
			e.getOption("user").getAsUser().openPrivateChannel()
				.queue(ch -> {
					EmbedBuilder eb = new EmbedBuilder()
						.setTitle("✉ Message Ban Received")
						.setAuthor("Moderator: " + e.getMember().getEffectiveName())
						.addField("Duration", String.format("%d:%02d", e.getOption("hours").getAsLong(), e.getOption("minutes").getAsLong()), false);
					if (e.getOption("reason") != null)
						eb.addField("Reason", e.getOption("reason").getAsString(), false);
					
					ch.sendMessageEmbeds(eb.build()).queue();
					ch.close().queue();
				});
		} catch (SchedulerException e1) {
			e.reply("⚠ Successfully added a message ban to " + e.getOption("user").getAsMember().getAsMention() + ", but scheduling the unban failed." +
					" You will need to manually unban this user.")
				.setEphemeral(true)
				.queue();
			e.getOption("user").getAsUser().openPrivateChannel()
				.queue(ch -> {
					EmbedBuilder eb = new EmbedBuilder()
						.setTitle("✉ Message Ban Received")
						.setAuthor("Moderator: " + e.getMember().getEffectiveName())
						.addField("Duration", e.getOption("hours").getAsString() + ":" + e.getOption("minutes").getAsString(), false)
						.addField("Note", "⚠ Error scheduling unban! Ask a moderator or admin to manually remove the ban.", false);
					if (e.getOption("reason") != null)
						eb.addField("Reason", e.getOption("reason").getAsString(), false);
					
					ch.sendMessageEmbeds(eb.build()).queue();
					ch.close().queue();
				});
		}
	}

}
