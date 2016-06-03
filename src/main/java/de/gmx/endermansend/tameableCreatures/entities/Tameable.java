package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.v1_9_R1.Material;

import java.util.List;
import java.util.UUID;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    boolean tamed();

    boolean isTameable();

    boolean isUnconscious();

    int getTorpidity();

    int getMaxTorpidity();

    int getTamingProgress();

    int getMaxTamingProgress();

    int getLevel();

    UUID getOwners();

    String getName();

    void setName(String name);

    float getSpeed();

    double getDamage();

    /**
     * @return [0]: Current xp [1]: xp until level up
     */
    float[] getXp();

    List<Material> getPreferredFood();

    List<Material> getMineableBlocks();

    void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager);

    void decreaseTorpidityBy(int torpidityDecrease);

}
