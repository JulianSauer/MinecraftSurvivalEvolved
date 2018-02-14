package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayer;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.UnconsciousPlayers;
import de.juliansauer.minecraftsurvivalevolved.gui.inventories.InventoryGUI;
import de.juliansauer.minecraftsurvivalevolved.gui.visuals.ScoreboardHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Handles mounting of tamed entities and opening of their main menu.
 */
public class PlayerInteractListener implements BasicEventListener {

    private InventoryGUI gui;

    public PlayerInteractListener() {
        gui = new InventoryGUI();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUnconsciousPlayerInteract(PlayerInteractEvent e) {
        if (playerIsUnconscious(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

        if (playerIsUnconscious(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }

        Entity entity = e.getRightClicked();
        MSEEntity mseEntity = getMSEEntityFromEntity(entity);
        if (mseEntity == null)
            return;

        Player player = e.getPlayer();
        mseEntity.interact(entity, mseEntity, player, gui);

    }

    @EventHandler
    public void onPlayerAccessUnconsciousInventory(PlayerInteractEntityEvent e) {

        if (playerIsUnconscious(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }

        Entity entity = e.getRightClicked();
        if (!(entity instanceof Player))
            return;

        Player unconsciousPlayer = (Player) entity;
        if (!UnconsciousPlayers.contains(unconsciousPlayer.getUniqueId()))
            return;

        Player player = e.getPlayer();
        player.openInventory(unconsciousPlayer.getInventory());
        MSEPlayer unconsciousMSEPlayer = MSEPlayerMap.getPlayerRegistry().getMSEPlayer(unconsciousPlayer);
        if (unconsciousMSEPlayer != null)
            ScoreboardHandler.addPlayer(unconsciousMSEPlayer, player);

    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {

        // Cancel event if a hologram is clicked
        if (!e.getRightClicked().isVisible())
            e.setCancelled(true);

    }

}
