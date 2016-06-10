package de.julianSauer.minecraftSurvivalEvolved.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GetValuesFromConfig {

    private ConfigHandler config;

    GetValuesFromConfig(ConfigHandler config) {
        this.config = config;
    }

    public int levelCapFor(String entity) {
        return config.getIntFromConfig(entity, "LevelCap");
    }

    public float levelMultiplierFor(String entity) {
        return (float) config.getDoubleFromConfig(entity, "LevelMultiplier");
    }

    public boolean tameableFor(String entity) {
        return config.getBooleanFromConfig(entity, "Tameable");
    }

    public int alphaProbabilityFor(String entity) {
        return config.getIntFromConfig(entity, "AlphaProbability");
    }

    public double damageFor(String entity) {
        return config.getDoubleFromConfig(entity, "Damage");
    }

    public float speedFor(String entity) {
        return (float) config.getDoubleFromConfig(entity, "Speed");
    }

    public int maxToripidityFor(String entity) {
        return config.getIntFromConfig(entity, "MaxTorpidity");
    }

    public int fortitudeFor(String entity) {
        return config.getIntFromConfig(entity, "Fortitude");
    }

    public int maxTamingProgressFor(String entity) {
        return config.getIntFromConfig(entity, "MaxTamingProgress");
    }

    public int xpUntilLevelUpFor(String entity) {
        return config.getIntFromConfig(entity, "XpUntilLevelUp");
    }

    public List<Material> mineableBlocksFor(String entity) {
        ArrayList blocks = new ArrayList();
        for (String s : config.getStringListFromConfig(entity, "MineableBlocks")) {
            Material tempMaterial = Material.getMaterial(s);
            if (tempMaterial != null)
                blocks.add(tempMaterial);
        }
        return blocks;
    }

    public List<Material> preferredFoodFor(String entity) {
        ArrayList preferredFood = new ArrayList();
        for (String s : config.getStringListFromConfig(entity, "PreferredFood")) {
            Material tempMaterial = Material.getMaterial(s);
            if (tempMaterial != null)
                preferredFood.add(tempMaterial);
        }
        return preferredFood;
    }


}
