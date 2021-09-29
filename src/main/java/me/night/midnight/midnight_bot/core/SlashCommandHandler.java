package me.night.midnight.midnight_bot.core;

// Class that handles SlashCommands

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.night.midnight.midnight_bot.audio.AddIntro;
import me.night.midnight.midnight_bot.audio.AddOutro;
import me.night.midnight.midnight_bot.audio.EditIntro;
import me.night.midnight.midnight_bot.audio.EditOutro;
import me.night.midnight.midnight_bot.audio.ListIntros;
import me.night.midnight.midnight_bot.audio.RemoveIntro;
import me.night.midnight.midnight_bot.audio.RemoveOutro;
import me.night.midnight.midnight_bot.commands.Migrate;
import me.night.midnight.midnight_bot.commands.Ping;
import me.night.midnight.midnight_bot.commands.moderation.ImgBan;
import me.night.midnight.midnight_bot.commands.moderation.MsgBan;
import me.night.midnight.midnight_bot.commands.moderation.SetImgBan;
import me.night.midnight.midnight_bot.commands.moderation.SetMsgBan;
import me.night.midnight.midnight_bot.commands.moderation.SetVcBan;
import me.night.midnight.midnight_bot.commands.moderation.VcBan;
import me.night.midnight.midnight_bot.commands.moderation.ViewLog;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SlashCommandHandler extends ListenerAdapter {
	private Hashtable<String, SlashCommand> slashCommands;
	private JDA jda;
	
	/**
	 * Creates a new SlashCommandHandler with the given JDA.
	 * Do not make more than one since it will cause issues!
	 * @param jda
	 */
	public SlashCommandHandler(JDA jda) {
		this.jda = jda;
	}
	
	/**
	 * Registers all SlashCommands to the bot
	 */
	public void getSlashCommands() {
		slashCommands = new Hashtable<String, SlashCommand>();
		List<SlashCommand> cmds = new ArrayList<SlashCommand>();
		EventWaiter waiter = new EventWaiter();
		
		// Add an EventWaiter to the bot
		jda.addEventListener(waiter);
		
		// Add all the commands to the list
		cmds.add(new Ping());
		cmds.add(new SetMsgBan());
		cmds.add(new SetImgBan());
		cmds.add(new SetVcBan());
		cmds.add(new MsgBan());
		cmds.add(new ImgBan());
		cmds.add(new VcBan());
		cmds.add(new ViewLog());
		cmds.add(new Migrate());
		cmds.add(new ListIntros());
		cmds.add(new AddIntro(waiter));
		cmds.add(new AddOutro(waiter));
		cmds.add(new RemoveIntro());
		cmds.add(new RemoveOutro());
		cmds.add(new EditIntro());
		cmds.add(new EditOutro());
		
		// Get CommandData from all the SlashCommands
		List<CommandData> cmdData = new ArrayList<CommandData>();
		
		for (SlashCommand cmd : cmds) {
			cmdData.add(cmd.getCommandData());
			slashCommands.put(cmd.getName(), cmd);
		}
		
		// Clear global commands
		jda.updateCommands().queue();
		
		// Add commands to each server
		for (Guild g : jda.getGuilds())
			g.updateCommands().addCommands(cmdData).queue();
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent e) {
		String command = e.getName();
		Logger.log("Received command " + command + " from guild " + e.getGuild().getName());
		
		SlashCommand cmd = slashCommands.get(command);
		if (cmd == null)
			Logger.log("Error! This command does not exist!");
		else
			cmd.execute(e);
	}
}
