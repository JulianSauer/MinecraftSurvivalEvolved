package de.juliansauer.minecraftsurvivalevolved.entities;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.TamingHandler;
import de.juliansauer.minecraftsurvivalevolved.gui.visuals.BarHandler;
import de.juliansauer.minecraftsurvivalevolved.gui.visuals.HologramHandler;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Handles unconscious state for tameable entities. Also interacts with {@link TamingHandler} to manage the taming process.
 */
public class UnconsciousnessTimerTameable<T extends EntityInsentient & MSEEntity> extends BukkitRunnable implements UnconsciousnessTimer {

    private UUID hologram;

    private boolean initialized;
    private T mseEntity;
    private TamingHandler tamingHandler;
    private boolean threadCurrentlyRunning = false;

    public UnconsciousnessTimerTameable(TamingHandler tamingHandler, T mseEntity) {

        this.mseEntity = mseEntity;
        this.tamingHandler = tamingHandler;
        threadCurrentlyRunning = true; // TODO: Remove initialization
        this.initialized = false;

        if (mseEntity.getWorld().isLoaded(mseEntity.getChunkCoordinates()))
            init();

    }

    private void init() {
        this.initialized = true;
        if (mseEntity.isTamed() || mseEntity.isAlpha())
            hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), ChatColor.RED + "Unconscious");
        else {
            mseEntity.startFoodTimer();
            mseEntity.setCustomName("");
            hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), getHologramText());
        }
    }

    @Override
    public void run() {
        threadCurrentlyRunning = true;
        if (!mseEntity.isAlive()) {
            this.cancel();
            return;
        }

        if (!initialized)
            init();

        mseEntity.decreaseTorpidityBy(mseEntity.getTorporDepletion());
        if (mseEntity.isTamed() || mseEntity.isAlpha())
            return;

        updateTamingProcess();
        if (!HologramHandler.updateHologram(hologram, getHologramText()))
            this.cancel();
    }

    @Override
    public boolean isThreadCurrentlyRunning() {
        return threadCurrentlyRunning;
    }

    /**
     * Stops thread and despawns the hologram.
     */
    @Override
    public void cancel() {
        HologramHandler.despawnHologram(hologram);
        mseEntity.setCustomName(mseEntity.getDefaultName());
        threadCurrentlyRunning = false;
        super.cancel();
    }

    @Override
    public long getTimeUntilWakeUp() {
        return (mseEntity.getTorpidity() / mseEntity.getTorporDepletion()) * unconsciousnessUpdateInterval;
    }

    /**
     * Updates the progress if the entity is still tameable until it is tamed.
     */
    private void updateTamingProcess() {

        if (!mseEntity.isTameable())
            return;

        int tamingIncrease = mseEntity.updateHunger();
        if (tamingIncrease > 0) {
            mseEntity.eatAnimation();
        }

        int tamingProgress = tamingHandler.getTamingProgress();
        tamingProgress += tamingIncrease;
        tamingHandler.setTamingProgress(tamingProgress);
        if (tamingProgress < 0)
            tamingProgress = 0;
        if (tamingProgress >= mseEntity.getMaxTamingProgress())
            tamingHandler.setSuccessfullyTamed();

    }

    private String[] getHologramText() {
        return new String[]{
                ChatColor.DARK_AQUA + mseEntity.getDefaultName(),
                ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                ChatColor.DARK_PURPLE + BarHandler.getProgressBar(mseEntity.getTorpidity(), mseEntity.getMaxTorpidity()),
                ChatColor.GOLD + BarHandler.getProgressBar(tamingHandler.getTamingProgress(), mseEntity.getMaxTamingProgress())
        };
    }

}