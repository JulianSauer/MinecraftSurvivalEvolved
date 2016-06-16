package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.visuals.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener extends BasicInventoryListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {

        if (playerClickedOnButtonMenu(e)) {

            Inventory inventory = e.getClickedInventory();
            Player player = (Player) e.getWhoClicked();
            MSEEntity mseEntity = (MSEEntity) inventory.getHolder();

            if (inventory.getName().equals("Entity Options")) {
                // Entity options GUI
                e.setCancelled(
                        processEntityOptionsMenu(player, mseEntity, inventory, e.getCurrentItem())
                );
            } else if (inventory.getName().equals(mseEntity.getEntityStats().getEntityType())) {
                // Main GUI
                e.setCancelled(
                        processMainMenu(player, mseEntity, inventory, e.getCurrentItem())
                );
            } else if (inventory.getName().contains(" Inventory")) {
                // Inventory GUI
                e.setCancelled(
                        processInventoryMenu(player, mseEntity, inventory, e.getCurrentItem())
                );
            }
        }
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

    private boolean processEntityOptionsMenu(Player player, MSEEntity mseEntity, Inventory inventory, ItemStack currentItem) {

        ItemStack[] buttons = InventoryGUI.getOptionsMenuButtons();
        if (!buttonsAreValid(buttons, inventory.getContents()))
            return false;

        if (buttonNamesAreEqual(currentItem, buttons[0])) {
            // Back
            InventoryGUI.openMainGUI(player, mseEntity);

        } else if (buttonNamesAreEqual(currentItem, buttons[1])) {
            // Change name
            // TODO
            return true;

        } else if (buttonNamesAreEqual(currentItem, buttons[2])) {
            // Health
            // TODO
            return true;

        } else if (buttonNamesAreEqual(currentItem, buttons[3])) {
            // Damage
            // TODO
            return true;

        } else if (buttonNamesAreEqual(currentItem, buttons[4])) {
            // Food
            // TODO
            return true;

        }
        return false;

    }

    private boolean processMainMenu(Player player, MSEEntity mseEntity, Inventory inventory, ItemStack currentItem) {

        ItemStack[] buttons = InventoryGUI.getMainMenuButtons();
        if (!buttonsAreValid(buttons, inventory.getContents()))
            return false;

        if (buttonNamesAreEqual(currentItem, buttons[0])) {
            // Open inventory
            InventoryGUI.openInventoryGUI(player, mseEntity);
            return true;

        } else if (buttonNamesAreEqual(currentItem, buttons[1])) {
            // Open options
            InventoryGUI.openOptionsGUI(player, mseEntity, false);
            return true;
        }
        return false;

    }

    private boolean processInventoryMenu(Player player, MSEEntity mseEntity, Inventory inventory, ItemStack currentItem) {

        ItemStack[] buttons = InventoryGUI.getInventoryMenuButtons();
        if (!buttonsAreValid(buttons, inventory.getContents()))
            return false;

        if (buttonNamesAreEqual(currentItem, buttons[0])) {
            // Back
            InventoryGUI.openMainGUI(player, mseEntity);
            return true;
        }
        return false;

    }


}
