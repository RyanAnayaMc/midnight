package me.night.midnight.midnight_bot.commands.moderation;

import org.quartz.SchedulerException;

import me.night.midnight.midnight_bot.core.SlashCommand;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class MsgBan implements SlashCommand {
	private String name = "msgban";
	
	@Override
	public void run(SlashCommandEvent e) {
		Role r = GuildSettingsHandler.getSettingsFor(e.getGuild()).getTextBanRole();
		
		if (r == null) {
			e.reply("❌ The message ban role has not been set on this server! Ask an admin to set it with `/setmsgban`!")
				.setEphemeral(true)
				.queue();
			return;
		}
		
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
			e.reply("⚠ Successfully added a message ban to " + e.getOption("user").getAsMember().getAsMention() + ", but scheduling the unban failed." +
					" You will need to manually unban this user.")
				.setEphemeral(true)
				.queue();
			e.getOption("user").getAsUser().openPrivateChannel()
				.queue(ch -> {
					EmbedBuilder eb = new EmbedBuilder()
						.setTitle("✉ Message Ban Received")
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

	@Override
	public CommandData getCommandData() {
		return new CommandData(name, "Message bans a user for a set amount of time.")
				.addOption(OptionType.USER, "user", "The user to apply the ban to.", true)
				.addOption(OptionType.INTEGER, "hours", "The duration in hours to ban the user.", true)
				.addOption(OptionType.INTEGER, "minutes", "The duration in minutes to ban the user.", true)
				.addOption(OptionType.STRING, "reason", "Why the user is receiving this ban.", false);
	}

	@Override
	public boolean checkPermissions(Member m, ReplyAction reply) {
		if (!m.hasPermission(Permission.MESSAGE_MANAGE)) {
			reply.setContent("❌ You must be a moderator to run this command!").setEphemeral(true).queue();
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

}
