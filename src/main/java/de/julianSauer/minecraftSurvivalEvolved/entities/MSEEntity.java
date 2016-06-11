package de.julianSauer.minecraftSurvivalEvolved.entities;

import org.bukkit.inventory.InventoryHolder;

public interface MSEEntity extends Tameable, InventoryHolder {

    boolean isAlpha();

    int getLevel();

    float getSpeed();

    double getDamage();

    /**
     * @return [0]: Current xp [1]: xp until level up
     */
    float[] getXp();
}
