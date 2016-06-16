package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.visuals.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Handles drops of custom entities.
 */
public class EntityDeathListener extends BasicListener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        MSEEntity mseEntity = getMSEEntityFromEntity(e.getEntity());
        if (mseEntity == null)
            return;

        Inventory entityInventory = mseEntity.getInventory();

        // Remove back button
        ItemStack backButton = entityInventory.getItem(0);
        if (backButton != null
                && backButton.getType() == InventoryGUI.getInventoryMenuButtons()[0].getType()
                && backButton.getItemMeta().hasDisplayName()
                && backButton.getItemMeta().getDisplayName().equals(InventoryGUI.getInventoryMenuButtons()[0].getItemMeta().getDisplayName()))
            entityInventory.setItem(0, new ItemStack(Material.AIR));

        e.getDrops().addAll(Arrays.asList(entityInventory.getContents()));
        entityInventory.clear();

    }

}
