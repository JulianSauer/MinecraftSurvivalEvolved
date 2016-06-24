package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

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

    void setPassiveGoals();

    void setNeutralGoals();

    void setAggressiveGoals();

    void setWandering(boolean wandering);

    void toggleFollowing(EntityPlayer player);

    EntityPlayer getFollowingPlayer();

    boolean isFollowing();

    /**
     * Loads data for this entity.
     *
     * @param data NBTTags used for loading
     */
    default void load(NBTTagCompound data) {
        getTamingHandler().initWith(data);
        getPathfinderHandler().initWith(data);
    }

    /**
     * Saves data for this entity.
     *
     * @param data NBTTags used for saving
     */
    default void save(NBTTagCompound data) {
        if (!getTamingHandler().isInitialized())
            getTamingHandler().initWithDefaults();
        if (!getPathfinderHandler().isInitialized())
            getPathfinderHandler().initWithDefaults();
        getTamingHandler().saveData(data);
        getPathfinderHandler().saveData(data);
        data.setBoolean("MSEInitialized", true);
    }

}
