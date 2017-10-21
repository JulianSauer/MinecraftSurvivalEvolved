package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
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
