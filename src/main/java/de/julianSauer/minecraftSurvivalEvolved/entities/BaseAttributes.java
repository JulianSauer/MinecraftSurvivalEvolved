package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.config.ConfigHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads a basic set of attributes for entities from config.
 */
public class BaseAttributes {

    private static HashMap<String, BaseAttributes> cache;

    private final boolean tameable;

    private final int fortitude;
    private final int levelCap;
    private final int maxTamingProgress;
    private final int maxTorpidity;
    private final int maxFoodValue;
    private final int xpUntilLevelUp;
    private final int highestFoodSaturation;

    private final float levelMultiplier;
    private final int alphaProbability;
    private final float speed;

    private final double damage;

    private final Map<Material, Integer> preferredFood;
    private final Map<String, Integer> foodSaturations;

    private BaseAttributes(String entity) {

        ConfigHandler config = MSEMain.getInstance().getConfigHandler();

        // Prevent spaces in .yml files
        entity = entity.replace(" ", "");

        tameable = config.getTameableFor(entity);
        fortitude = config.getFortitudeFor(entity);
        levelCap = config.getLevelCapFor(entity);
        maxTamingProgress = config.getMaxTamingProgressFor(entity);
        maxTorpidity = config.getMaxToripidityFor(entity);
        maxFoodValue = config.getMaxFoodFor(entity);
        xpUntilLevelUp = config.getXpUntilLevelUpFor(entity);
        levelMultiplier = config.getLevelMultiplierFor(entity);
        alphaProbability = config.getAlphaProbabilityFor(entity);
        damage = config.getDamageFor(entity);
        speed = config.getSpeedFor(entity);
        preferredFood = config.getPreferredFoodFor(entity);
        foodSaturations = config.getFoodSaturations();
        highestFoodSaturation = Collections.max(foodSaturations.values());

    }

    /**
     * Returns an object containing basic attributes of an entity from disk/cache.
     *
     * @param entity Name of the entity
     * @return Basic Attributes for this entity
     */
    public static BaseAttributes getBaseAttributesFor(String entity) {
        if (cache == null)
            cache = new HashMap();

        BaseAttributes ret = cache.get(entity);
        if (ret == null) {
            ret = new BaseAttributes(entity);
            cache.put(entity, ret);
        }
        return ret;
    }

    public boolean isTameable() {
        return tameable;
    }

    public int getFortitude() {
        return fortitude;
    }

    public int getLevelCap() {
        return levelCap;
    }

    public int getMaxTorpidity() {
        return maxTorpidity;
    }

    public int getMaxFoodValue() {
        return maxFoodValue;
    }

    public int getFoodsaturationFor(String food) {
        if (foodSaturations.containsKey(food))
            return foodSaturations.get(food);
        return 0;
    }

    public int getHighestFoodSaturation() {
        return highestFoodSaturation;
    }

    public int getMaxTamingProgress() {
        return maxTamingProgress;
    }

    public int getXpUntilLevelUp() {
        return xpUntilLevelUp;
    }

    public float getLevelMultiplier() {
        return levelMultiplier;
    }

    public int getAlphaProbability() {
        return alphaProbability;
    }

    public float getSpeed() {
        return speed;
    }

    public double getDamage() {
        return damage;
    }

    public Map<Material, Integer> getPreferredFood() {
        return preferredFood;
    }

}
