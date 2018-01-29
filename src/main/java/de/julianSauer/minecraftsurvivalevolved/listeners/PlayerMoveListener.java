package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.UnconsciousPlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements BasicEventListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (UnconsciousPlayers.contains(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }

}
