package de.julianSauer.minecraftSurvivalEvolved.tribes;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of all tribes.
 */
public class TribeRegistry {

    private static TribeRegistry instance;

    private Map<UUID, Tribe> tribes;
    private Map<String, UUID> tribeNames;

    private TribeRegistry() {
        tribes = new HashMap<>();
        tribeNames = new HashMap<>();
    }

    public static TribeRegistry getTribeRegistry() {
        if (instance == null)
            instance = new TribeRegistry();
        return instance;
    }

    /**
     * Loads tribes from all tribe.yml files.
     */
    public void loadTribes() {
        MSEMain plugin = MSEMain.getInstance();
        plugin.getLogger().info("Loading Tribes");
        tribes = plugin.getConfigHandler().getTribes();
        tribes.values().stream().forEach(tribe -> tribeNames.put(tribe.getName(), tribe.getUniqueID()));
        plugin.getLogger().info("Tribes loaded");
    }

    /**
     * Saves tribes to tribe.yml files.
     */
    public void saveTribes() {
        MSEMain plugin = MSEMain.getInstance();
        plugin.getLogger().info("Saving Tribes");
        plugin.getConfigHandler().setTribes(tribes);
        plugin.getLogger().info("Tribes saved");
    }

    public void registerTribe(Tribe tribe) {
        tribes.put(tribe.getUniqueID(), tribe);
        tribeNames.put(tribe.getName(), tribe.getUniqueID());
    }

    public void unregisterTribe(UUID tribeUUID) {
        if (tribes.containsKey(tribeUUID)) {
            String tribeName = tribes.get(tribeUUID).getName();
            if (tribeNames.containsKey(tribeName))
                tribeNames.remove(tribeName);
            unregisterTribe(tribes.get(tribeUUID));
        }
    }

    public void unregisterTribe(Tribe tribe) {
        tribes.remove(tribe.getUniqueID());
    }

    public Collection<Tribe> getTribes() {
        return tribes.values();
    }

    public Tribe getTribe(UUID tribeUUID) {
        return tribes.get(tribeUUID);
    }

    public Tribe getTribe(String tribeName) {
        return getTribe(tribeNames.get(tribeName));
    }

    public boolean tribeExists(String tribeName) {
        return tribeNames.containsKey(tribeName);
    }

}
