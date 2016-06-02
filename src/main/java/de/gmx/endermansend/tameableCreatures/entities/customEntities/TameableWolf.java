package de.gmx.endermansend.tameableCreatures.entities.customEntities;

import de.gmx.endermansend.tameableCreatures.entities.AttributeHandler;
import de.gmx.endermansend.tameableCreatures.entities.RidingHandler;
import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import net.minecraft.server.v1_9_R1.EntityWolf;
import net.minecraft.server.v1_9_R1.Material;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;
import java.util.UUID;

public class TameableWolf extends EntityWolf implements Tameable, InventoryHolder {

    private AttributeHandler attributeHandler;

    private RidingHandler ridingHandler;

    private Inventory inventory;

    public TameableWolf(World world) {
        super(world);
        attributeHandler = new AttributeHandler(this);
        ridingHandler = new RidingHandler(this);
    }

    @Override
    public void g(float sideMot, float forMot) {

        if (isUnconscious())
            return;

        if (!ridingHandler.isMounted()) {
            super.g(sideMot, forMot);
            return;
        }

        float[] temp = ridingHandler.calculateMovement(sideMot, forMot);
        sideMot = temp[0];
        forMot = temp[1];

        this.setYawPitch(this.yaw, this.pitch);
        super.g(sideMot, forMot);

        ridingHandler.jump();

    }

    public boolean tamed() {
        return attributeHandler.isTamed();
    }

    public boolean isTameable() {
        return attributeHandler.isTameable();
    }

    public boolean isUnconscious() {
        return attributeHandler.isUnconscious();
    }

    public int getTorpidity() {
        return attributeHandler.getTorpidity();
    }

    public int getMaxTorpidity() {
        return attributeHandler.getMaxTorpidity();
    }

    public int getTamingProgress() {
        return attributeHandler.getTamingProgress();
    }

    public int getMaxTamingProgress() {
        return attributeHandler.getMaxTamingProgress();
    }

    public int getLevel() {
        return attributeHandler.getLevel();
    }

    public UUID getOwners() {
        return attributeHandler.getOwner();
    }

    public void setName(String name) {
        attributeHandler.setName(name);
    }

    public double getDamage() {
        return attributeHandler.getDamage();
    }

    public float[] getXp() {
        return new float[]{attributeHandler.getCurrentXp(), attributeHandler.getXpUntilLevelUp()};
    }

    public List<Material> getPreferredFood() {
        return attributeHandler.getPreferredFood();
    }

    public List<Material> getMineableBlocks() {
        return attributeHandler.getMineableBlocks();
    }

    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        attributeHandler.increaseTorpidityBy(torpidityIncrease, lastDamager);
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        attributeHandler.decreaseTorpidityBy(torpidityDecrease);
    }

    public Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.createInventory(this, 18, this.getName());
        return inventory;
    }

}
