package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractListener extends BasicListener {

    boolean enableTestRiding = false;

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

        if (enableTestRiding) {
            testRide(e);
            return;
        }

        Entity entity = e.getRightClicked();
        MSEEntity mseEntity = getMSEEntityFromEntity(entity);
        if (mseEntity == null)
            return;

        Player player = e.getPlayer();

        if (mseEntity.getTamingHandler().isUnconscious()) {
            openTamingGUI(player, entity, mseEntity);
        } else if (mseEntity.getTamingHandler().isTamed() && mseEntity.getTamingHandler().getOwner().equals(player.getUniqueId())) {
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

        if (getMSEEntityFromEntity(entity) == null)
            return;
        if (entity.isEmpty())
            entity.setPassenger(player);

    }

    private void openTamingGUI(Player player, Entity entity, MSEEntity mse) {

        String name = entity.getCustomName();
        if (name == null)
            name = entity.getName();

        Inventory tamingGUI = mse.getInventory();
        player.openInventory(tamingGUI);

    }

}
