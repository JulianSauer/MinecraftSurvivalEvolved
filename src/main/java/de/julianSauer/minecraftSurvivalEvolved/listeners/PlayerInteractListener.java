package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.visuals.inventories.InventoryGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Handles mounting of tamed entities and opening of their main menu.
 */
public class PlayerInteractListener implements BasicEventListener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        MSEEntity mseEntity = getMSEEntityFromEntity(entity);
        if (mseEntity == null)
            return;

        Player player = e.getPlayer();

        if (mseEntity.getTamingHandler().isUnconscious() && !mseEntity.getTamingHandler().isTamed()) {
            InventoryGUI.openTamingGUI(player, mseEntity);
        } else if (mseEntity.getTamingHandler().isTamed() && mseEntity.getTamingHandler().getOwner().equals(player.getUniqueId())) {
            if (player.isSneaking())
                InventoryGUI.openMainGUI(player, mseEntity);
            else if (entity.isEmpty())
                entity.setPassenger(player);
        }

    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {

        // Cancel event if a hologram is clicked
        if (!e.getRightClicked().isVisible())
            e.setCancelled(true);

    }

}
