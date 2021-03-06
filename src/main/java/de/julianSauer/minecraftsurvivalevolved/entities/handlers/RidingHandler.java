package de.juliansauer.minecraftsurvivalevolved.entities.handlers;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntityPlayer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RidingHandler<T extends EntityInsentient & MSEEntity> implements MovementHandlerInterface {

    final T entity;
    private Field jump = null;
    private Method setYawPitch;

    private float lastViewingDirection;

    public RidingHandler(T entity) {
        this.entity = entity;
        lastViewingDirection = 0;

        // Accessing private fields of entities
        jump = ReflectionHelper.getPrivateVariable(EntityLiving.class, "bc");
        setYawPitch = ReflectionHelper.getPrivateMethod(Entity.class, "setYawPitch", float.class, float.class);
    }

    /**
     * Handles movement for walking entities.
     */
    public void handleMovement(float[] args) {

        if (!entity.getTameableAttributesContainer().isInitialized()) {
            entity.callSuperMovement(args);
            return;
        }

        if (entity.isUnconscious()) {

            // Prevent movement when unconscious
            entity.lastYaw = entity.yaw = 0;
            entity.pitch = entity.getPitchWhileTaming();
            entity.aO = entity.aM = lastViewingDirection;
            setYawPitch(entity.yaw, entity.pitch);
            entity.motX = 0;
            entity.motZ = 0;
            return;
        }

        lastViewingDirection = entity.aO;

        if (!isMounted()) {
            entity.l(entity.getSpeed() * 2);
            entity.callSuperMovement(args);
            return;
        }

        entity.l(entity.getSpeed());
        entity.callSuperMovement(calculateMovement());
        jump();
    }

    /**
     * Lets the entity jump if the passenger jumps.
     */
    private void jump() {

        if (jump != null && entity.onGround) {
            try {
                if (jump.getBoolean(entity.passengers.get(0)))
                    entity.motY = 0.5D;
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Checks if a player is currently riding the entity.
     *
     * @return True if entity is mounted by a player
     */
    boolean isMounted() {
        if (!entity.getTameableAttributesContainer().isInitialized())
            return false;

        return !(entity.isAlpha()
                || entity.passengers == null
                || entity.passengers.size() < 1
                || !(entity.passengers.get(0) instanceof EntityPlayer)
        );
    }

    /**
     * Sets the yaw and pitch of an entity using reflection.
     *
     * @param f  Yaw
     * @param f1 Pitch
     */
    private void setYawPitch(float f, float f1) {
        try {
            setYawPitch.invoke(entity, f, f1);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates correct yaw, pitch and speed for this entity. Entity#setYawPitch(float f, float f1) has to be called
     * afterwards. The super#g(float sideMot, float forMot) method of the entity has to be called with the return
     * value of this method.
     *
     * @return [0]: updated sideMot, [1]: updated forMot
     */
    float[] calculateMovement() {

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

        setYawPitch(entity.yaw, entity.pitch);
        return new float[]{sideMot, forMot};

    }

}
