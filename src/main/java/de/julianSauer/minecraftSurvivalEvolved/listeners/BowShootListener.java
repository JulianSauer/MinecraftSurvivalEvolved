package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Handles attacks with tranquilizer arrows. Sets metadata for EntityDamageByEntityListener.
 */
public class BowShootListener implements BasicEventListener {

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {

        LivingEntity livingEntity = e.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;

            ItemStack shotArrow = getShotArrow(player);
            if (shotArrow == null)
                return;
            ItemMeta arrowMeta = shotArrow.getItemMeta();

            if (arrowMeta.hasDisplayName()) {
                if (arrowMeta.getDisplayName().equals("Tranquilizer Arrow")) {
                    e.getProjectile().setMetadata("Tranquilizer Arrow", new FixedMetadataValue(MSEMain.getInstance(), true));
                }
            }

        }

    }

    /**
     * Finds the arrow stack that is shot with the bow.
     *
     * @param player The player who's shooting
     * @return ItemStack that is shot
     */
    private ItemStack getShotArrow(Player player) {

        PlayerInventory inventory = player.getInventory();
        ItemStack arrow = inventory.getItemInOffHand();
        if (isArrow(arrow))
            return arrow;

        for (int i = 0; i < inventory.getSize(); i++) {
            arrow = inventory.getItem(i);
            if (isArrow(arrow))
                return arrow;
        }

        return null;
    }

    /**
     * Checks if the ItemStack is a normal arrow.
     *
     * @param arrow ItemStack that should be checked
     * @return True if it's a normal arrow
     */
    private boolean isArrow(ItemStack arrow) {
        return arrow != null
                && (arrow.getType().equals(Material.ARROW)
                || arrow.getType().equals(Material.SPECTRAL_ARROW)
                || arrow.getType().equals(Material.TIPPED_ARROW));
    }

}
