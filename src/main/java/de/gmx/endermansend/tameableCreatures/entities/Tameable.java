package de.gmx.endermansend.tameableCreatures.entities;

import java.util.UUID;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    boolean isTamed();

    boolean isTameable();

    boolean isUnconscious();

    int getTorpidity();

    UUID getOwner();

    void increaseTorpidityBy(int torpidityIncrease);

    void decreaseTorpidityBy(int torpidityDecrease);

    boolean setSuccessfullyTamed(UUID newOwner);

}
