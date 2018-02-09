package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.gui.inventories.InventoryGUI;
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

    private InventoryGUI gui;

    public InventoryClickListener() {
        gui = new InventoryGUI();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {

        if (playerIsUnconscious(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if (playerClickedOnButtonMenu(e)) {

            Inventory inventory = e.getClickedInventory();
            Player player = (Player) e.getWhoClicked();

            if (e.getClickedInventory().getHolder() instanceof MSEEntity) {

                MSEEntity mseEntity = (MSEEntity) inventory.getHolder();

                if (inventory.getName().equals("Entity Options")) {
                    e.setCancelled(
                            gui.optionsMenuButtonClicked(e.getSlot(), player, mseEntity));

                } else if (inventory.getName().equals(mseEntity.getName())) {
                    e.setCancelled(
                            gui.mainMenuButtonClicked(e.getSlot(), player, mseEntity));

                } else if (inventory.getName().contains(" Inventory")) {
                    if (mseEntity.isTamed()) {
                        e.setCancelled(
                                gui.inventoryMenuButtonClicked(e.getSlot(), player, mseEntity));
                    } else {
                        e.setCancelled(
                                gui.tamingMenuButtonClicked(e.getSlot(), player, mseEntity));
                    }
                }

            } else {

                if (inventory.getName().equals("Current ranks:")) {
                    e.setCancelled(gui.rankViewButtonclicked(e.getSlot(), player));

                } else if (inventory.getName().equals("Edit current ranks:")) {
                    e.setCancelled(gui.rankEditButtonClicked(e.getSlot(), player));
                }

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
