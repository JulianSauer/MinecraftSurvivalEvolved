package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        if (!(entity instanceof CraftEntity))
            return;
        if (!(((CraftEntity) entity).getHandle() instanceof Tameable))
            return;

        Tameable tameableEntity = (Tameable) ((CraftEntity) entity).getHandle();

        Player player = e.getPlayer();

        if (tameableEntity.isUnconscious()) {
            openTamingGUI(player, entity, tameableEntity);
        } else if (tameableEntity.isTamed() && tameableEntity.getOwner().equals(player.getUniqueId())) {
            if (entity.isEmpty())
                entity.setPassenger(player);
        }

    }

    private void openTamingGUI(Player player, Entity entity, Tameable tameableEntity) {

        String name = entity.getCustomName();
        if (name == null)
            name = entity.getName();

        Inventory tamingGUI = Bukkit.createInventory(player, 18, name + " Torpidity: " + tameableEntity.getTorpidity() + "/" + tameableEntity.getMaxTorpidity() + " Taming: " + tameableEntity.getTamingProgress() + "/" + tameableEntity.getMaxTamingProgress());
        player.openInventory(tamingGUI);

    }

}
