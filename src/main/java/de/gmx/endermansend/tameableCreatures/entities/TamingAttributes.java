package de.gmx.endermansend.tameableCreatures.entities;

import de.gmx.endermansend.tameableCreatures.main.TameableCreatures;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Implements generic functionality for taming that can be used to implement Tameable.
 */
public class TamingAttributes {

    private boolean tamed;
    private boolean tameable;
    private boolean unconscious;

    private int torpidity;
    private int maxTorpidity;
    private int fortitude;
    private int torporDepletion;
    private int tamingProgress;
    private int tamingProgressInterval;

    private UUID owner;
    private UUID tamer;

    private UnconsciousnessTimer unconsciousnessTimer;
    private long unconsciousnessUpdateInterval;

    public TamingAttributes() {
        tamed = false;
        tameable = true;
        unconscious = false;

        torpidity = 0;
        maxTorpidity = 100;
        fortitude = 20;
        torporDepletion = 1;
        tamingProgress = 0;
        tamingProgressInterval = 0;
        unconsciousnessUpdateInterval = 100L;
    }

    public TamingAttributes(int maxTorpidity, int fortitude, int torporDepletion) {
        this();
        this.maxTorpidity = maxTorpidity;
        this.fortitude = fortitude;
        this.torporDepletion = torporDepletion;
    }

    public TamingAttributes(int maxTorpidity, int fortitude, int torporDepletion, int tamingProgressInterval, boolean tameable) {
        this(maxTorpidity, fortitude, torporDepletion);
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

    public boolean setSuccessfullyTamed() {

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
        } else if (!isUnconscious() && torpidity >= fortitude) {
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(TameableCreatures.getInstance(), 0, unconsciousnessUpdateInterval);
        }

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
            tamingProgress += tamingProgressInterval;
            if (tamingProgress >= 100) {
                setSuccessfullyTamed();
            }

        }

    }

}
