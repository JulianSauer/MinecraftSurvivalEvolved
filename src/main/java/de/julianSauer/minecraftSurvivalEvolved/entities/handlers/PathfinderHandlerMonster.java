package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalFollowPlayer;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalRandomTarget;
import net.minecraft.server.v1_9_R1.*;

public class PathfinderHandlerMonster extends PathfinderHandlerAbstract {

    public PathfinderHandlerMonster(MSEEntity mseEntity) {
        super(mseEntity);
    }

    @Override
    public void setPassiveGoals() {
        entityMode = EntityMode.PASSIVE;
        clearPathFinderGoals();
        if (!(mseEntity instanceof EntityCreature))
            return;

        EntityCreature entity = (EntityCreature) mseEntity;
        mseEntity.getGoalSelector().a(0, new PathfinderGoalFloat(entity));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalLookAtPlayer(entity, EntityHuman.class, 8.0F));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalRandomLookaround(entity));
        mseEntity.getTargetSelector().a(0, new PathfinderGoalHurtByTarget(entity, false, new Class[0]));
        if (wandering && !following)
            mseEntity.getGoalSelector().a(5, new PathfinderGoalRandomStroll(entity, 0.8D));
        else if (following && followingPlayer != null)
            mseEntity.getGoalSelector().a(1, new PathfinderGoalFollowPlayer(mseEntity, followingPlayer));
    }

    @Override
    public void setNeutralGoals() {
        setPassiveGoals();
        entityMode = EntityMode.NEUTRAL;
        if (!(mseEntity instanceof EntityCreature))
            return;

        EntityCreature entity = (EntityCreature) mseEntity;
        mseEntity.getGoalSelector().a(4, mseEntity.getMeleeAttack());
        mseEntity.getGoalSelector().a(3, new PathfinderGoalLeapAtTarget(entity, 0.4F));
        mseEntity.getTargetSelector().a(0, new PathfinderGoalRandomTarget(entity, EntityMonster.class, false, getNeutralPredicate(mseEntity)));
    }

    @Override
    public void setAggressiveGoals() {
        setNeutralGoals();
        entityMode = EntityMode.AGGRESSIVE;
        if (!(mseEntity instanceof EntityCreature))
            return;

        EntityCreature entity = (EntityCreature) mseEntity;
        mseEntity.getTargetSelector().a(0, new PathfinderGoalRandomTarget(entity, EntityPlayer.class, false, getNeutralPredicate(mseEntity)));
        mseEntity.getTargetSelector().a(1, new PathfinderGoalRandomTarget(entity, EntityAnimal.class, false, input -> true));
    }

    @Override
    public void setDefaultGoals() {
        entityMode = EntityMode.DEFAULT;
        clearPathFinderGoals();
        if (!(mseEntity instanceof EntityCreature))
            return;

        EntityCreature entity = (EntityCreature) mseEntity;
        mseEntity.getGoalSelector().a(0, new PathfinderGoalFloat(entity));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalLookAtPlayer(entity, EntityHuman.class, 8.0F));
        mseEntity.getGoalSelector().a(6, new PathfinderGoalRandomLookaround(entity));
        mseEntity.getGoalSelector().a(4, mseEntity.getMeleeAttack());
        mseEntity.getGoalSelector().a(4, new PathfinderGoalRandomStroll(entity, 0.8D));
        mseEntity.getTargetSelector().a(0, new PathfinderGoalHurtByTarget(entity, false, new Class[0]));
        mseEntity.getTargetSelector().a(0, new PathfinderGoalRandomTarget(entity, EntityPlayer.class, false, input -> true));
        mseEntity.getTargetSelector().a(0, new PathfinderGoalRandomTarget(entity, EntityAnimal.class, false, getDefaultPredicate(mseEntity)));
    }

}
