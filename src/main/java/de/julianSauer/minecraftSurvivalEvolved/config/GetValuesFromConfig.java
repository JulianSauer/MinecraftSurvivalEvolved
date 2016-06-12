package de.julianSauer.minecraftSurvivalEvolved.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int maxFoodFor(String entity) {
        return config.getIntFromConfig(entity, "MaxFood");
    }

    public Map<String, Integer> foodSaturations() {
        return config.getAllValuesFromConfig("Food");
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

    public Map<Material, Integer> preferredFoodFor(String entity) {
        Map<Material, Integer> preferredFood = new HashMap<>();
        Map<String, Object> temp = config.getConfigurationSectionFromConfig(entity, "PreferredFood").getValues(false);
        for (String s : temp.keySet()) {
            Material tempMaterial = Material.getMaterial(s);
            if (tempMaterial != null)
                preferredFood.put(tempMaterial, (Integer) temp.get(s));
        }
        return preferredFood;
    }


}
