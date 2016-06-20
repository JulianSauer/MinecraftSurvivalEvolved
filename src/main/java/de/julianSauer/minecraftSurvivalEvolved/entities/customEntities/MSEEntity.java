package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import net.minecraft.server.v1_9_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public interface MSEEntity extends Tameable, InventoryHolder {

    String getName();

    void setCustomName(String name);

    EntityStats getEntityStats();

    UUID getUniqueID();

    PathfinderGoalSelector getGoalSelector();

    PathfinderGoalSelector getTargetSelector();

    PathfinderGoalMeleeAttack getMeleeAttack();

}
