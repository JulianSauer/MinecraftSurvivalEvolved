package de.juliansauer.minecraftsurvivalevolved.entities.mseentities;

import de.juliansauer.minecraftsurvivalevolved.entities.UnconsciousnessTimer;
import de.juliansauer.minecraftsurvivalevolved.entities.UnconsciousnessTimerTameable;
import de.juliansauer.minecraftsurvivalevolved.entities.containers.TameableAttributesContainer;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.MiningHandler;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.PathfinderHandler;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.Persistentable;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.TamingHandler;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import de.juliansauer.minecraftsurvivalevolved.tribes.Tribe;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeRegistry;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Basic functionality of a tameable entity.
 */
public interface Tameable extends Unconsciousable, Persistentable {

    TameableAttributesContainer getTameableAttributesContainer();

    TamingHandler getTamingHandler();

    MiningHandler getMiningHandler();

    PathfinderHandler getPathfinderHandler();

    boolean isTameable();

    boolean isTamed();

    void setTamed(boolean tamed);

    int getMaxTamingProgress();

    UUID getMSEOwner();

    void setMSEOwner(UUID owner);

    /**
     * Checks if a player is the owner or a member of the owning tribe of this entity.
     *
     * @param player The possible owner
     * @return True if the player is an owner
     */
    default boolean isOwner(UUID player) {
        if (getMSEOwner() != null)
            return getMSEOwner().equals(player);
        else if (getTribe() != null) {
            Tribe owningTribe = TribeRegistry.getTribeRegistry().getTribe(getTribe());
            if (owningTribe == null)
                return false;
            return owningTribe.isMember(player);
        }
        return false;
    }

    /**
     * Checks if this one and another entity have the same owners.
     *
     * @param mseEntity The other entity
     * @return True if they have the same owners
     */
    default boolean sameOwner(MSEEntity mseEntity) {
        if (getMSEOwner() != null && mseEntity.getMSEOwner() != null)
            return getMSEOwner().equals(mseEntity.getMSEOwner());
        else if (getTribe() != null && mseEntity.getTribe() != null)
            return getTribe().equals(mseEntity.getTribe());

        return false;
    }

    UUID getTribe();

    void setTribe(UUID tribe);

    void startFoodTimer();

    default void forceTame(Player newOwner) {
        getTamingHandler().forceTame(newOwner);
    }

    void setPitchWhileTaming(float pitch);

    float getPitchWhileTaming();

    void callSuperMovement(float[] args);

    World getWorld();

    Location getLocation();

    default PathfinderHandler.EntityMode getEntityMode() {
        return getPathfinderHandler().getEntityMode();
    }

    default void setPassiveGoals() {
        getPathfinderHandler().setPassiveGoals();
    }

    default void setNeutralGoals() {
        getPathfinderHandler().setNeutralGoals();
    }

    default void setAggressiveGoals() {
        getPathfinderHandler().setAggressiveGoals();
    }

    default void setWandering(boolean wandering) {
        getPathfinderHandler().setWandering(wandering);
    }

    default void toggleFollowing(EntityPlayer player) {
        getPathfinderHandler().toggleFollowing(player);
    }

    default EntityPlayer getFollowingPlayer() {
        return getPathfinderHandler().getFollowingPlayer();
    }

    default boolean isFollowing() {
        return getPathfinderHandler().isFollowing();
    }

    @Override
    default void initWithDefaults() {
        getTameableAttributesContainer().initWithDefaults();
        getTamingHandler().initWithDefaults();
        getPathfinderHandler().initWithDefaults();
    }

    @Override
    default void initWith(NBTTagCompound data) {
        getTameableAttributesContainer().initWith(data);
        getTamingHandler().initWith(data);
        getPathfinderHandler().initWith(data);
        data.setBoolean("MSE" + this.getClass() + "Initialized", true);
    }

    @Override
    default void saveData(NBTTagCompound data) {
        getTameableAttributesContainer().saveData(data);
        getTamingHandler().saveData(data);
        getPathfinderHandler().saveData(data);
        data.setBoolean("MSE" + this.getClass() + "Initialized", true);
    }

    @Override
    default void eatAnimation() {
        setPitchWhileTaming(-30F);
        getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
        (new BukkitRunnable() {
            @Override
            public void run() {
                setPitchWhileTaming(0F);
                this.cancel();
            }
        }).runTaskTimerAsynchronously(MSEMain.getInstance(), 4L, 0L);
    }

    @Override
    default void updateConsciousness() {
        getTamingHandler().updateConsciousness();
    }

    @Override
    default UnconsciousnessTimerTameable getUnconsciousnessTimer() {
        return getTamingHandler().getUnconsciousnessTimerTameable();
    }

    @Override
    default void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer) {
        if (unconsciousnessTimer instanceof UnconsciousnessTimerTameable)
            getTamingHandler().setUnconsciousnessTimerTameable((UnconsciousnessTimerTameable) unconsciousnessTimer);
        else
            throw new IllegalArgumentException("Wrong type for timer");
    }

}
