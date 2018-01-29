package de.juliansauer.minecraftsurvivalevolved.entities.pathfinders;

import com.google.common.base.Predicate;
import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.PathfinderGoalNearestAttackableTarget;

public class PathfinderGoalRandomTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T> {

    public PathfinderGoalRandomTarget(EntityCreature entity, Class<T> var2, boolean var3, Predicate<? super T> var4) {
        super(entity, var2, 10, var3, false, var4);
    }

}
