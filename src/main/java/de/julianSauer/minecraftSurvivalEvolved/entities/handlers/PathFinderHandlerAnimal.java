package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.*;

import java.util.Set;

public class PathFinderHandlerAnimal implements PathFinderHandler {

    private MSEEntity mseEntity;

    private Set goalB;
    private Set goalC;
    private Set targetB;
    private Set targetC;

    private EntityMode entityMode;
    private boolean wandering;

    public PathFinderHandlerAnimal(MSEEntity mseEntity) {
        this.mseEntity = mseEntity;

        goalB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "b");
        goalC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "c");
        targetB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "b");
        targetC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "c");

        entityMode = EntityMode.PASSIVE;
        wandering = false;
    }

    @Override
    public void clearPathFinderGoals() {
        goalB.clear();
        goalC.clear();
        targetB.clear();
        targetC.clear();
    }

    @Override
    public EntityMode getEntityMode() {
        return entityMode;
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
        if (wandering)
            mseEntity.getGoalSelector().a(5, new PathfinderGoalRandomStroll(entity, 0.8D));
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
    public void setWandering(boolean wandering) {
        this.wandering = wandering;
        updateGoals();
    }

}
