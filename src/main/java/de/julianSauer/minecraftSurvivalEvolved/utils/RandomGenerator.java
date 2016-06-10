package de.julianSauer.minecraftSurvivalEvolved.utils;

import java.util.Random;

public class RandomGenerator {

    private static Random random;

    private RandomGenerator() {
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

}
