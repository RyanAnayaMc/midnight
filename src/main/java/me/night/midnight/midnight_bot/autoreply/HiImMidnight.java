package me.night.midnight.midnight_bot.autoreply;

import java.util.Random;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HiImMidnight extends ListenerAdapter {
	private String[] reactionEmotesNegative = new String[] {
			"<:RaidenShotgun:876383294235746364>",
			"<:AN94cry:700521060684136528>",
			"<:9a91wtf:746431486592286810>",
			"<:AmiyaWtf:672260806179684352>",
			"<a:MiraFuckYouGun:858897059317547098>",
			"<:KeqingGun:850945544817868860>"
	};
	
	private String[] reactionEmotesPositive = new String[] {
			"<a:RaidenLove:892843720779661362>",
			"<:KeqingHeart:800528417090961409>",
			"<:9a91yandere:727567099286782052>",
			"<:shine:821112518219005993>",
			"<:ak12stare:750590611077201940>",
			"<:MeiStare:760218472096858143>"
	};
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		String msg = e.getMessage().getContentDisplay();
		String msgCopy = msg.toLowerCase();
		int startIndex = 0;
		
		if (msgCopy.startsWith("i'm "))
			startIndex = 4;
		else if (msgCopy.startsWith("im "))
			startIndex = 3;
		else if (msgCopy.startsWith("i am "))
			startIndex = 5;
		else
			return;
		
		msg = msg.substring(startIndex);
		
		if (msg.equalsIgnoreCase("midnight")) {
			e.getMessage().reply("No, I'm Midnight!").queue();
			e.getMessage().reply(getRandom(reactionEmotesNegative)).queue();
		} else {
			e.getMessage().reply("Hi " + msg + ", I'm Midnight!").queue();
			e.getMessage().reply(getRandom(reactionEmotesPositive)).queue();
		}
		
	}
	
	public String getRandom(String[] arr) {
		int index = new Random().nextInt(arr.length);
		return arr[index];
	}
}
