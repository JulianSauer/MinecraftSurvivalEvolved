package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MiningHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.PathFinderHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.entity.Player;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    TamingHandler getTamingHandler();

    MiningHandler getMiningHandler();

    PathFinderHandler getPathFinderHandler();

    default void forceTame(Player newOwner) {
        getTamingHandler().forceTame(newOwner);
    }

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

    default PathFinderHandler.EntityMode getEntityMode() {
        return getPathFinderHandler().getEntityMode();
    }

    void setPassiveGoals();

    void setNeutralGoals();

    void setAggressiveGoals();

    void setWandering(boolean wandering);

    void toggleFollowing(EntityPlayer player);

    EntityPlayer getFollowingPlayer();

    boolean isFollowing();

}
