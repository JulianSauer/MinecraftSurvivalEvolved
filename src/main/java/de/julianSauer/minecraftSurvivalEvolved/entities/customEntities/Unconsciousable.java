package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimer;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimerHuman;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Basic functionality of an entity that can be knocked out.
 */
public interface Unconsciousable extends InventoryHolder {

    /**
     * Increases the torpidity and updates the consciousness of an entity.
     */
    default void increaseTorpidityBy(int torpidityIncrease) {
        getEntityAttributes().setTorpidity(getEntityAttributes().getTorpidity() + torpidityIncrease);
        if (getEntityAttributes().getTorpidity() > getEntityAttributes().getMaxTorpidity())
            getEntityAttributes().setTorpidity(getEntityAttributes().getMaxTorpidity());
        updateConsciousness();
    }

    default void decreaseTorpidityBy(int torpidityDecrease) {
        getEntityAttributes().setTorpidity(getEntityAttributes().getTorpidity() - torpidityDecrease);
        if (getEntityAttributes().getTorpidity() < 0)
            getEntityAttributes().setTorpidity(0);
        updateConsciousness();
    }

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
    void eatAnimation();

    EntityAttributes getEntityAttributes();

    Object getEntity();

    UnconsciousnessTimer getUnconsciousnessTimer();

    void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer);

    /**
     * Updates the consciousness of the entity.
     */
    default void updateConsciousness() {

        if (getEntityAttributes().isUnconscious() && getEntityAttributes().getTorpidity() <= 0) {
            // Wake up
            getEntityAttributes().setUnconscious(false);
            if (getUnconsciousnessTimer() != null && getUnconsciousnessTimer().isThreadCurrentlyRunning())
                getUnconsciousnessTimer().cancel();

        } else if ((!getEntityAttributes().isUnconscious() && getEntityAttributes().getFortitude() != null && getEntityAttributes().getTorpidity() >= getEntityAttributes().getFortitude())) {
            // Fall asleep
            getEntityAttributes().setUnconscious(true);
            setUnconsciousnessTimer(new UnconsciousnessTimerHuman(getEntity()));
            getUnconsciousnessTimer().runTaskTimer(MSEMain.getInstance(), 0, getUnconsciousnessTimer().getUnconsciousnessUpdateInterval());
        }

    }

}
