package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Stops listening for packets for this player and unregisters him as a tribe member.
 */
public class PlayerQuitListener implements BasicEventListener {

    private TribeMemberRegistry tribeMemberRegistry;

    public PlayerQuitListener() {
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        MSEMain.getInstance().getPacketInjector().removePlayer(e.getPlayer());
        tribeMemberRegistry.unregisterPlayer(e.getPlayer());
    }

}
