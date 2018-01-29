package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeMemberRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Starts listening for packets for this player and registers him as a tribe member.
 */
public class PlayerJoinListener implements BasicEventListener {

    private MSEPlayerMap msePlayerMap;

    private TribeMemberRegistry tribeMemberRegistry;

    public PlayerJoinListener() {
        msePlayerMap = MSEPlayerMap.getPlayerRegistry();
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        MSEMain.getInstance().getPacketInjector().addPlayer(e.getPlayer());
        msePlayerMap.registerPlayer(e.getPlayer());
        tribeMemberRegistry.registerPlayer(e.getPlayer());

    }

}
