package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * General stats of an entity. Also grands access to base stats of an entity.
 */
public class EntityStats<T extends EntityInsentient & MSEEntity> {

    private T mseEntity;

    private BaseStats baseStats;

    private int alphaPredatorMultiplier; // baseStats are multiplied by this value; set to 1 if not an alpha
    private int currentFoodValue;
    private int foodDepletion;
    private Integer level;

    private float currentXp;

    private String entityType;

    private FoodTimer foodTimer;

    public EntityStats(T mseEntity) {

        this.mseEntity = mseEntity;
        entityType = mseEntity.getName();

        baseStats = BaseStats.getBaseAttributesFor(mseEntity.getName());
        if (Calculator.getRandomInt(101) <= baseStats.getAlphaProbability())
            alphaPredatorMultiplier = 4;
        else
            alphaPredatorMultiplier = 1;

        currentFoodValue = baseStats.getMaxFoodValue();
        foodDepletion = 5;
        currentXp = 0;
        updateLevel(0);
        if (mseEntity.getTamingHandler().isTamed())
            startFoodTimer();

    }

    public boolean isAlpha() {
        return (alphaPredatorMultiplier != 1);
    }

    public BaseStats getBaseStats() {
        return baseStats;
    }

    public int getFortitude() {
        return (int) Calculator.calculateLevelDependentStatFor(baseStats.getFortitude(), level, baseStats.getLevelMultiplier());
    }

    public int getLevel() {
        return level;
    }

    public float getCurrentXp() {
        return currentXp;
    }

    public float getXpUntilLevelUp() {
        return baseStats.getXpUntilLevelUp();
    }

    public double getDamage() {
        return Calculator.calculateLevelDependentStatFor(baseStats.getDamage(), level, getMultiplier());
    }

    public float getSpeed() {
        float speedMultiplier = getMultiplier();
        speedMultiplier /= 2;
        return (float) Calculator.calculateLevelDependentStatFor(baseStats.getSpeed(), level, speedMultiplier);
    }

    public String getDefaultName() {
        if (this.isAlpha())
            return "Alpha " + entityType + " (" + level + ")";
        else
            return entityType + " (" + level + ")";
    }

    public String getEntityType() {
        return entityType;
    }

    public int getCurrentFoodValue() {
        return currentFoodValue;
    }

    public float getMultiplier() {
        return baseStats.getLevelMultiplier() * alphaPredatorMultiplier;
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    public void updateLevel(int levelIncrease) {
        if (level == null)
            level = Calculator.getRandomInt(baseStats.getLevelCap()) + 1;
        if (levelIncrease > 0)
            level += levelIncrease;
        if (!mseEntity.getTamingHandler().isTamed()) {
            mseEntity.setCustomName(getDefaultName());
        }
    }

    /**
     * Increases the xp for this entity. Also handles level ups.
     *
     * @param xpIncrease
     */
    public void increaseXp(float xpIncrease) {
        currentXp += xpIncrease;
        float currentXpForLevelUp = (float) Calculator.calculateLevelDependentStatFor(baseStats.getXpUntilLevelUp(), level, getMultiplier());
        while (currentXp >= currentXpForLevelUp) {
            currentXp -= currentXpForLevelUp;
            updateLevel(1);
            currentXpForLevelUp = (float) Calculator.calculateLevelDependentStatFor(baseStats.getXpUntilLevelUp(), level, getMultiplier());
        }
    }

    /**
     * Tries to eat food from the inventory.
     *
     * @return Saturation of the food that was eaten.
     */
    public int updateHunger() {

        Inventory inventory = mseEntity.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null)
                continue;

            Material itemMaterial = item.getType();
            if (baseStats.getPreferredFood().containsKey(itemMaterial)) {

                int saturation = baseStats.getFoodsaturationFor(item.getType().toString());
                if (saturation <= 0)
                    continue;
                if (currentFoodValue + saturation > baseStats.getMaxFoodValue())
                    return 0;

                currentFoodValue += saturation;
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);

                return baseStats.getPreferredFood().get(itemMaterial);
            }
        }
        if (!mseEntity.getTamingHandler().isTamed())
            // Taming bar decreases if entity is not fed continuously
            return baseStats.getHighestFoodSaturation() < baseStats.getMaxFoodValue() - currentFoodValue ? -10 : 0;
        return 0;

    }

    /**
     * Starts food depletion for this entity.
     */
    public void startFoodTimer() {
        if (foodTimer != null)
            return;
        foodTimer = new FoodTimer();
        foodTimer.runTaskTimerAsynchronously(ThisPlugin.getInstance(), 0L, 100L);
    }

    /**
     * Handles autonomous eating of an entity
     */
    class FoodTimer extends BukkitRunnable {

        @Override
        public void run() {

            if (!mseEntity.isAlive())
                this.cancel();

            currentFoodValue -= foodDepletion;
            if (mseEntity.getTamingHandler().isTamed()) // Hunger is updated by taming handler during a taming process
                updateHunger();
            if (currentFoodValue <= 0) {
                mseEntity.setHealth(mseEntity.getHealth() - 0.5F);
                currentFoodValue = 0;
            }

        }
    }

}
