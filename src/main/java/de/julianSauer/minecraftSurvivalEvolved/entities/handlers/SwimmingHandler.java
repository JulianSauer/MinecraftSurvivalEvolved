package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.MathHelper;

public class SwimmingHandler<T extends EntityInsentient & MSEEntity> extends RidingHandler {

    public SwimmingHandler(T entity) {
        super(entity);
    }

    /**
     * Handles movement for swimming entities.
     *
     * @param args
     */
    @Override
    public void handleMovement(float[] args) {

        if (((MSEEntity) entity).getTamingHandler().isUnconscious())
            return;

        if (!isMounted()) {
            ((MSEEntity) entity).callSuperMovement(new float[]{});
            return;
        }

        float[] mot = calculateMovement();
        entity.motX = mot[0];
        entity.motY = mot[1];
        entity.motZ = mot[2];

        if (!entity.inWater) {
            entity.motX *= 0.1;
            entity.motY *= 0.1;
            entity.motZ *= 0.1;
        }

        entity.move(entity.motX, entity.motY, entity.motZ);

    }

    /**
     * Calculates correct yaw, pitch, speed and movement vector for this entity. Entity#setYawPitch(float f, float f1)
     * has to be called afterwards. The Entity#move(double d0, double d1, double d2) method of the entity has to be called with
     * the return value of this method.
     *
     * @return [0]: updated motX, [1]: updated motY, [2]: updated motZ
     */
    @Override
    protected float[] calculateMovement() {

        float[] mot = super.calculateMovement();
        float z = mot[0];
        float y = 0;
        float x = mot[1];

        mot = rotate(x, y, entity.pitch * 1.5F);
        x = mot[0];
        y = -mot[1];
        mot = rotate(x, -z, entity.yaw + 90);
        x = mot[0];
        z = mot[1];

        float speed = ((MSEEntity) entity).getEntityStats().getSpeed();
        return new float[]{
                x * speed,
                y * speed,
                z * speed
        };

    }

    private float[] rotate(float x, float y, float angle) {
        angle = toRadians(angle);
        return new float[]{
                x * MathHelper.cos(angle) - y * MathHelper.sin(angle),
                x * MathHelper.sin(angle) + y * MathHelper.cos(angle)
        };

    }

    private float toRadians(float angle) {
        if (entity.yaw < 0)
            return (float) Math.toRadians(angle + 360);
        else if (entity.yaw > 360)
            return (float) Math.toRadians(angle - 360);
        else
            return (float) Math.toRadians(angle);
    }

}
