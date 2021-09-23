package me.night.midnight.midnight_bot.commands.moderation;

import org.quartz.SchedulerException;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class VcBan implements SlashCommand {

	@Override
	public void run(SlashCommandEvent e) {
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			e.reply("❌ You must be a moderator to run this command!").queue();
			return;
		}
		
		Role r = GuildSettingsHandler.getSettingsFor(e.getGuild()).getVoiceBanRole();
		if (r == null) {
			e.reply("❌ The voice ban role has not been set on this server! Ask an admin to set it with `/setvcban`!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
		try {
			BanCommand.ban(e, r, BanType.VOICE_BAN);
			e.reply("✅ Successfully added a voice ban to " + e.getOption("user").getAsMember().getAsMention() + " for " + e.getOption("hours").getAsString() +
					" hours and "+ e.getOption("minutes").getAsString() + " minutes.").queue();
			e.getOption("user").getAsUser().openPrivateChannel()
				.queue(ch -> {
					EmbedBuilder eb = new EmbedBuilder()
						.setTitle("🔇Voice Ban Received")
						.setThumbnail(e.getGuild().getIconUrl())
						.addField("Server", e.getGuild().getName(), false)
						.setAuthor("Moderator: " + e.getMember().getEffectiveName(), e.getMember().getUser().getAvatarUrl(), e.getMember().getUser().getAvatarUrl())
						.addField("Duration", String.format("%d:%02d", e.getOption("hours").getAsLong(), e.getOption("minutes").getAsLong()), false);
					if (e.getOption("reason") != null)
						eb.addField("Reason", e.getOption("reason").getAsString(), false);
					
					ch.sendMessageEmbeds(eb.build()).queue();
					ch.close().queue();
				});
		} catch (SchedulerException e1) {
			e.reply("⚠ Successfully added a voice ban to " + e.getOption("user").getAsMember().getAsMention() + ", but scheduling the unban failed." +
					" You will need to manually unban this user.").queue();
			e.getOption("user").getAsUser().openPrivateChannel()
				.queue(ch -> {
					EmbedBuilder eb = new EmbedBuilder()
						.setTitle("🔇 Voice Ban Received")
						.setThumbnail(e.getGuild().getIconUrl())
						.addField("Server", e.getGuild().getName(), false)
						.setAuthor("Moderator: " + e.getMember().getEffectiveName(), e.getMember().getUser().getAvatarUrl(), e.getMember().getUser().getAvatarUrl())
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
