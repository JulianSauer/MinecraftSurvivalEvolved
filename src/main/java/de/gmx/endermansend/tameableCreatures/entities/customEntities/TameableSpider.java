package de.gmx.endermansend.tameableCreatures.entities.customEntities;

import de.gmx.endermansend.tameableCreatures.entities.EntityAttributes;
import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class TameableSpider extends EntitySpider implements Tameable, InventoryHolder {

    private EntityAttributes entityAttributes;

    private Inventory inventory;

    public TameableSpider(World world) {
        super(world);
        entityAttributes = new EntityAttributes(this, 100, 20, 1, 1, true);
    }

    @Override
    public void g(float sideMot, float forMot) {

        if (isUnconscious())
            return;

        if (this.passengers == null || this.passengers.size() != 1 || !(this.passengers.get(0) instanceof EntityPlayer)) {
            if(!this.isTamed())
                super.g(sideMot, forMot);
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

        Field jump = null;
        try {
            jump = EntityLiving.class.getDeclaredField("bc");
            jump.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        if(jump != null && this.onGround) {
            try {
                if(jump.getBoolean(passenger)) {
                    double jumpHeight = 0.5D;
                    this.motY = jumpHeight;
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

    }

    public boolean isTamed() {
        return entityAttributes.isTamed();
    }

    public boolean isTameable() {
        return entityAttributes.isTameable();
    }

    public boolean isUnconscious() {
        return entityAttributes.isUnconscious();
    }

    public int getTorpidity() {
        return entityAttributes.getTorpidity();
    }

    public int getMaxTorpidity() {
        return entityAttributes.getMaxTorpidity();
    }

    public int getTamingProgress() {
        return entityAttributes.getTamingProgress();
    }

    public int getMaxTamingProgress() {
        return entityAttributes.getMaxTamingProgress();
    }

    public int getLevel() {
        return entityAttributes.getLevel();
    }

    public UUID getOwner() {
        return entityAttributes.getOwner();
    }

    public void setName(String name) {
        entityAttributes.setName(name);
    }

    public double getDamage() {
        return entityAttributes.getDamage();
    }

    public float[] getXp() {
        return new float[]{entityAttributes.getCurrentXp(), entityAttributes.getXpUntilLevelUp()};
    }

    public List<Material> getPreferredFood() {
        return entityAttributes.getPreferredFood();
    }

    public List<Material> getMineableBlocks() {
        return entityAttributes.getMineableBlocks();
    }

    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        entityAttributes.increaseTorpidityBy(torpidityIncrease, lastDamager);
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        entityAttributes.decreaseTorpidityBy(torpidityDecrease);
    }

    public Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.createInventory(this, 18, this.getName());
        return inventory;
    }
}
