package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.visuals.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener extends BasicListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Inventory inventory = e.getClickedInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
        if (!(inventoryHolder instanceof MSEEntity))
            return;

        MSEEntity mseEntity = (MSEEntity) inventoryHolder;
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) e.getWhoClicked();

        if (inventory.getName().equals("Entity Stats")) {

            // Entity stats GUI
            ItemStack[] buttons = InventoryGUI.getStatsButtons();
            for (int i = 0; i < buttons.length; i++)
                if (!compareItems(buttons[i], inventory.getItem(i)))
                    return;

            if(e.getCurrentItem().getItemMeta().getDisplayName().equals("Back")) {
                e.setCancelled(true);
                InventoryGUI.openTamedGUI(player, mseEntity);
            }

        } else if (inventory.getName().equals(mseEntity.getEntityStats().getEntityType())) {

            // Tamed GUI
            ItemStack[] buttons = InventoryGUI.getTamedButtons();
            for (int i = 0; i < buttons.length; i++)
                if (!compareItems(buttons[i], inventory.getItem(i)))
                    return;

            if(e.getCurrentItem().getItemMeta().getDisplayName().equals("Open Inventory")) {
                e.setCancelled(true);
                InventoryGUI.openTamingGUI(player, mseEntity);
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals("Open Entity Stats")) {
                e.setCancelled(true);
                InventoryGUI.openStatsGUI(player, mseEntity);
            }

        }

    }

    /**
     * Compares type and name of an item.
     *
     * @param item1 First item
     * @param item2 Second item
     * @return True if type and name are equal
     */
    private boolean compareItems(ItemStack item1, ItemStack item2) {
        return item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())
                && item1.getType() == item2.getType();
    }

}
