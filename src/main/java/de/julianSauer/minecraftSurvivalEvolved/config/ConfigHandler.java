package de.julianSauer.minecraftSurvivalEvolved.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
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
        Map<String, Object> temp = getAllValuesFromConfig("Food.yml");
        Map<String, Integer> ret = new HashMap<>();
        for (Map.Entry<String, Object> entry : temp.entrySet()) {
            try {
                ret.put(entry.getKey(), (Integer) entry.getValue());
            } catch (ClassCastException e) {
                noValueFoundFor("Food.yml");
            }
        }
        return getAllValuesFromConfig("Food.yml");
    }

    public Map<Material, Integer> getMineableBlocksFor(String entity) {
        return getMapFromSection(entity, "MineableBlocks");
    }

    public Map<Material, Integer> getPreferredFoodFor(String entity) {
        return getMapFromSection(entity, "PreferredFood");
    }

    private Map<Material, Integer> getMapFromSection(String entity, String sectionName) {
        Map<Material, Integer> returnMap = new HashMap<>();
        ConfigurationSection configurationSection = getConfigurationSectionFromConfig(entity + ".yml", defaultEntity, sectionName);
        for (String materialName : configurationSection.getValues(false).keySet()) {
            Material material = Material.getMaterial(materialName);
            if (material != null) {
                try {
                    returnMap.put(material, (Integer) configurationSection.get(materialName));
                } catch (ClassCastException e) {
                    noValueFoundFor(entity + ".yml", defaultEntity, sectionName);
                }
            }
        }
        return returnMap;
    }

}
