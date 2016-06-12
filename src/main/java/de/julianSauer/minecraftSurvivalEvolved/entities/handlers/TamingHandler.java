package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import de.julianSauer.minecraftSurvivalEvolved.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.visuals.HologramHandler;
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

    private T mceEntity;

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

    public TamingHandler(T mceEntity) {

        this.mceEntity = mceEntity;

        threadCurrentlyRunning = false;

        unconscious = false;
        tamed = false;
        torpidity = 0;
        torporDepletion = 1;
        tamingProgress = 0;
        unconsciousnessUpdateInterval = 100L;

    }

    public boolean isTamed() {
        return tamed && !mceEntity.getEntityStats().isAlpha();
    }

    public boolean isTameable() {
        return mceEntity.getEntityStats().getBaseStats().isTameable() && !tamed && !mceEntity.getEntityStats().isAlpha();
    }

    public boolean isUnconscious() {
        return unconscious && !mceEntity.getEntityStats().isAlpha();
    }

    public int getTamingProgress() {
        if (mceEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mceEntity.getName() + " at x:" + mceEntity.locX + " y:" + mceEntity.locY + " z:"
                    + mceEntity.locZ + ")");
        return tamingProgress;
    }

    public int getMaxTamingProgress() {
        if (mceEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mceEntity.getName() + " at x:" + mceEntity.locX + " y:" + mceEntity.locY + " z:"
                    + mceEntity.locZ + ")");
        return (int) Calculator.calculateLevelDependentStatFor(mceEntity.getEntityStats().getBaseStats().getMaxTamingProgress(), mceEntity.getEntityStats().getLevel(), mceEntity.getEntityStats().getMultiplier());
    }

    public UUID getOwner() {
        if (tamed && !mceEntity.getEntityStats().isAlpha())
            return owner;
        return null;
    }

    public int getTorpidity() {
        if (mceEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mceEntity.getName() + " at x:" + mceEntity.locX + " y:" + mceEntity.locY + " z:"
                    + mceEntity.locZ + ")");
        return torpidity;
    }

    public int getMaxTorpidity() {
        return (int) Calculator.calculateLevelDependentStatFor(mceEntity.getEntityStats().getBaseStats().getMaxTorpidity(), mceEntity.getEntityStats().getLevel(), mceEntity.getEntityStats().getMultiplier());
    }

    private Location getLocation() {
        return new Location(mceEntity.getWorld().getWorld(), mceEntity.locX, mceEntity.locY, mceEntity.locZ);
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is succesfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {

        if (mceEntity.getEntityStats().isAlpha())
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

        if (isTameable() && tamer != null && !mceEntity.getEntityStats().isAlpha()) {
            tamed = true;
            owner = tamer;
            decreaseTorpidityBy(getMaxTorpidity());
            mceEntity.getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            mceEntity.setCustomName(mceEntity.getEntityStats().getDefaultName());
            BarHandler.sendTamedTextTo(Bukkit.getPlayer(owner), mceEntity.getName());
            return true;
        } else {
            return false;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    private void updateConsciousness() {

        if (mceEntity.getEntityStats().isAlpha())
            return;

        if (isUnconscious() && torpidity <= 0) {
            unconscious = false;
            if (unconsciousnessTimer != null && threadCurrentlyRunning) {
                unconsciousnessTimer.cancel();
            }
        } else if (!isUnconscious() && torpidity >= mceEntity.getEntityStats().getBaseStats().getFortitude()) {
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(ThisPlugin.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }


    /*private boolean updateHunger() {

        if (mceEntity.getEntityStats().isAlpha())
            return false;

        Inventory inventory = mceEntity.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null)
                continue;
            if (mceEntity.getEntityStats().getBaseStats().getPreferredFood().contains(item.getType())) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);


                return true;
            }
        }
        return false;
    }*/

    class UnconsciousnessTimer extends BukkitRunnable {

        UUID hologram;

        public UnconsciousnessTimer() {
            mceEntity.setCustomName("");
            hologram = HologramHandler.spawnHologramAt(getLocation(), getHologramText());
            threadCurrentlyRunning = true;
            mceEntity.getEntityStats().startFoodTimer();
        }

        public void run() {
            threadCurrentlyRunning = true;
            if (!mceEntity.isAlive())
                this.cancel();

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
            mceEntity.setCustomName(mceEntity.getEntityStats().getDefaultName());
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

            int tamingIncrease = mceEntity.getEntityStats().updateHunger();
            System.out.println(getTamingProgress() + "+" + tamingIncrease + "/" + getMaxTamingProgress());
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

            mceEntity.setPitchWhileTaming(-30F);
            mceEntity.getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
            (new BukkitRunnable() {
                @Override
                public void run() {
                    mceEntity.setPitchWhileTaming(0F);
                    this.cancel();
                }
            }).runTaskTimerAsynchronously(ThisPlugin.getInstance(), 4L, 0L);

        }

        private String[] getHologramText() {
            return new String[]{
                    ChatColor.DARK_AQUA + mceEntity.getEntityStats().getDefaultName(),
                    ChatColor.DARK_AQUA + "Health: " + (int) mceEntity.getHealth() + "/" + (int) mceEntity.getMaxHealth(),
                    ChatColor.DARK_PURPLE + BarHandler.getProgressBar(getTorpidity(), getMaxTorpidity()),
                    ChatColor.GOLD + BarHandler.getProgressBar(getTamingProgress(), getMaxTamingProgress())
            };
        }

    }

}
