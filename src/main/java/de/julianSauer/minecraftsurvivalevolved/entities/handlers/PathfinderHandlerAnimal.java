package de.juliansauer.minecraftsurvivalevolved.entities.handlers;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.pathfinders.PathfinderGoalFollowPlayer;
import net.minecraft.server.v1_9_R1.*;

public class PathfinderHandlerAnimal extends PathfinderHandlerAbstract {

    public PathfinderHandlerAnimal(MSEEntity mseEntity) {
        super(mseEntity);
    }

    @Override
    public void setPassiveGoals() {
        clearPathFinderGoals();
        entityMode = EntityMode.PASSIVE;
        if (!(mseEntity instanceof EntityAnimal))
            return;

        EntityAnimal entity = (EntityAnimal) mseEntity;
        mseEntity.getGoalSelector().a(0, new PathfinderGoalFloat(entity));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalLookAtPlayer(entity, EntityHuman.class, 8.0F));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalRandomLookaround(entity));
        if (wandering && !following)
            mseEntity.getGoalSelector().a(5, new PathfinderGoalRandomStroll(entity, 0.8D));
        else if (following && followingPlayer != null)
            mseEntity.getGoalSelector().a(1, new PathfinderGoalFollowPlayer(mseEntity, followingPlayer));
    }

    @Override
    public void setNeutralGoals() {
        setPassiveGoals();
        entityMode = EntityMode.NEUTRAL;
    }

    @Override
    public void setAggressiveGoals() {
        setNeutralGoals();
        entityMode = EntityMode.AGGRESSIVE;
    }

    @Override
    public void setDefaultGoals() {
        entityMode = EntityMode.DEFAULT;
        clearPathFinderGoals();
        if (!(mseEntity instanceof EntityAnimal))
            return;

        EntityAnimal entity = (EntityAnimal) mseEntity;
        mseEntity.getGoalSelector().a(0, new PathfinderGoalFloat(entity));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalLookAtPlayer(entity, EntityHuman.class, 8.0F));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalRandomLookaround(entity));
        mseEntity.getTargetSelector().a(1, new PathfinderGoalHurtByTarget(entity, false, new Class[0]));
        mseEntity.getGoalSelector().a(3, new PathfinderGoalRandomStroll(entity, 0.8D));
    }

}
