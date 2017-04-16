package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.BaseStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.AlphaParticleSpawner;
import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * General stats of an entity. Also grands access to base stats of an entity.
 * 
 * Note: The weird error printing is due to Minecraft catching all exceptions when loading an entity and simply removing
 * it instead of printing it there.
 */
public class GeneralBehaviorHandler<T extends EntityInsentient & MSEEntity> implements Persistentable {

    private final T mseEntity;

    private final BaseStats baseStats;

    private int alphaPredatorMultiplier; // baseStats are multiplied by this value; set to 1 if not an alpha
    private int currentFoodValue;
    private int foodDepletion;
    private Integer level;

    private float currentXp;

    private FoodTimer foodTimer;

    private boolean initialized;

    public GeneralBehaviorHandler(T mseEntity) {
        this.mseEntity = mseEntity;
        baseStats = BaseStats.getBaseAttributesFor(mseEntity.getEntityType());
        foodDepletion = 5;
        initialized = false;
    }

    @Override
    public void initWithDefaults() {
        initialized = true;

        if (Calculator.applyProbability(baseStats.getAlphaProbability()))
            alphaPredatorMultiplier = 4;
        else
            alphaPredatorMultiplier = 1;

        updateLevel(0);
        currentFoodValue = baseStats.getMaxFoodValue();
        currentXp = 0;
        if (mseEntity.getTamingHandler().isTamed())
            startFoodTimer();

        if (isAlpha()) {
            (new AlphaParticleSpawner((LivingEntity) mseEntity.getCraftEntity())).startEffects();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
        }
    }

    @Override
    public void initWith(NBTTagCompound data) {
        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        initialized = true;
        if (data.getBoolean("MSEIsAlpha")) {
            alphaPredatorMultiplier = 4;
            (new AlphaParticleSpawner((LivingEntity) mseEntity.getCraftEntity())).startEffects();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
        } else
            alphaPredatorMultiplier = 1;

        this.level = data.getInt("MSELevel");
        this.currentFoodValue = data.getInt("MSECurrentFoodValue");
        this.currentXp = data.getFloat("MSECurrentXp");
        updateLevel(0);
        if (mseEntity.getTamingHandler().isTamed())
            startFoodTimer();
    }

    @Override
    public void saveData(NBTTagCompound data) {
        data.setInt("MSELevel", getLevel());
        data.setBoolean("MSEIsAlpha", isAlpha());
        data.setInt("MSECurrentFoodValue", getCurrentFoodValue());
        data.setFloat("MSECurrentXp", getCurrentXp());
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public boolean isAlpha() {
        if (!initialized)
            return false;
        return (alphaPredatorMultiplier != 1);
    }

    public BaseStats getBaseStats() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return baseStats;
    }

    public Integer getFortitude() {
        if (!initialized)
            return null;
        return (int) Calculator.calculateLevelDependentStatFor(baseStats.getFortitude(), level, getMultiplier());
    }

    public int getLevel() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return level;
    }

    public float getCurrentXp() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return currentXp;
    }

    public float getXpUntilLevelUp() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return baseStats.getXpUntilLevelUp();
    }

    public double getDamage() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return Calculator.calculateLevelDependentStatFor(baseStats.getDamage(), level, getMultiplier());
    }

    public double getMaxDamage() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return Calculator.calculateLevelDependentStatFor(baseStats.getDamage(), baseStats.getLevelCap(), getMultiplier());
    }

    public float getSpeed() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        float speedMultiplier = getMultiplier();
        speedMultiplier /= 2;
        return (float) Calculator.calculateLevelDependentStatFor(baseStats.getSpeed(), level, speedMultiplier);
    }

    public String getDefaultName() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        if (this.isAlpha())
            return "Alpha " + mseEntity.getEntityType() + " (" + level + ")";
        else
            return mseEntity.getEntityType() + " (" + level + ")";
    }

    public int getCurrentFoodValue() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return currentFoodValue;
    }

    public float getMultiplier() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return baseStats.getLevelMultiplier() * alphaPredatorMultiplier;
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    public void updateLevel(int levelIncrease) {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        if (level == null)
            level = Calculator.getRandomInt(baseStats.getLevelCap()) + 1;
        if (levelIncrease > 0)
            level += levelIncrease;
        if (!mseEntity.getTamingHandler().isTamed())
            mseEntity.setCustomName(getDefaultName());
    }

    /**
     * Increases the xp for this entity. Also handles level ups.
     *
     * @param xpIncrease Amount of increase
     */
    public void increaseXp(float xpIncrease) {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

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

        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

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
                if (currentFoodValue + saturation + foodDepletion > baseStats.getMaxFoodValue())
                    return 0;

                currentFoodValue += saturation + foodDepletion;
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
        foodTimer.runTaskTimerAsynchronously(MSEMain.getInstance(), 0L, 100L);
    }

    /**
     * Handles autonomous eating of an entity
     */
    private class FoodTimer extends BukkitRunnable {

        @Override
        public void run() {

            if (!mseEntity.isAlive())
                this.cancel();
            if (!isInitialized())
                return;

            currentFoodValue -= foodDepletion;
            if (mseEntity.getTamingHandler().isTamed()) // Hunger is updated by taming handler during a taming process
                updateHunger();
            if (currentFoodValue <= 0) {
                mseEntity.damageEntity(DamageSource.GENERIC, 0.5F);
                currentFoodValue = 0;
            } else if (Calculator.applyProbability(30))
                mseEntity.setHealth(mseEntity.getHealth() + 0.5F);

        }
    }

}
