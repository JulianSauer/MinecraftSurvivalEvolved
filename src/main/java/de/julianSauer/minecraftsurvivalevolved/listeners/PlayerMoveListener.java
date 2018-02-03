package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.UnconsciousPlayers;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements BasicEventListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        LivingEntity player = e.getPlayer();
        if (UnconsciousPlayers.contains(player.getUniqueId()) && player.isOnGround())
            e.setCancelled(true);
    }

}
