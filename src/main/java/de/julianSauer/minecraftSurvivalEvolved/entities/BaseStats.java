package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.config.ConfigHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

/**
 * Loads base stats of entities from config.
 */
public class BaseStats {

    private static HashMap<String, BaseStats> cache;

    private boolean tameable;

    private int fortitude;
    private int levelCap;
    private int maxTamingProgress;
    private int maxTorpidity;
    private int xpUntilLevelUp;

    private float levelMultiplier;
    private int alphaProbability;
    private float speed;

    private double damage;

    private List<Material> preferredFood;
    private List<Material> mineableBlocks;

    private BaseStats(String entity) {

        ConfigHandler config = ThisPlugin.getConfigHandler();

        // Prevent spaces in .yml files
        entity = entity.replace(" ", "");

        tameable = config.get.tameableFor(entity);
        fortitude = config.get.fortitudeFor(entity);
        levelCap = config.get.levelCapFor(entity);
        maxTamingProgress = config.get.maxTamingProgressFor(entity);
        maxTorpidity = config.get.maxToripidityFor(entity);
        xpUntilLevelUp = config.get.xpUntilLevelUpFor(entity);
        levelMultiplier = config.get.levelMultiplierFor(entity);
        alphaProbability = config.get.alphaProbabilityFor(entity);
        damage = config.get.damageFor(entity);
        speed = config.get.speedFor(entity);
        preferredFood = config.get.preferredFoodFor(entity);
        mineableBlocks = config.get.mineableBlocksFor(entity);

        if (levelCap <= 0)
            throw new NumberFormatException("Level cap has to be higher than " + levelCap);

    }

    /**
     * Returns an object containing basic attributes of an entity from disk/cache.
     *
     * @param entity Name of the entity
     * @return BaseStats for this entity
     */
    public static BaseStats getBaseAttributesFor(String entity) {

        if (cache == null)
            cache = new HashMap<String, BaseStats>();
        BaseStats ret = cache.get(entity);
        if (ret == null) {
            ret = new BaseStats(entity);
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

    public List<Material> getPreferredFood() {
        return preferredFood;
    }

    public List<Material> getMineableBlocks() {
        return mineableBlocks;
    }
}
