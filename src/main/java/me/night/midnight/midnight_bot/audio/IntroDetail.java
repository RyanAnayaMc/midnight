package me.night.midnight.midnight_bot.audio;

import java.util.Arrays;

public class IntroDetail {
	private String filename;
	private String fileDirectory;
	private int volume;
	private ExtraAction[] extraActions;
	
	public IntroDetail(String dir, String fName, int vol, ExtraAction...actions) {
		filename = fName;
		fileDirectory = dir;
		volume = Math.max(0, vol);
		extraActions = actions;
	}
	
	public String getPath() {
		return fileDirectory + filename;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public ExtraAction[] getExtraActions() {
		return Arrays.copyOf(extraActions, extraActions.length);
	}
	
	protected enum ExtraAction {
		KICK
	}
}
