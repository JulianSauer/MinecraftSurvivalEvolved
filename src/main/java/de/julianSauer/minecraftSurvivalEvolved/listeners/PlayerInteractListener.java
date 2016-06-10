package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PlayerInteractListener<T extends Tameable & InventoryHolder> extends BasicListener {

    boolean enableTestRiding = false;

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

        if (enableTestRiding) {
            testRide(e);
            return;
        }

        Entity entity = e.getRightClicked();
        Tameable tameableEntity = getTameableEntityFromEntity(entity);
        if (tameableEntity == null)
            return;

        Player player = e.getPlayer();

        if (tameableEntity.isUnconscious() && tameableEntity instanceof InventoryHolder) {
            openTamingGUI(player, entity, (T) tameableEntity);
        } else if (tameableEntity.tamed() && tameableEntity.getOwners().equals(player.getUniqueId())) {
            if (entity.isEmpty())
                entity.setPassenger(player);
        }

    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {

        // Cancel event if a hologram is clicked
        if (!e.getRightClicked().isVisible())
            e.setCancelled(true);

    }

    /**
     * Allows entity riding without taming the entity first.
     *
     * @param e
     */
    private void testRide(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if (getTameableEntityFromEntity(entity) == null)
            return;
        if (entity.isEmpty())
            entity.setPassenger(player);

    }

    private void openTamingGUI(Player player, Entity entity, T tameable) {

        String name = entity.getCustomName();
        if (name == null)
            name = entity.getName();

        Inventory tamingGUI = tameable.getInventory();
        player.openInventory(tamingGUI);

    }

}
