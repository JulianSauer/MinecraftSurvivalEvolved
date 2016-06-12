package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class EntityDeathListener extends BasicListener {

    /**
     * Adds inventory items of an entity to the drops on death.
     *
     * @param e
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        MSEEntity mseEntity = getMSEEntityFromEntity(e.getEntity());
        if (mseEntity == null)
            return;

        Inventory entityInventory = mseEntity.getInventory();
        e.getDrops().addAll(Arrays.asList(entityInventory.getContents()));
        entityInventory.clear();

    }

}
