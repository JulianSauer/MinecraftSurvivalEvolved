package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;

/**
 * Saves the tribes in .yml files when the world is saved.
 */
public class WorldSaveListener implements BasicEventListener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        TribeRegistry.getTribeRegistry().saveTribes();
    }

}
