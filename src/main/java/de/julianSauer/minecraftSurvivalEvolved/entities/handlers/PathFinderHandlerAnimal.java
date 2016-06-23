package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalFollowPlayer;
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
    private boolean following;
    private EntityPlayer followingPlayer;

    private boolean initialized;

    public PathFinderHandlerAnimal(MSEEntity mseEntity) {
        this.mseEntity = mseEntity;
        initialized = false;

        goalB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "b");
        goalC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "c");
        targetB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "b");
        targetC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "c");

        following = false;
        followingPlayer = null;
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
    public EntityPlayer getFollowingPlayer() {
        return followingPlayer;
    }

    @Override
    public boolean isFollowing() {
        return following;
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

    @Override
    public void setWandering(boolean wandering) {
        this.wandering = wandering;
        updateGoals();
    }

    @Override
    public void toggleFollowing(EntityPlayer player) {
        if (following) {
            if (player.equals(followingPlayer)) {
                following = false;
                followingPlayer = null;
            } else {
                followingPlayer = player;
            }
        } else {
            if (player != null) {
                following = true;
                followingPlayer = player;
            } else {
                followingPlayer = null;
            }
        }
        updateGoals();
    }

}
