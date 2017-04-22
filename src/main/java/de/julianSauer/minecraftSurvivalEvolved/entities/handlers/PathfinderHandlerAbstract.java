package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;

import java.util.Set;

/**
 * Implements basic functionality for goal handling.
 */
public abstract class PathfinderHandlerAbstract implements PathfinderHandler {

    protected final MSEEntity mseEntity;

    private Set goalB;
    private Set goalC;
    private Set targetB;
    private Set targetC;

    protected EntityMode entityMode;
    protected boolean wandering;
    protected boolean following;
    protected EntityPlayer followingPlayer;

    private boolean initialized;

    public PathfinderHandlerAbstract(MSEEntity mseEntity) {
        this.mseEntity = mseEntity;

        goalB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "b");
        goalC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getGoalSelector(), "c");
        targetB = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "b");
        targetC = (Set) ReflectionHelper.getPrivateVariableValue(PathfinderGoalSelector.class, mseEntity.getTargetSelector(), "c");

        following = false;
        followingPlayer = null;
    }

    @Override
    public void initWithDefaults() {
        setDefaultGoals();
        wandering = false;
        initialized = true;
    }

    @Override
    public void initWith(NBTTagCompound data) {
        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        entityMode = EntityMode.valueOf(data.getString("MSEEntityMode"));
        wandering = data.getBoolean("MSEWandering");
        updateGoals();
        initialized = true;
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
