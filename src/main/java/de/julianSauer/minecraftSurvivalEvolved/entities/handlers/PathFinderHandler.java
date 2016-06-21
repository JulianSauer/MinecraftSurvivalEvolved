package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import com.google.common.base.Predicate;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityCreeper;
import net.minecraft.server.v1_9_R1.EntityPlayer;

/**
 * Controls entity behavior
 */
public interface PathFinderHandler {

    enum EntityMode {
        PASSIVE,
        NEUTRAL,
        AGGRESSIVE
    }

    EntityMode getEntityMode();

    void clearPathFinderGoals();

    /**
     * Updates the pathfinder goals of this entity. Considers EntityMode and wandering.
     */
    default void updateGoals() {
        switch (getEntityMode()) {
            case PASSIVE:
                setPassiveGoals();
                break;
            case NEUTRAL:
                setNeutralGoals();
                break;
            case AGGRESSIVE:
                setAggressiveGoals();
                break;
        }
    }

    /**
     * Disables movement and attacking of other entities.
     */
    void setPassiveGoals();

    /**
     * Enables attacking of hostile entities.
     */
    void setNeutralGoals();

    /**
     * Enables attacking of all entities.
     */
    void setAggressiveGoals();

    /**
     * Enables wandering for this entity.
     *
     * @param wandering
     */
    void setWandering(boolean wandering);

    default Predicate getNeutralPredicate(MSEEntity mseEntity) {
        return object -> {
            if (object instanceof EntityCreeper)
                return false;
            if (object instanceof MSEEntity) {
                MSEEntity entity = (MSEEntity) object;
                if (entity.getTamingHandler().getOwner() != null)
                    return !entity.getTamingHandler().getOwner().equals(mseEntity.getTamingHandler().getOwner());
            } else if (object instanceof EntityPlayer) {
                if (mseEntity.getTamingHandler() != null)
                    return !mseEntity.getTamingHandler().getOwner().equals(((Entity) object).getUniqueID());
            }
            return true;
        };
    }

}
