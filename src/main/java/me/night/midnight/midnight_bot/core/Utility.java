package me.night.midnight.midnight_bot.core;

import net.dv8tion.jda.api.entities.Member;

public class Utility {
	public static <T extends Comparable<T>> T clamp(T min, T val, T max) {
		if (val.compareTo(min) < 0)
			return min;
		else if (val.compareTo(max) > 0)
			return max;
		else
			return val;
	}
	
	/**
	 * Determines if one user has permissions over another.
	 * @param m1 The member to check permissions for.
	 * @param m2 The member to compare m1 to.
	 * @return Whether or not m1 has higher roles than m2.
	 */
	public static boolean hasPermissionOver(Member m1, Member m2) {
		return m1.getRoles().get(0).getPosition() > m2.getRoles().get(0).getPosition();
	}
}
