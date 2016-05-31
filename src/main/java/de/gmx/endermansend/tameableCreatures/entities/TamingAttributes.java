package de.gmx.endermansend.tameableCreatures.entities;

import de.gmx.endermansend.tameableCreatures.main.TameableCreatures;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Implements generic functionality for taming that can be used to implement Tameable.
 */
public class TamingAttributes {

    private TameableSpider tameableEntity;

    private boolean tamed;
    private boolean tameable;
    private boolean unconscious;

    private int torpidity;
    private int maxTorpidity;
    private int fortitude;
    private int torporDepletion;
    private int tamingProgress;
    private int maxTamingProgress;
    private int tamingProgressInterval;

    private UUID owner;
    private UUID tamer;

    private UnconsciousnessTimer unconsciousnessTimer;
    private long unconsciousnessUpdateInterval;

    public TamingAttributes(TameableSpider tameableEntity) {

        this.tameableEntity = tameableEntity;

        tamed = false;
        tameable = true;
        unconscious = false;

        torpidity = 0;
        maxTorpidity = 100;
        fortitude = 20;
        torporDepletion = 1;
        tamingProgress = 0;
        tamingProgressInterval = 0;
        maxTamingProgress = 100;
        unconsciousnessUpdateInterval = 100L;
    }

    public TamingAttributes(TameableSpider tameableEntity, int maxTorpidity, int fortitude, int torporDepletion) {
        this(tameableEntity);
        this.maxTorpidity = maxTorpidity;
        this.fortitude = fortitude;
        this.torporDepletion = torporDepletion;
    }

    public TamingAttributes(TameableSpider tameableEntity, int maxTorpidity, int fortitude, int torporDepletion, int tamingProgressInterval, boolean tameable) {
        this(tameableEntity, maxTorpidity, fortitude, torporDepletion);
        this.tameable = tameable;
        this.tamingProgressInterval = tamingProgressInterval;
    }

    public boolean isTamed() {
        return tamed;
    }

    public boolean isTameable() {
        return tameable;
    }

    public boolean isUnconscious() {
        return unconscious;
    }

    public int getTorpidity() {
        return torpidity;
    }

    public int getMaxTorpidity() {
        return maxTorpidity;
    }

    public int getTamingProgress() {
        return tamingProgress;
    }

    public int getMaxTamingProgress() {
        return maxTamingProgress;
    }

    public UUID getOwner() {
        if (tamed)
            return owner;
        return null;
    }

    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        tamer = lastDamager;
        torpidity += torpidityIncrease;
        if (torpidity > maxTorpidity)
            torpidity = maxTorpidity;
        updateConsciousness();
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        torpidity -= torpidityDecrease;
        if (torpidity < 0)
            torpidity = 0;
        updateConsciousness();
    }

    private boolean setSuccessfullyTamed() {

        if (!isTamed() && isTameable() && tamer != null) {
            tamed = true;
            tameable = false;
            owner = tamer;
            decreaseTorpidityBy(maxTorpidity);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    private void updateConsciousness() {

        if (isUnconscious() && torpidity <= 0) {
            unconscious = false;
            if (unconsciousnessTimer != null) {
                unconsciousnessTimer.cancel();
                System.out.println("Entity is no longer unconscious");
            }
        } else if (!isUnconscious() && torpidity >= fortitude) {
            System.out.println("Entity is unconscious");
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(TameableCreatures.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    /**
     * Tries to eat food from the inventory.
     *
     * @return True if food was found.
     */
    private boolean updateHunger() {

        Inventory inventory = tameableEntity.getInventory();

        if (!inventory.contains(Material.RAW_BEEF, 1))
            return false;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item.getType() == Material.RAW_BEEF) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);
                return true;
            }
        }
        return false;
    }

    class UnconsciousnessTimer extends BukkitRunnable {

        public void run() {
            decreaseTorpidityBy(torporDepletion);
            updateTamingProcess();
        }

        /**
         * Returns the game ticks until the entity becomes conscious again.
         *
         * @return Remaining time in game ticks
         */
        public long getTimeUntilWakeUp() {
            return (torpidity / torporDepletion) * unconsciousnessUpdateInterval;
        }

        /**
         * Updates the progress if the entity is still tameable until it is tamed.
         */
        private void updateTamingProcess() {

            if (!isTameable() || isTamed())
                return;

            if (updateHunger())
                tamingProgress += tamingProgressInterval;
            else
                tamingProgress = (tamingProgress - tamingProgressInterval) < 0 ? 0 : tamingProgress - tamingProgressInterval;
            if (tamingProgress >= 10) {
                setSuccessfullyTamed();
            }

        }

    }

}
