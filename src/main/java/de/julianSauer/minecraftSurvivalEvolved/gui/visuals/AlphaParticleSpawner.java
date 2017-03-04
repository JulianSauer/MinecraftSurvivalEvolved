package de.julianSauer.minecraftSurvivalEvolved.gui.visuals;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class AlphaParticleSpawner {

    private final MSEMain instance;

    private final LivingEntity entity;

    private ParticleEffects[] particleEffects;

    public AlphaParticleSpawner(LivingEntity entity) {
        instance = MSEMain.getInstance();
        this.entity = entity;
    }

    /**
     * Starts a new thread adding particle effects to the entity.
     */
    public void startEffects() {
        int particles = 7;
        particleEffects = new ParticleEffects[particles];
        for (int i = 0; i < particles; i++) {
            particleEffects[i] = new ParticleEffects();
            particleEffects[i].runTaskTimer(instance, 0L, i * 2L);
        }

    }

    /**
     * Stops the thread that creates the particles.
     */
    public void stopEffects() {
        if (particleEffects != null) {
            for (ParticleEffects particleEffect : particleEffects)
                particleEffect.cancel();
        }
    }

    private void spawnParticleEffects() {

        World world = entity.getWorld();
        Location location = entity.getLocation();
        world.spawnParticle(Particle.SPELL_WITCH, location, 1);
        world.spawnParticle(Particle.SPELL_WITCH, location.add(0, 1, 0), 1);

    }

    /**
     * Adds an effect to an entity if it's not dead.
     */
    private class ParticleEffects extends BukkitRunnable {

        public void run() {
            if (entity.isDead())
                this.cancel();
            else
                spawnParticleEffects();
        }
    }

}
