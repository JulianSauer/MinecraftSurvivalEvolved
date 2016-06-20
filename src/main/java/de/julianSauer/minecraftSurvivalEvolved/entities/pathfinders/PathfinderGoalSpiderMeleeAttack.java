package de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders;

import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntitySpider;
import net.minecraft.server.v1_9_R1.PathfinderGoalMeleeAttack;

public class PathfinderGoalSpiderMeleeAttack extends PathfinderGoalMeleeAttack {
    public PathfinderGoalSpiderMeleeAttack(EntitySpider entityspider) {
        super(entityspider, 1.0D, true);
    }

    public boolean b() {
        float f = this.b.e(1.0F);
        if (f >= 0.5F && this.b.getRandom().nextInt(100) == 0) {
            this.b.setGoalTarget(null);
            return false;
        } else {
            return super.b();
        }
    }

    protected double a(EntityLiving entityliving) {
        return (double) (4.0F + entityliving.width);
    }
}
