package de.gmx.endermansend.tameableCreatures.entities;

import de.gmx.endermansend.tameableCreatures.main.TameableCreatures;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implements generic functionality for entities that can be used to implement Tameable.
 */
public class EntityAttributes<T extends EntityInsentient & InventoryHolder> {

    private T tameableEntity;

    // Current status
    private boolean unconscious;

    private int torpidity;
    private int tamingProgress;

    // Level dependent
    private Integer level;
    private int maxTorpidity;
    private int fortitude;
    private int maxTamingProgress;

    private float currentXp;

    private double damage;

    // General stats
    private boolean tamed;
    private boolean tameable;

    private int levelCap;
    private int tamingProgressInterval;
    private int torporDepletion;

    private float levelMultiplier; // Increases maxTorpidity, fortitude, maxTamingProgress & damage in percent
    private float xpUntilLevelUp;

    private String name;

    private UUID owner;
    private UUID tamer;

    private List<Material> preferredFood;
    private List<Material> mineableBlocks;


    private UnconsciousnessTimer unconsciousnessTimer;
    private long unconsciousnessUpdateInterval;

    public EntityAttributes(T tameableEntity) {

        this.tameableEntity = tameableEntity;

        tamed = false;
        tameable = true;
        unconscious = false;

        torpidity = 0;
        maxTorpidity = 100;
        fortitude = 20;
        torporDepletion = 1;
        tamingProgress = 0;
        tamingProgressInterval = 0;
        maxTamingProgress = 100;
        unconsciousnessUpdateInterval = 100L;
        levelCap = 120;
        levelMultiplier = 0.01F;
        damage = 3;
        currentXp = 0;
        xpUntilLevelUp = 20;

        updateLevel(0);

        addPreferredFood(Material.RAW_BEEF);
        addMineableBlock(Material.LOG);
        addMineableBlock(Material.LEAVES);

    }

    public EntityAttributes(T tameableEntity, int maxTorpidity, int fortitude, int torporDepletion) {
        this(tameableEntity);
        this.maxTorpidity = maxTorpidity;
        this.fortitude = fortitude;
        this.torporDepletion = torporDepletion;
    }

    public EntityAttributes(T tameableEntity, int maxTorpidity, int fortitude, int torporDepletion, int tamingProgressInterval, boolean tameable) {
        this(tameableEntity, maxTorpidity, fortitude, torporDepletion);
        this.tameable = tameable;
        this.tamingProgressInterval = tamingProgressInterval;
    }

    public boolean isTamed() {
        return tamed;
    }

    public boolean isTameable() {
        return tameable;
    }

    public boolean isUnconscious() {
        return unconscious;
    }

    public int getTorpidity() {
        return torpidity;
    }

    public int getMaxTorpidity() {
        return (int) calculateLevelDependentStatFor(maxTamingProgress);
    }

    public double getDamage() {
        return calculateLevelDependentStatFor(damage);
    }

    public int getTamingProgress() {
        return tamingProgress;
    }

    public int getLevel() {
        return level;
    }

    private int getFortitude() {
        return (int) calculateLevelDependentStatFor(fortitude);
    }

    public float getCurrentXp() {
        return currentXp;
    }

    public float getXpUntilLevelUp() {
        return xpUntilLevelUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        tameableEntity.setCustomName(name);
    }

    public int getMaxTamingProgress() {
        return (int) calculateLevelDependentStatFor(maxTamingProgress);
    }

    public UUID getOwner() {
        if (tamed)
            return owner;
        return null;
    }

    public List<Material> getPreferredFood() {
        return preferredFood;
    }

    public List<Material> getMineableBlocks() {
        return mineableBlocks;
    }

    private void addPreferredFood(Material food) {
        if (preferredFood == null)
            preferredFood = new ArrayList<Material>();
        preferredFood.add(food);
    }

    private void addMineableBlock(Material block) {
        if (mineableBlocks == null)
            mineableBlocks = new ArrayList<Material>();
        mineableBlocks.add(block);
    }

    /**
     * Calculates the value for an attribute level dependent.
     *
     * @param baseValue Normal value of the entity
     * @return Level dependent value of the entity
     */
    private double calculateLevelDependentStatFor(double baseValue) {
        return (1 + level * levelMultiplier) * baseValue;
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    private void updateLevel(int levelIncrease) {
        if (level == null)
            level = (int) (Math.random() * levelCap + 1);
        if (levelIncrease > 0) {
            level += levelIncrease;
            if (!tamed)
                setName(tameableEntity.getName() + " (" + level + ")");
        }
    }

    /**
     * Increases the xp for this entity. Also handles level ups.
     *
     * @param xpIncrease
     */
    public void increaseXp(float xpIncrease) {
        currentXp += xpIncrease;
        float currentXpForLevelUp = (float) calculateLevelDependentStatFor(xpUntilLevelUp);
        while (currentXp >= currentXpForLevelUp) {
            currentXp -= currentXpForLevelUp;
            updateLevel(1);
            currentXpForLevelUp = (float) calculateLevelDependentStatFor(xpUntilLevelUp);
        }
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is succesfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        tamer = lastDamager;
        torpidity += torpidityIncrease;
        if (torpidity > getMaxTorpidity())
            torpidity = getMaxTorpidity();
        updateConsciousness();
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        torpidity -= torpidityDecrease;
        if (torpidity < 0)
            torpidity = 0;
        updateConsciousness();
    }

    /**
     * Sets the owner of an entity and wakes it up.
     *
     * @return False if the entity could not be tamed
     */
    private boolean setSuccessfullyTamed() {

        if (!isTamed() && isTameable() && tamer != null) {
            tamed = true;
            tameable = false;
            owner = tamer;
            decreaseTorpidityBy(getMaxTorpidity());
            return true;
        } else {
            return false;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    private void updateConsciousness() {

        if (isUnconscious() && torpidity <= 0) {
            unconscious = false;
            if (unconsciousnessTimer != null) {
                unconsciousnessTimer.cancel();
                System.out.println("Entity is no longer unconscious");
            }
        } else if (!isUnconscious() && torpidity >= getFortitude()) {
            System.out.println("Entity is unconscious");
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(TameableCreatures.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    /**
     * Tries to eat food from the inventory.
     *
     * @return True if food was found.
     */
    private boolean updateHunger() {

        Inventory inventory = tameableEntity.getInventory();

        if (!inventory.contains(Material.RAW_BEEF, 1))
            return false;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (preferredFood.contains(item.getType())) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);
                return true;
            }
        }
        return false;
    }

    class UnconsciousnessTimer extends BukkitRunnable {

        public void run() {
            decreaseTorpidityBy(torporDepletion);
            updateTamingProcess();
        }

        /**
         * Returns the game ticks until the entity becomes conscious again.
         *
         * @return Remaining time in game ticks
         */
        public long getTimeUntilWakeUp() {
            return (torpidity / torporDepletion) * unconsciousnessUpdateInterval;
        }

        /**
         * Updates the progress if the entity is still tameable until it is tamed.
         */
        private void updateTamingProcess() {

            if (!isTameable() || isTamed())
                return;

            if (updateHunger())
                tamingProgress += tamingProgressInterval;
            else
                tamingProgress = (tamingProgress - tamingProgressInterval) < 0 ? 0 : tamingProgress - tamingProgressInterval;
            if (tamingProgress >= 10) { // TODO: Replace with getMaxTamingProgress()
                setSuccessfullyTamed();
            }

        }

    }

}
