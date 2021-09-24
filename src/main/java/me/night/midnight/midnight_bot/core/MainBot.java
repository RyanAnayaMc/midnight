package me.night.midnight.midnight_bot.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import me.night.midnight.midnight_bot.audio.UserIntroHandler;
import me.night.midnight.midnight_bot.core.settings.GuildSettingsHandler;
import me.night.midnight.midnight_bot.moderation.messageinterceptor.MessageInterceptor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 * The code that handles the actual setup and startup of the bot
 * @author night
 * TODO take configuration from json file
 */

public class MainBot {
	private static Scheduler scheduler;
	private static JDA jda;
	
    public static void main( String[] args ) {
    	// Perform initial setup
    	Logger.setupLogger(); // Logger setup
    	
    	// Scheduler setup
    	try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e2) {
			Logger.log("Error initializing scheduler core. Exiting...");
			System.exit(1);
		}
    	
    	// Attempt to log in to bot
    	File tokenFile = new File(BotSettings.TOKEN_PATH_DEFAULT);
    	BotSettings settings = new BotSettings();
    	settings.readSettings();
    	
    	Scanner tokenScanner = null;
		try {
			tokenScanner = new Scanner(tokenFile);
		} catch (FileNotFoundException e1) {
			Logger.log("Could not find token file at " + BotSettings.TOKEN_PATH_DEFAULT +
					"! Exiting...");
			System.exit(1);
		}
    	
    	String loginToken = tokenScanner.nextLine();
    	tokenScanner.close();
    	
    	// Attempt to log in to the bot
        try {
			jda = JDABuilder
					.create(loginToken,
							GatewayIntent.GUILD_MEMBERS,
							GatewayIntent.GUILD_VOICE_STATES,
							GatewayIntent.GUILD_MESSAGES,
							GatewayIntent.GUILD_MESSAGE_REACTIONS,
							GatewayIntent.DIRECT_MESSAGES,
							GatewayIntent.DIRECT_MESSAGE_REACTIONS,
							GatewayIntent.GUILD_PRESENCES,
							GatewayIntent.GUILD_EMOJIS)
					.setMemberCachePolicy(MemberCachePolicy.ALL)
					.addEventListeners(new UserIntroHandler())
					.build();
		} catch (LoginException e) {
			Logger.log("Login failed! Exiting...");
			System.exit(1);
		}
        
        // Configure the bot's presence
        jda.getPresence()
        	.setPresence(
        			OnlineStatus.ONLINE,
        			Activity.watching("hentai and listening to heavy metal")
        		);
        
        try {
			jda.awaitReady();
		} catch (InterruptedException e) {
			Logger.log("Error! Bot startup failed.");
			System.exit(1);
		}
        
        // Configure slash commands
        SlashCommandHandler slashCmds = new SlashCommandHandler(jda);
       	slashCmds.getSlashCommands();
        	
        //jda.upsertCommand(cmd);
        jda.addEventListener(slashCmds);
        jda.addEventListener(new MessageInterceptor());
        
    	GuildSettingsHandler.retrieveSettings(jda);
    }
    
    public static Scheduler getScheduler() {
    	return scheduler;
    }
    
    public static JDA getJDA() {
    	return jda;
    }
}
