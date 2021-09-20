package me.night.midnight.midnight_bot.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import me.night.midnight.midnight_bot.commands.Ping;
import me.night.midnight.midnight_bot.commands.moderation.ImgBan;
import me.night.midnight.midnight_bot.commands.moderation.MsgBan;
import me.night.midnight.midnight_bot.commands.moderation.SetImgBan;
import me.night.midnight.midnight_bot.commands.moderation.SetMsgBan;
import me.night.midnight.midnight_bot.commands.moderation.SetVcBan;
import me.night.midnight.midnight_bot.commands.moderation.VcBan;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SlashCommandHandler extends ListenerAdapter {
	Hashtable<String, SlashCommand> slashCommands;
	
	public List<CommandData> getSlashCommands() {
		slashCommands = new Hashtable<String, SlashCommand>();
		List<CommandData> commands = new ArrayList<CommandData>();
		
		// Ping command
		commands.add(new CommandData("ping", "Pings the bot. Returns response time in ms."));
		slashCommands.put("ping", new Ping());
		
		// Set ban commands
		commands.add(new CommandData("setmsgban", "Sets the role used by /msgban")
				.addOption(OptionType.ROLE, "role", "The message ban role.", true));
		commands.add(new CommandData("setimgban", "Sets the role used by /imgban")
				.addOption(OptionType.ROLE, "role", "The image ban role.", true));
		commands.add(new CommandData("setvcban", "Sets the role used by /vcban")
				.addOption(OptionType.ROLE, "role", "The voice ban role.", true));
		commands.add(new CommandData("msgban", "Message bans a user for a set amount of time.")
				.addOption(OptionType.USER, "user", "The user to apply the ban to.", true)
				.addOption(OptionType.INTEGER, "hours", "The duration in hours to ban the user.", true)
				.addOption(OptionType.INTEGER, "minutes", "The duration in minutes to ban the user.", true));
		commands.add(new CommandData("imgban", "Image bans a user for a set amount of time.")
				.addOption(OptionType.USER, "user", "The user to apply the ban to.", true)
				.addOption(OptionType.INTEGER, "hours", "The duration in hours to ban the user.", true)
				.addOption(OptionType.INTEGER, "minutes", "The duration in minutes to ban the user.", true));
		commands.add(new CommandData("vcban", "Voice bans a user for a set amount of time.")
				.addOption(OptionType.USER, "user", "The user to apply the ban to.", true)
				.addOption(OptionType.INTEGER, "hours", "The duration in hours to ban the user.", true)
				.addOption(OptionType.INTEGER, "minutes", "The duration in minutes to ban the user.", true));
		slashCommands.put("setmsgban", new SetMsgBan());
		slashCommands.put("setimgban", new SetImgBan());
		slashCommands.put("setvcban", new SetVcBan());
		slashCommands.put("msgban", new MsgBan());
		slashCommands.put("imgban", new ImgBan());
		slashCommands.put("vcban", new VcBan());
		
		return commands;
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent e) {
		String command = e.getName();
		Logger.log("Received command " + command + " from guild " + e.getGuild().getName());
		
		SlashCommand cmd = slashCommands.get(command);
		if (cmd == null)
			Logger.log("Error! This command does not exist!");
		else
			cmd.run(e);
	}
}
