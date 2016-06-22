package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import net.minecraft.server.v1_9_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public interface MSEEntity extends Tameable, InventoryHolder {

    EntityStats getEntityStats();

    String getEntityType();

    PathfinderGoalSelector getGoalSelector();

    PathfinderGoalSelector getTargetSelector();

    PathfinderGoalMeleeAttack getMeleeAttack();

    /**
     * Creates an org.bukkit.Location based on the entity's coordinates.
     *
     * @return Current location of this entity
     */
    Location getLocation();

    /**
     * Converts the entity into a Bukkit entity.
     *
     * @return NMS entity as CraftEntity
     */
    Entity getCraftEntity();

    // Methods implemented by Minecraft
    String getName();

    UUID getUniqueID();

    void setCustomName(String name);

}
