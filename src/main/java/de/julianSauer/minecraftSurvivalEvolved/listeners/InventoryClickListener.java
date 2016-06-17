package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.visuals.inventories.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Handles menu navigation and prevents unwanted interactions with custom GUIs
 */
public class InventoryClickListener implements BasicInventoryListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {

        if (playerClickedOnButtonMenu(e)) {

            Inventory inventory = e.getClickedInventory();
            Player player = (Player) e.getWhoClicked();
            MSEEntity mseEntity = (MSEEntity) inventory.getHolder();

            if (inventory.getName().equals("Entity Options")) {
                e.setCancelled(
                        InventoryGUI.optionsMenuButtonClicked(e.getSlot(), player, mseEntity));

            } else if (inventory.getName().equals(mseEntity.getEntityStats().getEntityType())) {
                e.setCancelled(
                        InventoryGUI.mainMenuButtonClicked(e.getSlot(), player, mseEntity));

            } else if (inventory.getName().contains(" Inventory")) {
                e.setCancelled(
                        InventoryGUI.inventoryMenuButtonClicked(e.getSlot(), player, mseEntity));
            }
        }

        // Cancel events for GUIs
        if (playerInteractsWithCustomGUI(e)) {
            if (e.isShiftClick()
                    || e.getClick() == ClickType.DOUBLE_CLICK
                    || e.getClick() == ClickType.NUMBER_KEY)
                e.setCancelled(true);
            else if ((e.isRightClick()
                    || e.getClick() == ClickType.DROP
                    || e.getClick() == ClickType.CONTROL_DROP

            ) && e.getClickedInventory().getHolder() instanceof MSEEntity)
                e.setCancelled(true);
        }

    }

}
