package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

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
        getAttributedEntity().setTorpidity(getAttributedEntity().getTorpidity() + torpidityIncrease);
        if (getAttributedEntity().getTorpidity() > getAttributedEntity().getMaxTorpidity())
            getAttributedEntity().setTorpidity(getAttributedEntity().getMaxTorpidity());
        updateConsciousness();
    }

    default void decreaseTorpidityBy(int torpidityDecrease) {
        getAttributedEntity().setTorpidity(getAttributedEntity().getTorpidity() - torpidityDecrease);
        if (getAttributedEntity().getTorpidity() < 0)
            getAttributedEntity().setTorpidity(0);
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

    AttributedEntity getAttributedEntity();

    Object getEntity();

    UnconsciousnessTimer getUnconsciousnessTimer();

    void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer);

    /**
     * Updates the consciousness of the entity.
     */
    default void updateConsciousness() {

        if (getAttributedEntity().isUnconscious() && getAttributedEntity().getTorpidity() <= 0) {
            // Wake up
            getAttributedEntity().setUnconscious(false);
            if (getUnconsciousnessTimer() != null && getUnconsciousnessTimer().isThreadCurrentlyRunning())
                getUnconsciousnessTimer().cancel();

        } else if ((!getAttributedEntity().isUnconscious() && getAttributedEntity().getTorpidity() >= getAttributedEntity().getFortitude())) {
            // Fall asleep
            getAttributedEntity().setUnconscious(true);
            setUnconsciousnessTimer(new UnconsciousnessTimerHuman(getEntity()));
            getUnconsciousnessTimer().runTaskTimer(MSEMain.getInstance(), 0, getUnconsciousnessTimer().getUnconsciousnessUpdateInterval());
        }

    }

}
