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

    int getMaxTorpidity();

    int getTamingProgress();

    int getMaxTamingProgress();

    UUID getOwner();

    void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager);

    void decreaseTorpidityBy(int torpidityDecrease);

}
