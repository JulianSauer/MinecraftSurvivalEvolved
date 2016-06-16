package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.visuals.ScoreboardHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Removes scoreboard when a player closes a custom inventory.
 */
public class InventoryCloseListener extends BasicListener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {

        if (!(e.getPlayer() instanceof Player))
            return;

        ScoreboardHandler.removePlayer((Player) e.getPlayer());

    }

}
