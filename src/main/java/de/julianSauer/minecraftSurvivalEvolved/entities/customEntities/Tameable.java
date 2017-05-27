package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MiningHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.PathfinderHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.entity.Player;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable {

    EntityAttributes getEntityAttributes();

    TamingHandler getTamingHandler();

    MiningHandler getMiningHandler();

    PathfinderHandler getPathfinderHandler();

    default void forceTame(Player newOwner) {
        getTamingHandler().forceTame(newOwner);
    }

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

    default PathfinderHandler.EntityMode getEntityMode() {
        return getPathfinderHandler().getEntityMode();
    }

    default void setPassiveGoals() {
        getPathfinderHandler().setPassiveGoals();
    }

    default void setNeutralGoals() {
        getPathfinderHandler().setNeutralGoals();
    }

    default void setAggressiveGoals() {
        getPathfinderHandler().setAggressiveGoals();
    }

    default void setWandering(boolean wandering) {
        getPathfinderHandler().setWandering(wandering);
    }

    default void toggleFollowing(EntityPlayer player) {
        getPathfinderHandler().toggleFollowing(player);
    }

    default EntityPlayer getFollowingPlayer() {
        return getPathfinderHandler().getFollowingPlayer();
    }

    default boolean isFollowing() {
        return getPathfinderHandler().isFollowing();
    }

    /**
     * Loads data for this entity.
     *
     * @param data NBTTags used for loading
     */
    default void load(NBTTagCompound data) {
        getEntityAttributes().initWith(data);
        getTamingHandler().initWith(data);
        getPathfinderHandler().initWith(data);
    }

    /**
     * Saves data for this entity.
     *
     * @param data NBTTags used for saving
     */
    default void save(NBTTagCompound data) {
        getEntityAttributes().saveData(data);
        getTamingHandler().saveData(data);
        getPathfinderHandler().saveData(data);
        data.setBoolean("MSEInitialized", true);
    }

}
