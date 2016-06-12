package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    TamingHandler getTamingHandler();

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

}
