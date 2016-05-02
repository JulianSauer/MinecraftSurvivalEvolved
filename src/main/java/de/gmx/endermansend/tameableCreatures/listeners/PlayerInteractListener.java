package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {

        if (!(e.getRightClicked() instanceof Tameable))
            return;

        Tameable tameableEntity = (Tameable) e.getRightClicked();
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if (tameableEntity.isUnconscious()) {
            openTamingGUI(player, entity);
        } else if (tameableEntity.isTamed() && tameableEntity.getOwner().equals(player.getUniqueId())) {
            if (entity.isEmpty())
                entity.setPassenger(player);
        }

    }

    private void openTamingGUI(Player player, Entity entity) {

        String name = entity.getCustomName();
        if (name == null)
            name = entity.getName();

        Inventory tamingGUI = Bukkit.createInventory(player, 16, name);
        player.openInventory(tamingGUI);

    }

}
