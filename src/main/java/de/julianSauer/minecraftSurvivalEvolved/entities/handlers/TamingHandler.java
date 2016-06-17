package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import de.julianSauer.minecraftSurvivalEvolved.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.visuals.HologramHandler;
import de.julianSauer.minecraftSurvivalEvolved.visuals.inventories.InventoryGUI;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Implements taming functionality for entities that can be used to implement Tameable.
 */
public class TamingHandler<T extends EntityInsentient & MSEEntity> {

    private T mseEntity;

    private int tamingProgress;

    private boolean unconscious;
    private boolean tamed;

    private int torporDepletion;
    private int torpidity;

    private UUID owner;
    private UUID tamer;

    private UnconsciousnessTimer unconsciousnessTimer;
    private boolean threadCurrentlyRunning;
    private long unconsciousnessUpdateInterval;

    public TamingHandler(T mseEntity) {

        this.mseEntity = mseEntity;

        threadCurrentlyRunning = false;

        unconscious = false;
        tamed = false;
        torpidity = 0;
        torporDepletion = 1;
        tamingProgress = 0;
        unconsciousnessUpdateInterval = 100L;

    }

    public boolean isTamed() {
        return tamed && !mseEntity.getEntityStats().isAlpha();
    }

    public boolean isTameable() {
        return mseEntity.getEntityStats().getBaseStats().isTameable() && !tamed && !mseEntity.getEntityStats().isAlpha();
    }

    public boolean isUnconscious() {
        return unconscious && !mseEntity.getEntityStats().isAlpha();
    }

    public int getTamingProgress() {
        if (mseEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return tamingProgress;
    }

    public int getMaxTamingProgress() {
        if (mseEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return (int) Calculator.calculateLevelDependentStatFor(mseEntity.getEntityStats().getBaseStats().getMaxTamingProgress(), mseEntity.getEntityStats().getLevel(), mseEntity.getEntityStats().getMultiplier());
    }

    public UUID getOwner() {
        if (tamed && !mseEntity.getEntityStats().isAlpha())
            return owner;
        return null;
    }

    public int getTorpidity() {
        if (mseEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return torpidity;
    }

    public int getMaxTorpidity() {
        return (int) Calculator.calculateLevelDependentStatFor(mseEntity.getEntityStats().getBaseStats().getMaxTorpidity(), mseEntity.getEntityStats().getLevel(), mseEntity.getEntityStats().getMultiplier());
    }

    /**
     * Creates an org.bukkit.Location based on the entity's coordinates.
     *
     * @return Current location of this entity
     */
    public Location getLocation() {
        return new Location(mseEntity.getWorld().getWorld(), mseEntity.locX, mseEntity.locY, mseEntity.locZ);
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is succesfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {

        if (mseEntity.getEntityStats().isAlpha())
            return;
        tamer = lastDamager;
        torpidity += torpidityIncrease;
        if (torpidity > getMaxTorpidity())
            torpidity = getMaxTorpidity();
        updateConsciousness();

    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        torpidity -= torpidityDecrease;
        if (torpidity < 0)
            torpidity = 0;
        updateConsciousness();
    }

    /**
     * Sets the owner of an entity and wakes it up.
     *
     * @return False if the entity could not be tamed
     */
    private boolean setSuccessfullyTamed() {

        if (isTameable() && tamer != null && !mseEntity.getEntityStats().isAlpha()) {
            tamed = true;
            owner = tamer;
            decreaseTorpidityBy(getMaxTorpidity());
            mseEntity.getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            mseEntity.setCustomName(mseEntity.getEntityStats().getDefaultName());
            BarHandler.sendTamedTextTo(Bukkit.getPlayer(owner), mseEntity.getName());
            InventoryGUI.closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
            return true;
        } else {
            return false;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    private void updateConsciousness() {

        if (mseEntity.getEntityStats().isAlpha())
            return;

        if (isUnconscious() && torpidity <= 0) {
            unconscious = false;
            if (unconsciousnessTimer != null && threadCurrentlyRunning) {
                unconsciousnessTimer.cancel();
            }
        } else if (!isUnconscious() && torpidity >= mseEntity.getEntityStats().getFortitude()) {
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(ThisPlugin.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    class UnconsciousnessTimer extends BukkitRunnable {

        UUID hologram;

        public UnconsciousnessTimer() {
            mseEntity.setCustomName("");
            hologram = HologramHandler.spawnHologramAt(getLocation(), getHologramText());
            threadCurrentlyRunning = true;
            mseEntity.getEntityStats().startFoodTimer();
        }

        public void run() {
            threadCurrentlyRunning = true;
            if (!mseEntity.isAlive()) {
                this.cancel();
                return;
            }

            decreaseTorpidityBy(torporDepletion);
            updateTamingProcess();
            if (!HologramHandler.updateHologram(hologram, getHologramText()))
                this.cancel();
        }

        /**
         * Stops thread and despawns the hologram.
         */
        @Override
        public void cancel() {
            HologramHandler.despawnHologram(hologram);
            mseEntity.setCustomName(mseEntity.getEntityStats().getDefaultName());
            threadCurrentlyRunning = false;
            super.cancel();
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

            if (!isTameable())
                return;

            int tamingIncrease = mseEntity.getEntityStats().updateHunger();
            if (tamingIncrease > 0) {
                eatAnimation();
            }
            tamingProgress += tamingIncrease;
            if (tamingProgress < 0)
                tamingProgress = 0;
            if (tamingProgress >= getMaxTamingProgress())
                setSuccessfullyTamed();

        }

        private void eatAnimation() {

            mseEntity.setPitchWhileTaming(-30F);
            mseEntity.getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
            (new BukkitRunnable() {
                @Override
                public void run() {
                    mseEntity.setPitchWhileTaming(0F);
                    this.cancel();
                }
            }).runTaskTimerAsynchronously(ThisPlugin.getInstance(), 4L, 0L);

        }

        private String[] getHologramText() {
            return new String[]{
                    ChatColor.DARK_AQUA + mseEntity.getEntityStats().getDefaultName(),
                    ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                    ChatColor.DARK_PURPLE + BarHandler.getProgressBar(getTorpidity(), getMaxTorpidity()),
                    ChatColor.GOLD + BarHandler.getProgressBar(getTamingProgress(), getMaxTamingProgress())
            };
        }

    }

}
