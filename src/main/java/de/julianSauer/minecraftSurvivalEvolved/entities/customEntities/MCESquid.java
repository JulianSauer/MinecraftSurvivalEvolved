package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.AttributeHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MovementHandlerInterface;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.SwimmingHandler;
import net.minecraft.server.v1_9_R1.EntitySquid;
import net.minecraft.server.v1_9_R1.Material;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

public class MCESquid extends EntitySquid implements MSEEntity {

    private AttributeHandler attributeHandler;

    private MovementHandlerInterface movementHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    public MCESquid(World world) {
        super(world);
        attributeHandler = new AttributeHandler(this);
        movementHandler = new SwimmingHandler(this);
        pitchWhileTaming = 0;
    }

    @Override
    public void n() {

        movementHandler.handleMovement(new float[]{});

    }

    public void callSuperMovement(float[] args) {
        super.n();
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