package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayer;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles unconscious state for players
 */
public class UnconsciousnessTimerHuman extends BukkitRunnable implements UnconsciousnessTimer {

    private MSEPlayer msePlayer;
    private Player player;
    private EntityAttributes entityAttributes;
    private boolean threadCurrentlyRunning = false;

    public UnconsciousnessTimerHuman(Object player) {
        if (player instanceof Player)
            this.player = (Player) player;
        else
            throw new IllegalArgumentException("Instance was not an entityPlayer as expected");
        this.msePlayer = MSEPlayerMap.getPlayerRegistry().getMSEPlayer(this.player);
        entityAttributes = msePlayer.getEntityAttributes();
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
    }

    @Override
    public void run() {
        threadCurrentlyRunning = true;
        if (player.isDead()) {
            this.cancel();
            return;
        }

        if (!player.hasPotionEffect(PotionEffectType.BLINDNESS))
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
        msePlayer.decreaseTorpidityBy(entityAttributes.getTorporDepletion());
    }

    @Override
    public void cancel() {
        threadCurrentlyRunning = false;
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        super.cancel();
    }

    @Override
    public boolean isThreadCurrentlyRunning() {
        return threadCurrentlyRunning;
    }

    @Override
    public long getTimeUntilWakeUp() {
        return (entityAttributes.getTorpidity() / entityAttributes.getTorporDepletion()) * unconsciousnessUpdateInterval;
    }

}
