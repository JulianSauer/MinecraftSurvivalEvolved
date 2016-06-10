package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import de.julianSauer.minecraftSurvivalEvolved.visuals.AlphaParticleSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener extends BasicListener {


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {

        // Adds particle effect to alpha entities
        Entity entity = e.getEntity();
        Tameable tameableEntity = getTameableEntityFromEntity(entity);
        if (tameableEntity == null
                || !tameableEntity.isAlpha()
                || !(entity instanceof LivingEntity))
            return;

        (new AlphaParticleSpawner((LivingEntity) entity)).startEffects();

    }

}
