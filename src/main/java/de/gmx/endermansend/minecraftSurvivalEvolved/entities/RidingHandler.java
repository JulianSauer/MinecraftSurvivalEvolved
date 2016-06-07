package de.gmx.endermansend.minecraftSurvivalEvolved.entities;

import net.minecraft.server.v1_9_R1.*;

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
            if (!entity.tamed())
                return false;
        }
        return true;
    }

    /**
     * Calculates correct yaw, pitch and speed for this entity. Entity#setYawPitch(float f, float f1) has to be called
     * afterwards. The super#g(float sideMot, float forMot) method of the entity has to be called with the return
     * value of this method.
     *
     * @return [0]: updated sideMot, [1]: updated forMot
     */
    public float[] calculateMovement() {

        float sideMot, forMot;
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

        float speed = entity.getSpeed();
        entity.l(speed);
        return new float[]{sideMot, forMot};
    }

    /**
     * Calculates correct yaw, pitch, speed and movement vector for this entity. Entity#setYawPitch(float f, float f1)
     * has to be called afterwards. The Entity#move(double d0, double d1, double d2) method of the entity has to be called with
     * the return value of this method.
     *
     * @return [0]: updated motX, [1]: updated motY, [2]: updated motZ
     */
    public float[] calculateSwimming() {

        float[] mot = calculateMovement();
        float z = mot[0];
        float y = 0;
        float x = mot[1];

        mot = rotate(x, y, entity.pitch * 1.5F);
        x = mot[0];
        y = -mot[1];
        mot = rotate(x, -z, entity.yaw + 90);
        x = mot[0];
        z = mot[1];

        float speed = entity.getSpeed();
        return new float[]{
                x * speed,
                y * speed,
                z * speed
        };
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

    private float toRadians(float angle) {
        if (entity.yaw < 0)
            return (float) Math.toRadians(angle + 360);
        else if (entity.yaw > 360)
            return (float) Math.toRadians(angle - 360);
        else
            return (float) Math.toRadians(angle);
    }

    private float[] rotate(float x, float y, float angle) {
        angle = toRadians(angle);
        return new float[]{
                x * MathHelper.cos(angle) - y * MathHelper.sin(angle),
                x * MathHelper.sin(angle) + y * MathHelper.cos(angle)
        };

    }

}
