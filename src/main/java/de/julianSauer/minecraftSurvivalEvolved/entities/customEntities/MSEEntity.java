package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public interface MSEEntity extends Tameable, InventoryHolder {

    EntityStats getEntityStats();

    UUID getUniqueID();

}