package de.juliansauer.minecraftsurvivalevolved.entities.handlers;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;

import java.util.Set;

/**
 * Implements basic functionality for goal handling.
 */
public abstract class PathfinderHandlerAbstract implements PathfinderHandler {

    final MSEEntity mseEntity;

    private Set goalB;
    private Set goalC;
    private Set targetB;
    private Set targetC;

    EntityMode entityMode;
    boolean wandering;
    boolean following;
    EntityPlayer followingPlayer;

    private boolean initialized;

    PathfinderHandlerAbstract(MSEEntity mseEntity) {
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
        initWithPathfinderHandlerDefaults();
    }

    @Override
    public void initWith(NBTTagCompound data) {
        if (!data.getBoolean("MSE" + this.getClass() + "Initialized")) {
            initWithPathfinderHandlerDefaults();
            data.setBoolean("MSE" + this.getClass() + "Initialized", initialized);
            return;
        }

        entityMode = EntityMode.valueOf(data.getString("MSEEntityMode"));
        wandering = data.getBoolean("MSEWandering");
        updateGoals();
        initialized = true;
        data.setBoolean("MSE" + this.getClass() + "Initialized", initialized);
    }

    @Override
    public void saveData(NBTTagCompound data) {
        if (!isInitialized())
            initWithPathfinderHandlerDefaults();
        data.setString("MSEEntityMode", entityMode.toString());
        data.setBoolean("MSEWandering", wandering);
        data.setBoolean("MSE" + this.getClass() + "Initialized", initialized);
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

    /**
     * Prevents overriding of {@link PathfinderHandlerAbstract#initWithDefaults()}.
     */
    private void initWithPathfinderHandlerDefaults() {
        setDefaultGoals();
        wandering = false;
        initialized = true;
    }

}
