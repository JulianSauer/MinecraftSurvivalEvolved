package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;

public class EntityDeathListener extends BasicListener {

    /**
     * Adds inventory items of an entity to the drops on death.
     *
     * @param e
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        Tameable entity = getTameableEntityFromEntity(e.getEntity());
        if (entity == null || !(entity instanceof InventoryHolder))
            return;

        Inventory entityInventory = ((InventoryHolder) entity).getInventory();
        e.getDrops().addAll(Arrays.asList(entityInventory.getContents()));
        entityInventory.clear();

    }

}
