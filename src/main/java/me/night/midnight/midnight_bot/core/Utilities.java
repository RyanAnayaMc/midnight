package me.night.midnight.midnight_bot.core;

import java.util.List;
import java.util.Random;

public class Utilities {
    private static Random random = new Random();

    /**
     * Returns a random element from a list.
     * @param list The list to get an element from.
     * @param <T> The type of the given list.
     * @return A random element from the list.
     */
    public static <T> T getRandomFrom(List<T> list) {
        int size = list.size();
        int index = random.nextInt(size);
        return list.get(index);
    }

    /**
     * Returns a random element from an array.
     * @param array The array to get an element from.
     * @param <T> The type of the given array.
     * @return A random element from the array.
     */
    public static <T> T getRandomFrom(T[] array) {
        int size = array.length;
        int index = random.nextInt(size);

        return array[index];
    }
}
