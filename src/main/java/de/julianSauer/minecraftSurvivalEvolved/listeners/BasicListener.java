package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

public class BasicListener implements Listener {

    public MSEEntity getMSEEntityFromEntity(Entity entity) {
        if (entity instanceof CraftEntity)
            if (((CraftEntity) entity).getHandle() instanceof MSEEntity)
                return (MSEEntity) ((CraftEntity) entity).getHandle();
        return null;
    }

    public MSEEntity getMSEEntityFromVehicle(Entity entity) {
        if (entity.getVehicle() != null)
            return getMSEEntityFromEntity(entity.getVehicle());
        return null;
    }

}
