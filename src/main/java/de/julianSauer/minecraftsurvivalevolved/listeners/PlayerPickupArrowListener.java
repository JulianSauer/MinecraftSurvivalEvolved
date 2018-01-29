package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.items.CustomRecipes;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Replaces normal tipped arrows with tranq arrows.
 */
public class PlayerPickupArrowListener implements ArrowListener {

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupArrowEvent e) {

        if (!isTranqArrow(e.getArrow()))
            return;

        ItemStack arrow = CustomRecipes.getTranquilizerArrow();
        Player player = e.getPlayer();
        Map didNotFit = player.getInventory().addItem(arrow);
        if (didNotFit.isEmpty()) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5F, 8);
            e.getArrow().remove();
            e.setCancelled(true);
        }

    }

}
