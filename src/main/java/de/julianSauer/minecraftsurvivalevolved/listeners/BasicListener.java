package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

/**
 * Base for all listeners.
 */
public interface BasicListener {

    default MSEEntity getMSEEntityFromEntity(Entity entity) {
        if (entity instanceof CraftEntity)
            if (((CraftEntity) entity).getHandle() instanceof MSEEntity)
                return (MSEEntity) ((CraftEntity) entity).getHandle();
        return null;
    }

    default MSEEntity getMSEEntityFromVehicle(Entity entity) {
        if (entity.getVehicle() != null)
            return getMSEEntityFromEntity(entity.getVehicle());
        return null;
    }

}
