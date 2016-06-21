package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalRandomTarget;
import de.julianSauer.minecraftSurvivalEvolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.*;

import java.util.Set;

public class PathFinderHandlerCreature implements PathFinderHandler {

    private MSEEntity mseEntity;

    private Set goalB;
    private Set goalC;
    private Set targetB;
    private Set targetC;

    private EntityMode entityMode;
    private boolean wandering;

    public PathFinderHandlerCreature(MSEEntity mseEntity) {
        this.mseEntity = mseEntity;

        goalB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "b");
        goalC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "c");
        targetB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "b");
        targetC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "c");

        entityMode = EntityMode.PASSIVE;
        wandering = false;
    }

    @Override
    public EntityMode getEntityMode() {
        return entityMode;
    }

    @Override
    public void clearPathFinderGoals() {
        goalB.clear();
        goalC.clear();
        targetB.clear();
        targetC.clear();
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
        mseEntity.getTargetSelector().a(1, new PathfinderGoalHurtByTarget(entity, false, new Class[0]));
        if (wandering)
            mseEntity.getGoalSelector().a(5, new PathfinderGoalRandomStroll(entity, 0.8D));
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
        mseEntity.getTargetSelector().a(1, new PathfinderGoalRandomTarget(entity, EntityAnimal.class, false, object -> true));
    }

    @Override
    public void setWandering(boolean wandering) {
        this.wandering = wandering;
        updateGoals();
    }

}
