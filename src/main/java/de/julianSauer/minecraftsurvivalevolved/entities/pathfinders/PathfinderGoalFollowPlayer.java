package de.juliansauer.minecraftsurvivalevolved.entities.pathfinders;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PathfinderGoal;

/**
 * Lets the entity follow a player.
 */
public class PathfinderGoalFollowPlayer extends PathfinderGoal {

    private final MSEEntity mseEntity;
    private final EntityPlayer player;

    private final double speed;
    private final double distanceSquared;

    public PathfinderGoalFollowPlayer(MSEEntity mseEntity, EntityPlayer player) {
        this.mseEntity = mseEntity;
        this.player = player;
        this.speed = mseEntity.getSpeed() * 10;
        this.distanceSquared = 4.0;
        this.a(3);
    }

    @Override
    public boolean a() {
        return (player != null && player.isAlive() && mseEntity.getHandle().h(player) > distanceSquared);
    }

    @Override
    public void d() {
        ((EntityInsentient) mseEntity.getHandle()).getNavigation().n();
    }

    @Override
    public void e() {
        ((EntityInsentient) mseEntity.getHandle()).getControllerLook().a(player, 10.0F, 0F);
        ((EntityInsentient) mseEntity.getHandle()).getNavigation().a(player, speed);
    }

}
