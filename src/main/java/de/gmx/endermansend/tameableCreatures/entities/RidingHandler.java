package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntityPlayer;

import java.lang.reflect.Field;

public class RidingHandler<T extends EntityInsentient & Tameable> {

    T entity;
    Field jump = null;

    public RidingHandler(T entity) {
        this.entity = entity;

        try {
            jump = EntityLiving.class.getDeclaredField("bc");
            jump.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks if a player is currently riding the entity.
     *
     * @return True if entity is mounted by a player
     */
    public boolean isMounted() {
        if (entity.passengers == null || entity.passengers.size() != 1 || !(entity.passengers.get(0) instanceof EntityPlayer)) {
            if (!entity.isTamed())
                return false;
        }
        return true;
    }

    /**
     * Calculates correct yaw, pitch and speed for this entity. Entity#setYawPitch(float f, float f1) has to be called
     * afterwards. The super#g(float sideMot, float forMot) method of the entity has to be called with the return
     * value of this method.
     *
     * @param sideMot
     * @param forMot
     * @return [0]: updated sideMot, [1]: updated forMot
     */
    public float[] calculateMovement(float sideMot, float forMot) {
        Entity passenger = entity.passengers.get(0);

        entity.lastYaw = entity.yaw = passenger.yaw;
        entity.pitch = passenger.pitch * 0.5F;

        entity.aO = entity.aM = entity.yaw;

        entity.P = 1.0F;

        sideMot = ((EntityLiving) passenger).bd * 0.5F;
        forMot = ((EntityLiving) passenger).be;

        if (forMot <= 0.0F) {
            forMot *= 0.25F;
        }
        sideMot *= 0.75F;

        float speed = 0.35F;
        entity.i(speed);
        return new float[]{sideMot, forMot};
    }

    /**
     * Lets the entity jump if the passenger jumps.
     */
    public void jump() {

        if (jump != null && entity.onGround) {
            try {
                if (jump.getBoolean(entity.passengers.get(0))) {
                    double jumpHeight = 0.5D;
                    entity.motY = jumpHeight;
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

    }

}
