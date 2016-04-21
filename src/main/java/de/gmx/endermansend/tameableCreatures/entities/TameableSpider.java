package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntitySpider;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class TameableSpider extends EntitySpider implements Tameable {

    private TamingAttributes tamingAttributes;

    public TameableSpider(World world) {
        super(world);
        tamingAttributes = new TamingAttributes();
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

        try {
            Method b = EntitySpider.class.getDeclaredMethod("b", float.class, float.class);
            b.setAccessible(true);
            b.invoke(this.yaw, this.pitch);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }

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

    public boolean isTamed() {
        return tamingAttributes.isTamed();
    }

    public boolean isTameable() {
        return tamingAttributes.isTameable();
    }

    public boolean isUnconscious() {
        return tamingAttributes.isUnconscious();
    }

    public int getTorpidity() {
        return tamingAttributes.getTorpidity();
    }

    public UUID getOwner() {
        return tamingAttributes.getOwner();
    }

    public void increaseTorpidityBy(int torpidityIncrease) {
        tamingAttributes.increaseTorpidityBy(torpidityIncrease);
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        tamingAttributes.decreaseTorpidityBy(torpidityDecrease);
    }

    public boolean setSuccessfullyTamed(UUID newOwner) {
        return tamingAttributes.setSuccessfullyTamed(newOwner);
    }

}
