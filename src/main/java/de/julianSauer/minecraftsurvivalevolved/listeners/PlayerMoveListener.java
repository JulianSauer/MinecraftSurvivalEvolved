package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.UnconsciousPlayers;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements BasicEventListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (UnconsciousPlayers.contains(player.getUniqueId()) && player.isOnGround())
            e.setCancelled(true);

        if (player.isOnGround())
            return;
        Entity entity = player.getPassenger();
        if (entity == null
                || !(entity instanceof Chicken)
                || player.getVehicle() != null
                || player.isFlying())
            return;

        Block block = player.getLocation().subtract(0, 2, 0).getBlock();
        if (!block.isEmpty() && !block.isLiquid())
            return;

        Vector vector = player.getVelocity();
        if (vector.getY() < 1.0D) {
            vector.setX(vector.getX() * 1.1D);
            vector.setY(vector.getY() * 0.6D);
            vector.setZ(vector.getZ() * 1.1D);
            player.setVelocity(vector);
        }
    }

}
