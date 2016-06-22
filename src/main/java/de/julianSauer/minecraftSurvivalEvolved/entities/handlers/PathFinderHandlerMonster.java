package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalRandomTarget;
import de.julianSauer.minecraftSurvivalEvolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.*;

import java.util.Set;

public class PathFinderHandlerMonster implements PathFinderHandler {

    private MSEEntity mseEntity;

    private Set goalB;
    private Set goalC;
    private Set targetB;
    private Set targetC;

    private EntityMode entityMode;
    private boolean wandering;

    private boolean initialized;

    public PathFinderHandlerMonster(MSEEntity mseEntity) {
        this.mseEntity = mseEntity;
        initialized = false;

        goalB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "b");
        goalC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "c");
        targetB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "b");
        targetC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "c");

    }

    @Override
    public void initWithDefaults() {
        initialized = true;
        setDefaultGoals();
        wandering = false;
    }

    @Override
    public void initWith(NBTTagCompound data) {
        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        initialized = true;
        entityMode = EntityMode.valueOf(data.getString("MSEEntityMode"));
        wandering = data.getBoolean("MSEWandering");
        updateGoals();
    }

    @Override
    public void saveData(NBTTagCompound data) {
        if (!isInitialized())
            initWithDefaults();

        data.setString("MSEEntityMode", entityMode.toString());
        data.setBoolean("MSEWandering", wandering);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
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
        mseEntity.getTargetSelector().a(0, new PathfinderGoalHurtByTarget(entity, false, new Class[0]));
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

    @Override
    public void setWandering(boolean wandering) {
        this.wandering = wandering;
        updateGoals();
    }

}
