package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import com.google.common.base.Predicate;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityCreeper;
import net.minecraft.server.v1_9_R1.EntityMonster;
import net.minecraft.server.v1_9_R1.EntityPlayer;

/**
 * Controls entity behavior
 */
public interface PathfinderHandler extends Persistentable {

    enum EntityMode {
        DEFAULT,
        PASSIVE,
        NEUTRAL,
        AGGRESSIVE
    }

    EntityMode getEntityMode();

    EntityPlayer getFollowingPlayer();

    boolean isFollowing();

    void clearPathFinderGoals();

    /**
     * Updates the pathfinder goals of this entity. Considers EntityMode and wandering.
     */
    default void updateGoals() {
        switch (getEntityMode()) {
            case DEFAULT:
                setDefaultGoals();
                break;
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
     * Enables the normal goals of the entity.
     */
    void setDefaultGoals();

    /**
     * Enables wandering for this entity.
     *
     * @param wandering
     */
    void setWandering(boolean wandering);

    void toggleFollowing(EntityPlayer player);

    default Predicate getNeutralPredicate(MSEEntity mseEntity) {
        return input -> {
            if (input instanceof EntityCreeper)
                return false;
            if (input instanceof MSEEntity) {
                MSEEntity entity = (MSEEntity) input;
                if (entity.getTamingHandler().getOwner() != null)
                    return !entity.getTamingHandler().getOwner().equals(mseEntity.getTamingHandler().getOwner());
            } else if (input instanceof EntityPlayer) {
                if (mseEntity.getTamingHandler() != null && mseEntity.getTamingHandler().getOwner() != null)
                    return !mseEntity.getTamingHandler().getOwner().equals(((Entity) input).getUniqueID());
            }
            return true;
        };
    }

    default Predicate getDefaultPredicate(MSEEntity mseEntity) {
        return input -> !(input instanceof EntityMonster);
    }

}
