package de.julianSauer.minecraftSurvivalEvolved.entities;

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

    UUID getOwners();

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    List<Material> getPreferredFood();

    List<Material> getMineableBlocks();

    void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager, String lastDamagerName);

    void decreaseTorpidityBy(int torpidityDecrease);

    void callSuperMovement(float[] args);

}
