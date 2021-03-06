package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayer;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import de.juliansauer.minecraftsurvivalevolved.gui.visuals.BarHandler;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeMember;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeMemberRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements BasicEventListener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        TribeMember member = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(e.getEntity());
        if (member != null && member.hasTribe())
            BarHandler.sendPlayerDeathMessageTo(member.getTribe(), e.getDeathMessage());

        MSEPlayer msePlayer = MSEPlayerMap.getPlayerRegistry().getMSEPlayer(e.getEntity());
        if (msePlayer != null && msePlayer.isUnconscious())
            msePlayer.getUnconsciousnessTimer().cancel();

    }

}
