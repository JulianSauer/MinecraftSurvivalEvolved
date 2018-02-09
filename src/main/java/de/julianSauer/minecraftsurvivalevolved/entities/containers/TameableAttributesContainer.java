package de.juliansauer.minecraftsurvivalevolved.entities.containers;

import de.juliansauer.minecraftsurvivalevolved.config.ConfigHandler;
import de.juliansauer.minecraftsurvivalevolved.entities.FoodTimer;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.Persistentable;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.gui.visuals.AlphaParticleSpawner;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import de.juliansauer.minecraftsurvivalevolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Represents all current and maximum attribute values of a tameable entity.
 */
public class TameableAttributesContainer<T extends EntityInsentient & MSEEntity> extends AttributesContainer implements Persistentable {

    private T mseEntity;

    private boolean initialized;

    private final int alphaProbability;
    private int alphaPredatorMultiplier; // attributesContainer are multiplied by this value; set to 1 if not an alpha
    private final int maxFoodValue;
    private final int highestFoodSaturation;
    private final int maxTamingProgress;
    private int currentFoodValue;
    private int foodDepletion;
    private boolean tamed;
    private UUID owner;
    private UUID tribe;

    private final Map<Material, Integer> preferredFood;
    private final Map<String, Integer> foodSaturations;

    private FoodTimer<T> foodTimer;

    public TameableAttributesContainer(T mseEntity) {

        super(mseEntity.getEntityType());

        ConfigHandler config = MSEMain.getInstance().getConfigHandler();
        alphaProbability = config.getAlphaProbabilityFor(mseEntity.getEntityType());
        foodSaturations = config.getFoodSaturations();
        preferredFood = config.getPreferredFoodFor(mseEntity.getEntityType());
        maxFoodValue = config.getMaxFoodFor(mseEntity.getEntityType());
        maxTamingProgress = config.getMaxTamingProgressFor(mseEntity.getEntityType());
        highestFoodSaturation = Collections.max(foodSaturations.values());
        foodDepletion = 5;

        this.mseEntity = mseEntity;
        initialized = false;

    }

    @Override
    public void initWithDefaults() {

        super.initWithDefaults();

        if (Calculator.applyProbability(alphaProbability))
            alphaPredatorMultiplier = 4;
        else
            alphaPredatorMultiplier = 1;

        updateLevel(0);
        currentFoodValue = getMaxFoodValue();
        currentXp = 0;
        tamed = false;
        if (isTamed())
            startFoodTimer();

        if (isAlpha()) {
            (new AlphaParticleSpawner((LivingEntity) mseEntity.getCraftEntity())).startEffects();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
        }

        initialized = true;

    }

    @Override
    public void initWith(NBTTagCompound data) {

        super.initWith(data);

        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        if (data.getBoolean("MSEIsAlpha")) {
            alphaPredatorMultiplier = 4;
            (new AlphaParticleSpawner((LivingEntity) mseEntity.getCraftEntity())).startEffects();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
        } else
            alphaPredatorMultiplier = 1;

        level = data.getInt("MSELevel");
        currentFoodValue = data.getInt("MSECurrentFoodValue");
        currentXp = data.getFloat("MSECurrentXp");
        tamed = data.getBoolean("MSETamed");
        unconscious = data.getBoolean("MSEUnconscious");

        if (isTamed())
            startFoodTimer();

        if (tamed) {
            String ownerUUID = data.getString("MSEOwner");
            String tribeUUID = data.getString("MSETribe");
            if (ownerUUID != null && !ownerUUID.isEmpty())
                owner = UUID.fromString(ownerUUID);
            if (tribeUUID != null && !tribeUUID.isEmpty())
                tribe = UUID.fromString(tribeUUID);
        }

        initialized = true;

    }

    @Override
    public void saveData(NBTTagCompound data) {

        super.saveData(data);

        if (!isInitialized())
            initWithDefaults();

        data.setInt("MSELevel", getLevel());
        data.setBoolean("MSEIsAlpha", isAlpha());
        data.setInt("MSECurrentFoodValue", getCurrentFoodValue());
        data.setFloat("MSECurrentXp", getCurrentXp());

        data.setBoolean("MSETamed", isTamed());
        data.setBoolean("MSEUnconscious", unconscious);
        data.setInt("MSETorpidity", getTorpidity());
        if (isTamed()) {
            if (owner != null)
                data.setString("MSEOwner", getOwner().toString());
            if (tribe != null)
                data.setString("MSETribe", tribe.toString());
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public boolean isAlpha() {
        return (alphaPredatorMultiplier != 1);
    }

    public boolean isTamed() {
        return tamed && !isAlpha();
    }

    public void setTamed(boolean tamed) {
        this.tamed = tamed;
    }

    public void setTribe(UUID tribe) {
        this.tribe = tribe;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public boolean isTameable() {
        return super.isTameable() && !tamed && !isAlpha();
    }

    public FoodTimer getFoodTimer() {
        return foodTimer;
    }

    public int getMaxTamingProgress() {
        if (isAlpha())
            MSEMain.getInstance().getLogger().info("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return (int) Calculator.calculateLevelDependentStatFor(maxTamingProgress, getLevel(), getMultiplier());
    }

    public UUID getOwner() {
        if (tamed && !isAlpha())
            return owner;
        return null;
    }

    public UUID getTribe() {
        return tribe;
    }

    public String getDefaultName() {
        if (this.isAlpha())
            return "Alpha " + mseEntity.getEntityType() + " (" + level + ")";
        else
            return mseEntity.getEntityType() + " (" + level + ")";
    }

    public int getCurrentFoodValue() {
        return currentFoodValue;
    }

    public void setCurrentFoodValue(int currentFoodValue) {
        this.currentFoodValue = currentFoodValue;
    }

    public int getFoodDepletion() {
        return foodDepletion;
    }

    @Override
    public float getMultiplier() {
        return getLevelMultiplier() * alphaPredatorMultiplier;
    }

    public int getMaxFoodValue() {
        return maxFoodValue;
    }

    public int getFoodsaturationFor(String food) {
        if (foodSaturations.containsKey(food))
            return foodSaturations.get(food);
        return 0;
    }

    public int getHighestFoodSaturation() {
        return highestFoodSaturation;
    }

    public Map<Material, Integer> getPreferredFood() {
        return preferredFood;
    }

    /**
     * Starts food depletion for this entity.
     */
    public void startFoodTimer() {
        if (foodTimer != null)
            return;
        foodTimer = new FoodTimer<>(mseEntity);
        foodTimer.runTaskTimerAsynchronously(MSEMain.getInstance(), 0L, 100L);
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    public void updateLevel(int levelIncrease) {
        level = Calculator.getRandomInt(getMaxLevel() + 1);
        if (levelIncrease > 0)
            level += levelIncrease;
        if (!isTamed())
            mseEntity.setCustomName(getDefaultName());
    }

    /**
     * Increases the xp for this entity. Also handles level ups.
     *
     * @param xpIncrease Amount of increase
     */
    public void increaseXp(float xpIncrease) {
        currentXp += xpIncrease;
        float currentXpForLevelUp = (float) Calculator.calculateLevelDependentStatFor(getXpUntilLevelUp(), level, getMultiplier());
        while (currentXp >= currentXpForLevelUp) {
            currentXp -= currentXpForLevelUp;
            updateLevel(1);
            currentXpForLevelUp = (float) Calculator.calculateLevelDependentStatFor(getXpUntilLevelUp(), level, getMultiplier());
        }
    }

}
