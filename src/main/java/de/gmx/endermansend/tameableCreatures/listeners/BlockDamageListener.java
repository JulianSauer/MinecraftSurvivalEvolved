package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamageListener implements Listener {

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {

        if (isMountedBlockDamage(e.getPlayer())) {
            e.setInstaBreak(true);
        }

    }

    /**
     * Checks if a player is mining a block while riding a tamed entity.
     *
     * @param player Mining player
     * @return True if player is riding a tamed entity
     */
    private boolean isMountedBlockDamage(Player player) {
        Entity vehicle = player.getVehicle();
        if (vehicle == null || !(vehicle instanceof CraftEntity) || !(((CraftEntity) vehicle).getHandle() instanceof Tameable))
            return false;
        return true;
    }

}
