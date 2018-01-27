package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.gui.inventories.ButtonIcons;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Handles drops of custom entities.
 */
public class EntityDeathListener implements BasicEventListener {

    private ButtonIcons icons;

    public EntityDeathListener() {
        icons = new ButtonIcons();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        MSEEntity mseEntity = getMSEEntityFromEntity(e.getEntity());
        if (mseEntity == null)
            return;

        dropInventory(mseEntity.getInventory(), e.getDrops());
        sendDeathMessage(mseEntity);

    }

    /**
     * Removes the items used as buttons.
     *
     * @param entityInventory Inventory of the dying entity
     * @param drops           Default items that are droppped by the entity type
     */
    private void dropInventory(Inventory entityInventory, List<ItemStack> drops) {

        ItemStack backButton = entityInventory.getItem(0);
        if (icons.isButtonIcon(backButton, icons.getBackButton()) || icons.isButtonIcon(backButton, icons.getForceFeedButton()))
            entityInventory.setItem(0, new ItemStack(Material.AIR));

        drops.addAll(Arrays.asList(entityInventory.getContents()));
        entityInventory.clear();

    }

    /**
     * Informs the owners of the poor thing.
     *
     * @param mseEntity Entity that died
     */
    private void sendDeathMessage(MSEEntity mseEntity) {

        UUID tribeUUID = mseEntity.getTribe();
        if (tribeUUID == null)
            return;
        Tribe tribe = TribeRegistry.getTribeRegistry().getTribe(tribeUUID);

        if (tribe != null)
            BarHandler.sendEntityDeathMessageTo(tribe, mseEntity.getName());

        else {
            Player owner = Bukkit.getPlayer(mseEntity.getMSEOwner());
            if (owner != null)
                BarHandler.sendEntityDeathMessageTo(owner, mseEntity.getName());
        }

    }

}
