package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.visuals.AlphaParticleSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Handles particle effects of alpha entities.
 * TODO: Add same effect on server startup.
 */
public class EntitySpawnListener extends BasicListener {


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {

        Entity entity = e.getEntity();
        MSEEntity mseEntity = getMSEEntityFromEntity(entity);
        if (mseEntity == null
                || !mseEntity.getEntityStats().isAlpha()
                || !(entity instanceof LivingEntity))
            return;

        (new AlphaParticleSpawner((LivingEntity) entity)).startEffects();

    }

}
