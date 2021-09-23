package me.night.midnight.midnight_bot.audio;

import java.util.Arrays;

public class IntroDetail {
	private String path;
	private int volume;
	private ExtraAction[] extraActions;
	private boolean adminOnly;
	
	/**
	 * Creates a new IntroDetail
	 * @param p The path to the audio file
	 * @param vol The volume for the sound
	 * @param admin Whether or not this IntroDetail can only be changed by admins
	 * @param actions Any extra actions that will occur
	 */
	public IntroDetail(String p, int vol, boolean admin, ExtraAction...actions) {
		path = p;
		volume = Math.max(0, vol);
		extraActions = actions;
		adminOnly = admin;
	}
	
	public boolean isAdminOnly() {
		return adminOnly;
	}
	
	public String getPath() {
		return path;
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
