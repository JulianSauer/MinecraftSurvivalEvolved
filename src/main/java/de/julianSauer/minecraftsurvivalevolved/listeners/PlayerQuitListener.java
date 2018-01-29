package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeMemberRegistry;
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
