package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MiningHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import org.bukkit.entity.Player;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    TamingHandler getTamingHandler();

    MiningHandler getMiningHandler();

    default void forceTame(Player newOwner) {
        getTamingHandler().forceTame(newOwner);
    }

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

    void setPassiveGoals();

    void setNeutralGoals();

    void setAggressiveGoals();

    void setWandering(boolean wandering);

}
