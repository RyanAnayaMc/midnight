package me.night.midnight.midnight_bot.audio;

public class EmptyIntroException extends RuntimeException {
	private static final long serialVersionUID = 1444592210643537921L;

	public EmptyIntroException() {
		super("Cannot call method on empty intro!");
	}
	
	public EmptyIntroException(String msg) {
		super(msg);
	}
}
