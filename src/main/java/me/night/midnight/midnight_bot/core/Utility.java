package me.night.midnight.midnight_bot.core;

public class Utility {
	public static <T extends Comparable<T>> T clamp(T min, T val, T max) {
		if (val.compareTo(min) < 0)
			return min;
		else if (val.compareTo(max) > 0)
			return max;
		else
			return val;
	}
}
