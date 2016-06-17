package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps player names to entities to mark them for a name change.
 */
public class NameChangeHandler {

    private static Map<String, MSEEntity> waitingForNameChange = new HashMap<>();

    /**
     * Saves player name and entity in a temporary cache to change it's name later.
     *
     * @param initiatingPlayer The player name is used as a key
     * @param mseEntity The entity that will receive a new name
     */
    public static void markEntityForNameChange(String initiatingPlayer, MSEEntity mseEntity) {
        waitingForNameChange.put(initiatingPlayer, mseEntity);
    }

    /**
     * Tries to change the name of an entity from the cache. The name is not changed if the entity wasn't marked for a
     * name change or if the name is empty.
     *
     * @param initiatingPlayer The player name is used as a key
     * @param newName New name for entity
     */
    public static void changeNameOfEntity(String initiatingPlayer, String newName) {
        MSEEntity mseEntity = waitingForNameChange.get(initiatingPlayer);
        if(mseEntity == null || newName == null || newName.equals(""))
            return;

        mseEntity.setCustomName(newName);
        waitingForNameChange.remove(initiatingPlayer);
    }

}
