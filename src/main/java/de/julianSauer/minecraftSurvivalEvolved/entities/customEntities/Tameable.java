package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MiningHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    TamingHandler getTamingHandler();

    MiningHandler getMiningHandler();

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

    void setPassiveGoals();

    void setNeutralGoals();

    void setAggressiveGoals();

    void setWandering(boolean wandering);

}
