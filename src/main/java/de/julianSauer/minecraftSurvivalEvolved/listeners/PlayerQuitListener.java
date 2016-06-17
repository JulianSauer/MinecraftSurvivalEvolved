package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Stops listening for packets for this player.
 */
public class PlayerQuitListener extends BasicListener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        ThisPlugin.getPacketInjector().removePlayer(e.getPlayer());
    }

}
