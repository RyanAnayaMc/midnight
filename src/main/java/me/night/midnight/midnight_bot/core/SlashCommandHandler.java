package me.night.midnight.midnight_bot.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import me.night.midnight.midnight_bot.audio.ListIntros;
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
	
	public SlashCommandHandler(JDA jda) {
		this.jda = jda;
	}
	
	public void getSlashCommands() {
		slashCommands = new Hashtable<String, SlashCommand>();
		List<SlashCommand> cmds = new ArrayList<SlashCommand>();
		
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
		
		for (SlashCommand cmd : cmds) {
			for (Guild g : jda.getGuilds())
				g.updateCommands().addCommands(cmd.getCommandData()).queue();
			slashCommands.put(cmd.getName(), cmd);
		}
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
