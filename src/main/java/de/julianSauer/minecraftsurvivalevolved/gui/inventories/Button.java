package de.juliansauer.minecraftsurvivalevolved.gui.inventories;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a button in an inventory. Consists of an icon displayed by an ItemStack and an action when the button is
 * pressed.
 */
interface Button {
    ItemStack getButton();

    default void onClick(Player player) {

    }

    default void onClick(Player player, MSEEntity mseEntity) {

    }
}