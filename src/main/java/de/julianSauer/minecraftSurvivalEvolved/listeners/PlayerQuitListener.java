package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Stops listening for packets for this player.
 */
public class PlayerQuitListener implements BasicEventListener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        MSEMain.getInstance().getPacketInjector().removePlayer(e.getPlayer());
    }

}
