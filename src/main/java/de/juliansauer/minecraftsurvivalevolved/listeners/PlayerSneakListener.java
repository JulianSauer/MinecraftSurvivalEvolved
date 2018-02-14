package de.juliansauer.minecraftsurvivalevolved.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakListener implements BasicEventListener {

    /**
     * Throws off carried entities.
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking())
            return;

        e.getPlayer().eject();
    }

}
