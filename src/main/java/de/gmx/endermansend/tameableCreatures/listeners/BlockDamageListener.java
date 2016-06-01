package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamageListener extends BasicListener {

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {

        Tameable tameableEntity = getTameableEntityFromVehicle(e.getPlayer());
        if (tameableEntity != null) {
            if (tameableEntity.getMineableBlocks().contains(e.getBlock().getType()))
                e.setInstaBreak(true);
        }

    }

}
