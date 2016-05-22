package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class TameableSpider extends EntitySpider implements Tameable, InventoryHolder {

    private TamingAttributes tamingAttributes;

    private Inventory inventory;

    public TameableSpider(World world) {
        super(world);
        tamingAttributes = new TamingAttributes(this, 100, 20, 1, 1, true);
        this.setCustomName("Tameable Spider");
    }

    @Override
    public void g(float sideMot, float forMot) {

        if (isUnconscious())
            return;

        if (this.passengers == null || this.passengers.size() != 1 || !(this.passengers.get(0) instanceof EntityPlayer)) {
            super.e(sideMot, forMot);
            this.P = 0.5F;
            return;
        }

        Entity passenger = this.passengers.get(0);

        this.lastYaw = this.yaw = passenger.yaw;
        this.pitch = passenger.pitch * 0.5F;

        this.setYawPitch(this.yaw, this.pitch);

        this.aO = this.aM = this.yaw;

        this.P = 1.0F;

        sideMot = ((EntityLiving) passenger).bd * 0.5F;
        forMot = ((EntityLiving) passenger).be;

        if (forMot <= 0.0F) {
            forMot *= 0.25F;
        }
        sideMot *= 0.75F;

        float speed = 0.35F;
        this.i(speed);
        super.g(sideMot, forMot);

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

    public int getMaxTorpidity() {
        return tamingAttributes.getMaxTorpidity();
    }

    public int getTamingProgress() {
        return tamingAttributes.getTamingProgress();
    }

    public int getMaxTamingProgress() {
        return tamingAttributes.getMaxTamingProgress();
    }

    public UUID getOwner() {
        return tamingAttributes.getOwner();
    }

    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        tamingAttributes.increaseTorpidityBy(torpidityIncrease, lastDamager);
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        tamingAttributes.decreaseTorpidityBy(torpidityDecrease);
    }

    public Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.createInventory(this, 18, this.getName());
        return inventory;
    }
}
