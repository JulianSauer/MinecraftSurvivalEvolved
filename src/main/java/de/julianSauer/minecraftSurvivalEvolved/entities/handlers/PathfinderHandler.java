package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import com.google.common.base.Predicate;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
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
     */
    void setWandering(boolean wandering);

    void toggleFollowing(EntityPlayer player);

    /**
     * Checks if the target should be attacked.
     *
     * @param mseEntity Attacking entity
     * @return True, if an attack is valid
     */
    default Predicate getNeutralPredicate(MSEEntity mseEntity) {
        return input -> {
            if (input instanceof EntityCreeper)
                return false;

            if (input instanceof MSEEntity) {
                MSEEntity entity = (MSEEntity) input;
                return !mseEntity.getEntityAttributes().sameOwner(entity);
            } else if (input instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) input;
                return !mseEntity.getEntityAttributes().isOwner(player.getUniqueID());
            }
            return true;
        };
    }

    default Predicate getDefaultPredicate(MSEEntity mseEntity) {
        return input -> !(input instanceof EntityMonster);
    }

}
