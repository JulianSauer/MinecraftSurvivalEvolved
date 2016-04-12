package de.gmx.endermansend.tameableCreatures.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if (entity.isEmpty()) {
            entity.setPassenger(player);
        } else if (entity.getPassenger().equals(player))
            entity.eject();

    }

}
