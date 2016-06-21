package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Starts listening for packets for this player.
 */
public class PlayerJoinListener implements BasicEventListener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        MSEMain.getPacketInjector().addPlayer(e.getPlayer());
    }

}
