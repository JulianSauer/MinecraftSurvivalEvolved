package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.AttributeHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MovementHandlerInterface;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.RidingHandler;
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

    private MovementHandlerInterface movementHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    public TameableWolf(World world) {
        super(world);
        attributeHandler = new AttributeHandler(this);
        movementHandler = new RidingHandler(this);
        pitchWhileTaming = 0;
    }

    @Override
    public void g(float sideMot, float forMot) {

        movementHandler.handleMovement(new float[]{sideMot, forMot});

    }

    public void callSuperMovement(float[] args) {
        if (args.length == 2)
            super.g(args[0], args[1]);
    }

    public boolean tamed() {
        return attributeHandler.isTamed();
    }

    public boolean isTameable() {
        return attributeHandler.isTameable();
    }

    public boolean isAlpha() {
        return attributeHandler.isAlpha();
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

    public float getPitchWhileTaming() {
        return pitchWhileTaming;
    }

    public void setPitchWhileTaming(float pitch) {
        this.pitchWhileTaming = pitch;
    }

    public float getSpeed() {
        return attributeHandler.getSpeed();
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

    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager, String lastDamagerName) {
        attributeHandler.increaseTorpidityBy(torpidityIncrease, lastDamager, lastDamagerName);
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
