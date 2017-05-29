package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.HologramHandler;
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
    private TameableEntityAttributes tameableEntityAttributes;
    private boolean threadCurrentlyRunning = false;

    public UnconsciousnessTimerTameable(TamingHandler tamingHandler, T mseEntity) {

        this.mseEntity = mseEntity;
        this.tamingHandler = tamingHandler;
        threadCurrentlyRunning = true; // TODO: Remove initialization
        tameableEntityAttributes = mseEntity.getTameableEntityAttributes();
        this.initialized = false;

        if (mseEntity.getWorld().isLoaded(mseEntity.getChunkCoordinates()))
            init();

    }

    private void init() {
        this.initialized = true;
        if (tameableEntityAttributes.isTamed() || tameableEntityAttributes.isAlpha())
            hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), ChatColor.RED + "Unconscious");
        else {
            tameableEntityAttributes.startFoodTimer();
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

        mseEntity.decreaseTorpidityBy(tameableEntityAttributes.getTorporDepletion());
        if (tameableEntityAttributes.isTamed() || tameableEntityAttributes.isAlpha())
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
        mseEntity.setCustomName(tameableEntityAttributes.getDefaultName());
        threadCurrentlyRunning = false;
        super.cancel();
    }

    @Override
    public long getTimeUntilWakeUp() {
        return (tameableEntityAttributes.getTorpidity() / tameableEntityAttributes.getTorporDepletion()) * unconsciousnessUpdateInterval;
    }

    /**
     * Updates the progress if the entity is still tameable until it is tamed.
     */
    private void updateTamingProcess() {

        if (!tameableEntityAttributes.isTameable())
            return;

        int tamingIncrease = tameableEntityAttributes.getFoodTimer().updateHunger();
        if (tamingIncrease > 0) {
            mseEntity.eatAnimation();
        }

        int tamingProgress = tamingHandler.getTamingProgress();
        tamingProgress += tamingIncrease;
        tamingHandler.setTamingProgress(tamingProgress);
        if (tamingProgress < 0)
            tamingProgress = 0;
        if (tamingProgress >= tameableEntityAttributes.getMaxTamingProgress())
            tamingHandler.setSuccessfullyTamed();

    }

    private String[] getHologramText() {
        return new String[]{
                ChatColor.DARK_AQUA + tameableEntityAttributes.getDefaultName(),
                ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                ChatColor.DARK_PURPLE + BarHandler.getProgressBar(tameableEntityAttributes.getTorpidity(), tameableEntityAttributes.getMaxTorpidity()),
                ChatColor.GOLD + BarHandler.getProgressBar(tamingHandler.getTamingProgress(), tameableEntityAttributes.getMaxTamingProgress())
        };
    }

}