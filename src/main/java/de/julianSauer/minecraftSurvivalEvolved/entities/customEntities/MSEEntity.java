package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.containers.TameableAttributesContainer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public interface MSEEntity extends Tameable, AttributedEntity {

    TameableAttributesContainer getTameableAttributesContainer();

    // AttributedEntity implementation

    @Override
    default AttributedEntity getAttributedEntity() {
        return this;
    }

    @Override
    default double getDamage() {
        return getTameableAttributesContainer().getDamage();
    }

    @Override
    default double getMaxDamage() {
        return getTameableAttributesContainer().getMaxDamage();
    }

    @Override
    default float getSpeed() {
        return getTameableAttributesContainer().getSpeed();
    }

    @Override
    default int getTorpidity() {
        return getTameableAttributesContainer().getTorpidity();
    }

    @Override
    default void setTorpidity(int torpidity) {
        getTameableAttributesContainer().setTorpidity(torpidity);
    }

    @Override
    default boolean isUnconscious() {
        return getTameableAttributesContainer().isUnconscious();
    }

    @Override
    default void setUnconscious(boolean unconscious) {
        getTameableAttributesContainer().setUnconscious(unconscious);
    }

    @Override
    default int getMaxTorpidity() {
        return getTameableAttributesContainer().getMaxTorpidity();
    }

    @Override
    default int getTorporDepletion() {
        return getTameableAttributesContainer().getTorporDepletion();
    }

    @Override
    default int getFortitude() {
        return getTameableAttributesContainer().getFortitude();
    }

    @Override
    default int getFood() {
        return getTameableAttributesContainer().getCurrentFoodValue();
    }

    @Override
    default void setFood(int food) {
        getTameableAttributesContainer().setCurrentFoodValue(food);
    }

    @Override
    default int getMaxFood() {
        return getTameableAttributesContainer().getMaxFoodValue();
    }

    @Override
    default int getFoodDepletion() {
        return getTameableAttributesContainer().getFoodDepletion();
    }

    @Override
    default int getLevel() {
        return getTameableAttributesContainer().getLevel();
    }

    @Override
    default float getLevelMultiplier() {
        return getTameableAttributesContainer().getMultiplier();
    }

    @Override
    default float getXpUntilLevelUp() {
        return getTameableAttributesContainer().getXpUntilLevelUp();
    }

    @Override
    default int getMaxLevel() {
        return getTameableAttributesContainer().getMaxLevel();
    }

    @Override
    default String getDefaultName() {
        return getTameableAttributesContainer().getDefaultName();
    }

    // Tameable implementation

    @Override
    default boolean isTameable() {
        return getTameableAttributesContainer().isTameable();
    }

    @Override
    default boolean isTamed() {
        return getTameableAttributesContainer().isTamed();
    }

    @Override
    default void setTamed(boolean tamed) {
        getTameableAttributesContainer().setTamed(tamed);
    }

    @Override
    default int getMaxTamingProgress() {
        return getTameableAttributesContainer().getMaxTamingProgress();
    }

    @Override
    default UUID getMSEOwner() {
        return getTameableAttributesContainer().getOwner();
    }

    @Override
    default void setMSEOwner(UUID owner) {
        getTameableAttributesContainer().setOwner(owner);
    }

    @Override
    default UUID getTribe() {
        return getTameableAttributesContainer().getTribe();
    }

    @Override
    default void setTribe(UUID tribe) {
        getTameableAttributesContainer().setTribe(tribe);
    }

    @Override
    default void startFoodTimer() {
        getTameableAttributesContainer().startFoodTimer();
    }

    default boolean isAlpha() {
        return getTameableAttributesContainer().isAlpha();
    }

    default int updateHunger() {
        return getTameableAttributesContainer().getFoodTimer().updateHunger();
    }

    PathfinderGoalSelector getGoalSelector();

    PathfinderGoalSelector getTargetSelector();

    PathfinderGoalMeleeAttack getMeleeAttack();

    /**
     * Creates an org.bukkit.Location based on the entity's coordinates.
     *
     * @return Current location of this entity
     */
    Location getLocation();

    /**
     * Converts the entity into a Bukkit entity.
     *
     * @return NMS entity as CraftEntity
     */
    default Entity getCraftEntity() {
        return CraftEntity.getEntity((CraftServer) Bukkit.getServer(), (net.minecraft.server.v1_9_R1.Entity) getEntity());
    }

    @Override
    default void load(NBTTagCompound data) {
        Tameable.super.load(data);
        setInventory(inventoryFromBase64String(data.getString("MSEInventory")));
    }

    @Override
    default void save(NBTTagCompound data) {
        Tameable.super.save(data);
        data.setBoolean("MSEInitialized", false);
        data.setString("MSEInventory", inventoryAsBase64String());
        data.setBoolean("MSEInitialized", true);
    }

    default void setInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++)
            getInventory().setItem(i, inventory.getItem(i));
    }

    // Methods implemented by Minecraft
    String getName();

    UUID getUniqueID();

    void setCustomName(String name);

    void playAttackSound();

    default void playAttackSound(Sound sound) {
        getCraftEntity().getWorld().playSound(getLocation(), sound, 1, 1);
    }

    /**
     * Converts the inventory of this entity into a String.
     * Method by graywolf336: https://gist.github.com/graywolf336/8153678
     *
     * @return Inventory as String
     */
    default String inventoryAsBase64String() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput;

        try {

            dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(getInventory().getSize());
            for (int i = 0; i < getInventory().getSize(); i++)
                dataOutput.writeObject(getInventory().getItem(i));
            dataOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Coder.encodeLines(outputStream.toByteArray());

    }

    /**
     * Converts a String into an inventory.
     * Method by graywolf336: https://gist.github.com/graywolf336/8153678
     *
     * @param inventoryString The inventory formatted as a Base64 String
     * @return An inventory
     */
    default Inventory inventoryFromBase64String(String inventoryString) {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(inventoryString));
        BukkitObjectInputStream dataInput;

        try {

            dataInput = new BukkitObjectInputStream(inputStream);

            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
