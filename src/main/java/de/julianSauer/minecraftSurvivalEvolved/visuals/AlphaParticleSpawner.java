package de.julianSauer.minecraftSurvivalEvolved.visuals;

import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class AlphaParticleSpawner {

    private ThisPlugin instance;

    private LivingEntity entity;

    private ParticleEffects[] particleEffects;

    public AlphaParticleSpawner(LivingEntity entity) {
        instance = ThisPlugin.getInstance();
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
        Location location = entity.getLocation(); // Possible FX: DRAGON_BREATH, CLOUD, CRIT, EXPLOSION_LARGE, EXPLOSION_HUGE, FLAME & LAVA, SPELL_WITCH
        world.spawnParticle(Particle.SPELL_WITCH, location, 1);
        world.spawnParticle(Particle.SPELL_WITCH, location.add(0, 1, 0), 1);

    }

    /**
     * Adds an effect to an entity if it's not dead.
     */
    class ParticleEffects extends BukkitRunnable {

        public void run() {
            if (entity.isDead())
                this.cancel();
            else
                spawnParticleEffects();
        }
    }

}
