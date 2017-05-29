package de.julianSauer.minecraftSurvivalEvolved.entities;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Handles unconscious state for entities. Predefines methods of Runnable.
 */
public interface UnconsciousnessTimer {

    long unconsciousnessUpdateInterval = 100L;

    void run(); // BukkitRunnable

    void cancel(); // BukkitRunnable

    BukkitTask runTaskTimer(Plugin plugin, long delay, long period); // BukkitRunnable

    default long getUnconsciousnessUpdateInterval() {
        return unconsciousnessUpdateInterval;
    }

    boolean isThreadCurrentlyRunning();

    /**
     * Returns the game ticks until the entity becomes conscious again.
     *
     * @return Remaining time in game ticks
     */
    long getTimeUntilWakeUp();

}
