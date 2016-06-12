package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamageListener extends BasicListener {

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {

        MSEEntity mseEntity = getMSEEntityFromVehicle(e.getPlayer());
        if (mseEntity != null) {
            if (mseEntity.getEntityStats().getBaseStats().getMineableBlocks().contains(e.getBlock().getType()))
                e.setInstaBreak(true);
        }

    }

}
