package de.gmx.endermansend.tameableCreatures.entities.customEntities;

import de.gmx.endermansend.tameableCreatures.entities.EntityAttributes;
import de.gmx.endermansend.tameableCreatures.entities.RidingHandler;
import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import net.minecraft.server.v1_9_R1.EntityCaveSpider;
import net.minecraft.server.v1_9_R1.Material;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;
import java.util.UUID;

public class TameableCaveSpider extends EntityCaveSpider implements Tameable, InventoryHolder {

    private EntityAttributes entityAttributes;

    private RidingHandler ridingHandler;

    private Inventory inventory;

    public TameableCaveSpider(World world) {
        super(world);
        entityAttributes = new EntityAttributes(this, 100, 20, 1, 1, true);
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
