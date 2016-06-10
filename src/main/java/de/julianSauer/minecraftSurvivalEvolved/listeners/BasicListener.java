package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

public class BasicListener implements Listener {

    public Tameable getTameableEntityFromEntity(Entity entity) {
        if (entity instanceof CraftEntity)
            if (((CraftEntity) entity).getHandle() instanceof Tameable)
                return (Tameable) ((CraftEntity) entity).getHandle();
        return null;
    }

    public Tameable getTameableEntityFromVehicle(Entity entity) {
        if (entity.getVehicle() != null)
            return getTameableEntityFromEntity(entity.getVehicle());
        return null;
    }

}
