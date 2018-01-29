package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * Handles mining of blocks wile riding.
 */
public class BlockDamageListener implements BasicEventListener {

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {

        MSEEntity mseEntity = getMSEEntityFromVehicle(e.getPlayer());
        if (mseEntity != null) {
            if (mseEntity.getMiningHandler().canMineBlock(e.getBlock().getType()))
                e.setCancelled(true);
        }
    }

}
