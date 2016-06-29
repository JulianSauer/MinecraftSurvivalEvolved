package de.julianSauer.minecraftSurvivalEvolved.utils;

import java.util.Random;

public class Calculator {

    private static Random random;

    private Calculator() {
    }

    /**
     * Uses a random value to determine if a chance is a hit or miss.
     *
     * @param chance Value in percent
     * @return True if it's a hit
     */
    public static boolean applyProbability(int chance) {
        return getRandomInt(101) <= chance;
    }

    /**
     * Creates a random int with 0 <= x < upper limit.
     *
     * @param upperLimit Limits the generated number
     * @return Random value
     */
    public static int getRandomInt(int upperLimit) {
        if (random == null)
            random = new Random();
        return random.nextInt(upperLimit);
    }

    /**
     * Calculates the value for an attribute with a custom multiplier.
     *
     * @param baseValue  Normal value of the entity
     * @param level      Level of the entity
     * @param multiplier Level multiplier of the entity
     * @return Multiplier dependet value of the entity
     */
    public static double calculateLevelDependentStatFor(double baseValue, int level, float multiplier) {
        return (1 + (level - 1) * multiplier) * baseValue;
    }

}
