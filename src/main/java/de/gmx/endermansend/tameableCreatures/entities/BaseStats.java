package de.gmx.endermansend.tameableCreatures.entities;

import de.gmx.endermansend.tameableCreatures.config.ConfigHandler;
import de.gmx.endermansend.tameableCreatures.main.TameableCreatures;
import org.bukkit.Material;

import java.util.List;

/**
 * Loads base stats of entities from config.
 */
public class BaseStats {

    private boolean tameable;

    private int fortitude;
    private int levelCap;
    private int maxTamingProgress;
    private int maxTorpidity;
    private int xpUntilLevelUp;

    private float levelMultiplier;

    private double damage;

    private List<Material> preferredFood;
    private List<Material> mineableBlocks;

    public BaseStats(String entity) {

        ConfigHandler config = TameableCreatures.getConfigHandler();

        tameable = config.get.tameableFor(entity);
        fortitude = config.get.fortitudeFor(entity);
        levelCap = config.get.levelCapFor(entity);
        maxTamingProgress = config.get.maxTamingProgressFor(entity);
        maxTorpidity = config.get.maxToripidityFor(entity);
        xpUntilLevelUp = config.get.xpUntilLevelUpFor(entity);
        levelMultiplier = config.get.levelMultiplierFor(entity);
        damage = config.get.damageFor(entity);
        preferredFood = config.get.preferredFoodFor(entity);
        mineableBlocks = config.get.mineableBlocksFor(entity);

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
