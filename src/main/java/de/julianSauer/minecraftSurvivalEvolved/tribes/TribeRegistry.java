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
        tribes.values().forEach(tribe -> tribeNames.put(tribe.getName(), tribe.getUniqueID()));
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

    /**
     * Removes a tribe from the registry. Changes are going to written to disk on server save.
     *
     * @param tribe Tribe that's going to be deleted
     */
    public void unregisterTribe(Tribe tribe) {
        UUID tribeUUID = tribe.getUniqueID();
        if (tribeNames.containsKey(tribe.getName()))
            tribeNames.remove(tribe.getName());
        if (tribes.containsKey(tribeUUID))
            tribes.remove(tribeUUID);
    }

    public Collection<Tribe> getTribes() {
        return tribes.values();
    }

    public Tribe getTribe(UUID tribeUUID) {
        return tribes.get(tribeUUID);
    }

    public Tribe getTribe(String tribeName) {
        for (String name : tribeNames.keySet())
            if (tribeName.equalsIgnoreCase(name))
                return getTribe(tribeNames.get(name));
        return null;
    }

    public boolean tribeExists(String tribeName) {
        for (String name : tribeNames.keySet())
            if (tribeName.equalsIgnoreCase(name))
                return true;
        return false;
    }

}
