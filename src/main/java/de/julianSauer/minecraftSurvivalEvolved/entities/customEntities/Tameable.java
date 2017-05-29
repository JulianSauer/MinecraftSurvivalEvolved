package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.TameableEntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimer;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimerTameable;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MiningHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.PathfinderHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable extends Unconsciousable, InventoryHolder {

    TameableEntityAttributes getTameableEntityAttributes();

    TamingHandler getTamingHandler();

    MiningHandler getMiningHandler();

    PathfinderHandler getPathfinderHandler();

    default void forceTame(Player newOwner) {
        getTamingHandler().forceTame(newOwner);
    }

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

    World getWorld();

    Location getLocation();

    default PathfinderHandler.EntityMode getEntityMode() {
        return getPathfinderHandler().getEntityMode();
    }

    default void setPassiveGoals() {
        getPathfinderHandler().setPassiveGoals();
    }

    default void setNeutralGoals() {
        getPathfinderHandler().setNeutralGoals();
    }

    default void setAggressiveGoals() {
        getPathfinderHandler().setAggressiveGoals();
    }

    default void setWandering(boolean wandering) {
        getPathfinderHandler().setWandering(wandering);
    }

    default void toggleFollowing(EntityPlayer player) {
        getPathfinderHandler().toggleFollowing(player);
    }

    default EntityPlayer getFollowingPlayer() {
        return getPathfinderHandler().getFollowingPlayer();
    }

    default boolean isFollowing() {
        return getPathfinderHandler().isFollowing();
    }

    /**
     * Loads data for this entity.
     *
     * @param data NBTTags used for loading
     */
    default void load(NBTTagCompound data) {
        getTameableEntityAttributes().initWith(data);
        getTamingHandler().initWith(data);
        getPathfinderHandler().initWith(data);
    }

    /**
     * Saves data for this entity.
     *
     * @param data NBTTags used for saving
     */
    default void save(NBTTagCompound data) {
        getTameableEntityAttributes().saveData(data);
        getTamingHandler().saveData(data);
        getPathfinderHandler().saveData(data);
        data.setBoolean("MSEInitialized", true);
    }

    @Override
    default void feedNarcotics() {
        Inventory inventory = getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.POTION) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equalsIgnoreCase("Narcotics")) {
                    if (item.getAmount() <= 1)
                        item = new ItemStack(Material.AIR);
                    else
                        item.setAmount(item.getAmount() - 1);
                    inventory.setItem(i, item);
                    eatAnimation();
                    increaseTorpidityBy(20);
                    break;
                }
            }
        }
    }

    /**
     * Shows an eat animation by moving the head upwards and playing a sound.
     */
    default void eatAnimation() {
        setPitchWhileTaming(-30F);
        getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
        (new BukkitRunnable() {
            @Override
            public void run() {
                setPitchWhileTaming(0F);
                this.cancel();
            }
        }).runTaskTimerAsynchronously(MSEMain.getInstance(), 4L, 0L);

    }

    @Override
    default void updateConsciousness() {
        getTamingHandler().updateConsciousness();
    }

    @Override
    default UnconsciousnessTimerTameable getUnconsciousnessTimer() {
        return getTamingHandler().getUnconsciousnessTimerTameable();
    }

    @Override
    default void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer) {
        if (unconsciousnessTimer instanceof UnconsciousnessTimerTameable)
            getTamingHandler().setUnconsciousnessTimerTameable((UnconsciousnessTimerTameable) unconsciousnessTimer);
        else
            throw new IllegalArgumentException("Wrong type for timer");
    }

}
