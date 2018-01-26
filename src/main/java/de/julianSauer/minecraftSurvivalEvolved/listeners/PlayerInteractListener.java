package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.UnconsciousPlayers;
import de.julianSauer.minecraftSurvivalEvolved.gui.inventories.InventoryGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Handles mounting of tamed entities and opening of their main menu.
 */
public class PlayerInteractListener implements BasicEventListener {

    private InventoryGUI gui;

    public PlayerInteractListener() {
        gui = new InventoryGUI();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        MSEEntity mseEntity = getMSEEntityFromEntity(entity);
        if (mseEntity == null)
            return;

        Player player = e.getPlayer();

        if (mseEntity.getTameableEntityAttributes().isUnconscious() && !mseEntity.getTameableEntityAttributes().isTamed() && !mseEntity.getTameableEntityAttributes().isAlpha()) {
            gui.openTamingGUI(player, mseEntity);
        } else if (mseEntity.getTameableEntityAttributes().isTamed() && mseEntity.getTameableEntityAttributes().isOwner(player.getUniqueId())) {
            if (player.isSneaking() || mseEntity.getTameableEntityAttributes().isUnconscious())
                gui.openMainGUI(player, mseEntity);
            else if (entity.isEmpty())
                entity.setPassenger(player);
        }

    }

    @EventHandler
    public void onPlayerAccessUnconsciousInventory(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        if (!(entity instanceof Player))
            return;

        Player unconsciousPlayer = (Player) entity;
        if (!UnconsciousPlayers.contains(unconsciousPlayer.getUniqueId()))
            return;

        Player player = e.getPlayer();
        player.openInventory(unconsciousPlayer.getInventory());

    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {

        // Cancel event if a hologram is clicked
        if (!e.getRightClicked().isVisible())
            e.setCancelled(true);

    }

}
