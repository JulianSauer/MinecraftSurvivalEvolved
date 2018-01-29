package de.juliansauer.minecraftsurvivalevolved.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * Prevents unwanted interaction with custom GUIs
 */
public class InventoryDragListener implements BasicInventoryListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent e) {

        if (playerInteractsWithCustomGUI(e)) {
            int size = e.getInventory().getSize();
            for (int i : e.getRawSlots()) {
                if (i < size) {
                    e.setCancelled(true);
                    break;
                }
            }
        }

    }

}
