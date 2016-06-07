package de.gmx.endermansend.minecraftSurvivalEvolved.listeners;

import de.gmx.endermansend.minecraftSurvivalEvolved.entities.Tameable;
import de.gmx.endermansend.minecraftSurvivalEvolved.entities.customEntities.TameableSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PlayerInteractListener extends BasicListener {

    boolean enableTestRiding = true;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {

        if (enableTestRiding) {
            testRide(e);
            return;
        }

        Entity entity = e.getRightClicked();
        Tameable tameableEntity = getTameableEntityFromEntity(entity);
        if (tameableEntity == null)
            return;

        Player player = e.getPlayer();

        if (tameableEntity.isUnconscious()) {
            openTamingGUI(player, entity, (TameableSpider) tameableEntity);
        } else if (tameableEntity.tamed() && tameableEntity.getOwners().equals(player.getUniqueId())) {
            if (entity.isEmpty())
                entity.setPassenger(player);
        }

    }

    /**
     * Allows entity riding without taming the entity first.
     *
     * @param e
     */
    private void testRide(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();

        if(getTameableEntityFromEntity(entity) == null)
            return;
        if (entity.isEmpty())
            entity.setPassenger(player);

    }

    private <T extends Tameable & InventoryHolder> void openTamingGUI(Player player, Entity entity, T tameable) {

        String name = entity.getCustomName();
        if (name == null)
            name = entity.getName();

        Inventory tamingGUI = tameable.getInventory();
        player.openInventory(tamingGUI);

    }

}
