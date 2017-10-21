package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Stops listening for packets for this player and unregisters him as a tribe member.
 */
public class PlayerQuitListener implements BasicEventListener {

    private MSEPlayerMap msePlayerMap;

    private TribeMemberRegistry tribeMemberRegistry;

    public PlayerQuitListener() {
        msePlayerMap = MSEPlayerMap.getPlayerRegistry();
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        MSEMain.getInstance().getPacketInjector().removePlayer(e.getPlayer());
        msePlayerMap.unregisterPlayer(e.getPlayer());
        tribeMemberRegistry.unregisterPlayer(e.getPlayer());
    }

}
