package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntitySpider;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.entity.Player;

public class TameableSpider extends EntitySpider {

    public TameableSpider(World world) {
        super(world);
    }

    @Override
    public void e(float sideMot, float forMot) {

        if (this.passengers == null || this.passengers.size() != 1 || !(this.passengers.get(0) instanceof Player)) {
            super.e(sideMot, forMot);
            // TODO: Find out what this.W has been renamed to so that npc entities can walk over half slabs
            return;
        }

        Entity passenger = this.passengers.get(0);

        this.lastYaw = this.yaw = passenger.yaw;
        this.pitch = passenger.pitch * 0.5F;

        this.b(this.yaw, this.pitch);
        this.aO = this.aM = this.yaw;

        sideMot = ((EntityLiving) passenger).bd * 0.5F;
        forMot = ((EntityLiving) passenger).be;

        if (forMot <= 0.0F) {
            forMot *= 0.25F;
        }
        sideMot *= 0.75F;

        float speed = 0.35F;
        this.i(speed);
        super.e(sideMot, forMot);

    }

    // Should actually be: https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/Entity.java#L163-L166
    protected void b(float f, float f1) {
        this.yaw = f % 360.0F;
        this.pitch = f1 % 360.0F;
    }

}
