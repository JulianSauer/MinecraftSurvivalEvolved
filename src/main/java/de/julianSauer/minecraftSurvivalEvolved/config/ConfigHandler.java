package de.julianSauer.minecraftSurvivalEvolved.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements getter and setter methods for config files.
 */
public class ConfigHandler extends ConfigHandlerBase {

    private String defaultEntity = "DefaultEntity.yml";

    public ConfigHandler() {
        super(new String[]{
                "CaveSpider.yml",
                "DefaultEntity.yml",
                "Food.yml",
                "Giant.yml",
                "Spider.yml",
                "Squid.yml",
                "Wolf.yml"
        });
    }

    public int getLevelCapFor(String entity) {
        int levelCap = getIntFromConfig(entity + ".yml", defaultEntity, "LevelCap");
        if (levelCap <= 0)
            throw new NumberFormatException("Level cap has to be higher than " + levelCap);
        return levelCap;
    }

    public float getLevelMultiplierFor(String entity) {
        return (float) getDoubleFromConfig(entity + ".yml", defaultEntity, "LevelMultiplier");
    }

    public boolean getTameableFor(String entity) {
        return getBooleanFromConfig(entity + ".yml", defaultEntity, "Tameable");
    }

    public int getAlphaProbabilityFor(String entity) {
        return getIntFromConfig(entity + ".yml", defaultEntity, "AlphaProbability");
    }

    public double getDamageFor(String entity) {
        return getDoubleFromConfig(entity + ".yml", defaultEntity, "Damage");
    }

    public float getSpeedFor(String entity) {
        return (float) getDoubleFromConfig(entity + ".yml", defaultEntity, "Speed");
    }

    public int getMaxToripidityFor(String entity) {
        return getIntFromConfig(entity + ".yml", defaultEntity, "MaxTorpidity");
    }

    public int getFortitudeFor(String entity) {
        return getIntFromConfig(entity + ".yml", defaultEntity, "Fortitude");
    }

    public int getMaxTamingProgressFor(String entity) {
        return getIntFromConfig(entity + ".yml", defaultEntity, "MaxTamingProgress");
    }

    public int getXpUntilLevelUpFor(String entity) {
        return getIntFromConfig(entity + ".yml", defaultEntity, "XpUntilLevelUp");
    }

    public int getMaxFoodFor(String entity) {
        return getIntFromConfig(entity + ".yml", defaultEntity, "MaxFood");
    }

    public Map<String, Integer> getFoodSaturations() {
        return getAllValuesFromConfig("Food.yml");
    }

    public List<Material> getMineableBlocksFor(String entity) {
        ArrayList blocks = new ArrayList();
        List<String> materialStrings = getStringListFromConfig(entity + ".yml", defaultEntity, "MineableBlocks");
        if (materialStrings == null)
            throw new IllegalStateException("No value found in " + entity + ".yml for MineableBlocks");
        for (String materialString : materialStrings) {
            Material tempMaterial = Material.getMaterial(materialString);
            if (tempMaterial != null)
                blocks.add(tempMaterial);
        }
        return blocks;
    }

    public Map<Material, Integer> getPreferredFoodFor(String entity) {
        Map<Material, Integer> preferredFood = new HashMap<>();
        ConfigurationSection foodList = getConfigurationSectionFromConfig(entity + ".yml", defaultEntity, "PreferredFood");
        if (foodList == null)
            throw new IllegalStateException("No value found in " + entity + ".yml for PreferredFood");
        for (String food : foodList.getValues(false).keySet()) {
            Material tempMaterial = Material.getMaterial(food);
            if (tempMaterial != null)
                preferredFood.put(tempMaterial, (Integer) foodList.get(food));
        }
        return preferredFood;
    }


}
