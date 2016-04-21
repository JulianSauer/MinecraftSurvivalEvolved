package de.gmx.endermansend.tameableCreatures.entities;

import java.util.UUID;

/**
 * Implements generic functionality for taming that can be used to implement Tameable.
 */
public class TamingAttributes {

    private boolean tamed;
    private boolean tameable;
    private boolean unconscious;

    private int torpidity;
    private int maxTorpidity;
    private int fortitude;

    private UUID owner;

    public TamingAttributes() {
        tamed = false;
        tameable = true;
        unconscious = false;

        torpidity = 0;
        maxTorpidity = 100;
        fortitude = 20;
    }

    public TamingAttributes(boolean tameable) {
        this();
        this.tameable = tameable;
    }

    public TamingAttributes(int maxTorpidity, int fortitude) {
        this();
        this.maxTorpidity = maxTorpidity;
        this.fortitude = fortitude;
    }

    public TamingAttributes(int maxTorpidity, int fortitude, boolean tameable) {
        this(maxTorpidity, fortitude);
        this.tameable = tameable;
    }

    public boolean isTamed() {
        return tamed;
    }

    public boolean isTameable() {
        return tameable;
    }

    public boolean isUnconscious() {
        return unconscious;
    }

    public int getTorpidity() {
        return torpidity;
    }

    public UUID getOwner() {
        if (tamed)
            return owner;
        return null;
    }

    public void increaseTorpidityBy(int torpidityIncrease) {
        torpidity += torpidityIncrease;
        if (torpidity > maxTorpidity)
            torpidity = maxTorpidity;
        if (torpidity >= fortitude)
            unconscious = true;
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        torpidity -= torpidityDecrease;
        if (torpidity < 0)
            torpidity = 0;
        if (torpidity == 0)
            unconscious = false;
    }

    public boolean setSuccessfullyTamed(UUID newOwner) {

        if (!isTamed() && isTameable()) {
            tamed = true;
            tameable = false;
            owner = newOwner;
            decreaseTorpidityBy(maxTorpidity);
            return true;
        } else {
            return false;
        }

    }

}
