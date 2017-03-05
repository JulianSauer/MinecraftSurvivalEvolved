package de.julianSauer.minecraftSurvivalEvolved.config;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Rank;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implements getter and setter methods for config files.
 */
public class ConfigHandler extends ConfigHandlerBase {

    private final String defaultEntity = "DefaultEntity.yml";

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
        return ret;
    }

    public Map<UUID, Tribe> getTribes() {

        File tribeFolder = new File(MSEMain.getInstance().getDataFolder() + "/Tribes");
        File[] tribeFiles = tribeFolder.listFiles();
        Map<UUID, Tribe> tribes = new HashMap<>();

        if (tribeFiles == null)
            return new HashMap<>();

        for (File tribeFile : tribeFiles) {
            String configName = tribeFile.getName();
            addConfigToCache("/Tribes/", configName);
            Tribe tribe = new Tribe(configName.substring(0, configName.length() - 4), false);

            for (String path : getConfigurationSectionFromConfig(configName, "").getKeys(false)) {

                String playerUUIDString = getStringFromConfig(configName, path + ".ID");
                UUID playerUUID = UUID.fromString(playerUUIDString);
                Rank rank = Rank.valueOf(getStringFromConfig(configName, path + ".Rank").toUpperCase());
                if (!playerUUIDString.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
                    MSEMain.getInstance().getLogger().warning("Player " + path + " (" + rank + ") in " + configName + " has a corrupt ID and will be ignored");
                    continue;
                }

                tribe.loadMember(playerUUID, rank);
            }

            tribes.put(tribe.getUniqueID(), tribe);
            removeConfigFromCache(configName);
        }
        return tribes;

    }

    /**
     * Compares the given tribes with the ones found on the disk and adjusts those.
     *
     * @param tribes Cached tribes that are going to be written to disk
     */
    public void setTribes(Map<UUID, Tribe> tribes) {

        Collection<Tribe> markedForRemoval = getTribes().values();
        markedForRemoval.removeAll(tribes.values());
        for (Tribe tribe : markedForRemoval)
            deleteFile("/Tribes/", tribe.getName() + ".yml");

        for (Tribe tribe : tribes.values()) {
            addConfigToCache("/Tribes/", tribe.getName() + ".yml");
            for (UUID playerUUID : tribe.getMemberUUIDs()) {
                String playerName = Bukkit.getOfflinePlayer(playerUUID).getName();
                setValueInConfig(tribe.getName() + ".yml", playerName + ".ID", playerUUID.toString());
                setValueInConfig(tribe.getName() + ".yml", playerName + ".Rank", tribe.getRankOfMember(playerUUID).toString());
            }
        }

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
