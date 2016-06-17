package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a button in an inventory. Consists of an icon displayed by an ItemStack and an action when the button is
 * pressed.
 */
public interface Button {
    ItemStack getButton();

    void onClick(Player player, MSEEntity mseEntity);
}