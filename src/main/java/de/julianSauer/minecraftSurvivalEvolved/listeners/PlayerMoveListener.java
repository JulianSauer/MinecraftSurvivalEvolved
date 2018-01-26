package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.UnconsciousPlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerMoveListener implements BasicEventListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (UnconsciousPlayers.contains(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }

}
