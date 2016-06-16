package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class BasicInventoryListener extends BasicListener {

    /**
     * Checks if a player interacts with an inventory while a menu from InventoryGUI is open.
     *
     * @param e Interaction event
     * @return True if the player has a custom GUI open
     */
    protected boolean playerInteractsWithCustomGUI(InventoryInteractEvent e) {
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
    protected boolean playerClickedOnButtonMenu(InventoryClickEvent e) {
        return e.getClickedInventory() != null
                && e.getClickedInventory().getHolder() instanceof MSEEntity
                && e.getWhoClicked() instanceof Player;
    }

    /**
     * Compares type and name of button items.
     *
     * @param items1 First button list
     * @param items2 Second button list
     * @return True if type and name are equal
     */
    protected boolean buttonsAreValid(ItemStack[] items1, ItemStack[] items2) {
        if (items1 == null || items2 == null)
            return false;
        for (int i = 0; i < items1.length; i++)
            if (items1[i] == null
                    || items2[i] == null
                    || !items1[i].getItemMeta().getDisplayName().equals(items2[i].getItemMeta().getDisplayName())
                    || items1[i].getType() != items2[i].getType())
                return false;
        return true;
    }

    /**
     * Compares the names of two buttons.
     *
     * @param item1 First button item
     * @param item2 Second button item
     * @return
     */
    protected boolean buttonNamesAreEqual(ItemStack item1, ItemStack item2) {
        return item1 != null
                && item2 != null
                && item1.hasItemMeta()
                && item2.hasItemMeta()
                && item1.getItemMeta().hasDisplayName()
                && item2.getItemMeta().hasDisplayName()
                && item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());
    }

}
