package me.night.midnight.midnight_bot.core;

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
import me.night.midnight.midnight_bot.commands.moderation.ViewLog;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SlashCommandHandler extends ListenerAdapter {
	private Hashtable<String, SlashCommand> slashCommands;
	private JDA jda;
	
	public SlashCommandHandler(JDA jda) {
		this.jda = jda;
	}
	
	public void getSlashCommands() {
		slashCommands = new Hashtable<String, SlashCommand>();
		List<SlashCommand> cmds = new ArrayList<SlashCommand>();
		EventWaiter waiter = new EventWaiter();
		
		jda.addEventListener(waiter);
		
		cmds.add(new Ping());
		cmds.add(new ViewLog());
		cmds.add(new Migrate());
		cmds.add(new ListIntros());
		cmds.add(new AddIntro(waiter));
		cmds.add(new AddOutro(waiter));
		cmds.add(new RemoveIntro());
		cmds.add(new RemoveOutro());
		cmds.add(new EditIntro());
		cmds.add(new EditOutro());
		
		List<CommandData> cmdData = new ArrayList<CommandData>();
		
		for (SlashCommand cmd : cmds) {
			cmdData.add(cmd.getCommandData());
			slashCommands.put(cmd.getName(), cmd);
		}
		
		jda.updateCommands().queue();
		
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
