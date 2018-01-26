package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayer;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.UnconsciousPlayers;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.HologramHandler;
import de.julianSauer.minecraftSurvivalEvolved.listeners.PlayerMoveListener;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Handles unconscious state for players
 */
public class UnconsciousnessTimerHuman extends BukkitRunnable implements UnconsciousnessTimer {

    private MSEPlayer msePlayer;
    private Player player;
    private EntityAttributes entityAttributes;
    private boolean threadCurrentlyRunning = false;
    private UUID hologram;

    public UnconsciousnessTimerHuman(Object player) {
        if (player instanceof Player)
            this.player = (Player) player;
        else
            throw new IllegalArgumentException("Instance was not an entityPlayer as expected");
        this.msePlayer = MSEPlayerMap.getPlayerRegistry().getMSEPlayer(this.player);
        entityAttributes = msePlayer.getEntityAttributes();
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
        this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1, 1);
        hologram = HologramHandler.spawnHologramAt(this.player.getLocation().add(0, 0.93, 0),
                "Torpor: " + msePlayer.getEntityAttributes().getTorpidity() + "/" + msePlayer.getEntityAttributes().getMaxTorpidity());
        UnconsciousPlayers.addUnconsciousPlayer(this.player.getUniqueId());
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
        HologramHandler.updateHologram(hologram, "Torpor: " + msePlayer.getEntityAttributes().getTorpidity() + "/" + msePlayer.getEntityAttributes().getMaxTorpidity());
    }

    @Override
    public void cancel() {
        threadCurrentlyRunning = false;
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1, 1);
        UnconsciousPlayers.removeUnconsciousPlayer(player);
        HologramHandler.despawnHologram(hologram);
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
