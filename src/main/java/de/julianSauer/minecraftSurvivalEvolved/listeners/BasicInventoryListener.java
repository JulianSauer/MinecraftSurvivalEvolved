package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface BasicInventoryListener extends BasicEventListener {

    /**
     * Checks if a player interacts with an inventory while a menu from InventoryGUI is open.
     *
     * @param e Interaction event
     * @return True if the player has a custom GUI open
     */
    default boolean playerInteractsWithCustomGUI(InventoryInteractEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
        return inventory != null
                && inventoryHolder instanceof MSEEntity
                && e.getWhoClicked() instanceof Player
                && !inventory.getName().contains(" Inventory");
    }

    /**
     * Checks if a player clicked on an inventory containing buttons from InventoryGUI.
     *
     * @param e Interaction event
     * @return True if the player clicked inside the GUI
     */
    default boolean playerClickedOnButtonMenu(InventoryClickEvent e) {
        return e.getClickedInventory() != null
                && e.getClickedInventory().getHolder() instanceof MSEEntity
                && e.getWhoClicked() instanceof Player;
    }

}
