package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;

/**
 * Saves the tribes in .yml files when the world is saved.
 */
public class WorldSaveListener implements BasicEventListener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        TribeRegistry.getTribeRegistry().saveTribes();
        MSEPlayerMap.getPlayerRegistry().savePlayers();
    }

}
