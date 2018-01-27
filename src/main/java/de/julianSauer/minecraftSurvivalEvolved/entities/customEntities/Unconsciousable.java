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

    int getTorpidity();

    void setTorpidity(int torpidity);

    int getMaxTorpidity();

    int getTorporDepletion();

    boolean isUnconscious();

    void setUnconscious(boolean unconscious);

    int getFortitude();

    /**
     * Increases the torpidity and updates the consciousness of an entity.
     */
    default void increaseTorpidityBy(int torpidityIncrease) {
        setTorpidity(getTorpidity() + torpidityIncrease);
        if (getTorpidity() > getMaxTorpidity())
            setTorpidity(getMaxTorpidity());
        updateConsciousness();
    }

    default void decreaseTorpidityBy(int torpidityDecrease) {
        setTorpidity(getTorpidity() - torpidityDecrease);
        if (getTorpidity() < 0)
            setTorpidity(0);
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

        if (isUnconscious() && getTorpidity() <= 0) {
            // Wake up
            setUnconscious(false);
            if (getUnconsciousnessTimer() != null && getUnconsciousnessTimer().isThreadCurrentlyRunning())
                getUnconsciousnessTimer().cancel();

        } else if ((!isUnconscious() && getTorpidity() >= getFortitude())) {
            // Fall asleep
            setUnconscious(true);
            setUnconsciousnessTimer(new UnconsciousnessTimerHuman(getEntity()));
            getUnconsciousnessTimer().runTaskTimer(MSEMain.getInstance(), 0, getUnconsciousnessTimer().getUnconsciousnessUpdateInterval());
        }

    }

}
