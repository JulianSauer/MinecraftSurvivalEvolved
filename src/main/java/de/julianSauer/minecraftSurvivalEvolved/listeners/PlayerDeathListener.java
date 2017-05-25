package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMember;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements BasicEventListener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        TribeMember member = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(e.getEntity());
        if (member != null && member.hasTribe())
            BarHandler.sendPlayerDeathMessageTo(member.getTribe(), e.getDeathMessage());

    }

}
