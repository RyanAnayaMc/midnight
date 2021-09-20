package me.night.midnight.midnight_bot.commands.moderation;

public enum BanType {
	TEXT_BAN,
	IMG_BAN,
	VOICE_BAN;
	
	public int toInt() {
		switch (this) {
		case TEXT_BAN:
			return 0;
		case IMG_BAN:
			return 1;
		case VOICE_BAN:
			return 2;
		}
		
		return -1;
	}
}
