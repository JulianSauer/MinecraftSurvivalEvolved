package de.julianSauer.minecraftSurvivalEvolved.entities.containers;

import de.julianSauer.minecraftSurvivalEvolved.entities.FoodTimer;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.Persistentable;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.AlphaParticleSpawner;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

/**
 * Represents all current and maximum attribute values of a tameable entity.
 */
public class TameableAttributesContainer<T extends EntityInsentient & MSEEntity> extends EntityAttributesContainer implements Persistentable {

    private T mseEntity;

    private boolean initialized;

    private int alphaPredatorMultiplier; // attributesContainer are multiplied by this value; set to 1 if not an alpha
    private int currentFoodValue;
    private int foodDepletion;
    private boolean tamed;
    private UUID owner;
    private UUID tribe;

    private FoodTimer foodTimer;

    public TameableAttributesContainer(T mseEntity) {

        super(mseEntity);

        this.mseEntity = mseEntity;
        attributesContainer = AttributesContainer.getBaseAttributesFor(mseEntity.getEntityType());
        foodDepletion = 5;
        initialized = false;

    }

    @Override
    public void initWithDefaults() {

        if (Calculator.applyProbability(attributesContainer.getAlphaProbability()))
            alphaPredatorMultiplier = 4;
        else
            alphaPredatorMultiplier = 1;

        updateLevel(0);
        currentFoodValue = attributesContainer.getMaxFoodValue();
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
        torpidity = data.getInt("MSETorpidity");
        unconscious = data.getBoolean("MSEUnconscious");

        updateLevel(0);
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

    public boolean isTameable() {
        return attributesContainer.isTameable() && !tamed && !isAlpha();
    }

    public FoodTimer getFoodTimer() {
        return foodTimer;
    }

    public int getMaxTamingProgress() {
        if (isAlpha())
            MSEMain.getInstance().getLogger().info("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return (int) Calculator.calculateLevelDependentStatFor(attributesContainer.getMaxTamingProgress(), getLevel(), getMultiplier());
    }

    public UUID getOwner() {
        if (tamed && !isAlpha())
            return owner;
        return null;
    }

    public UUID getTribe() {
        return tribe;
    }

    public double getDamage() {
        return Calculator.calculateLevelDependentStatFor(attributesContainer.getDamage(), level, getMultiplier());
    }

    public double getMaxDamage() {
        return Calculator.calculateLevelDependentStatFor(attributesContainer.getDamage(), attributesContainer.getLevelCap(), getMultiplier());
    }

    public float getSpeed() {
        float speedMultiplier = getMultiplier();
        speedMultiplier /= 2;
        return (float) Calculator.calculateLevelDependentStatFor(attributesContainer.getSpeed(), level, speedMultiplier);
    }

    @Override
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
        return attributesContainer.getLevelMultiplier() * alphaPredatorMultiplier;
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    @Override
    public void updateLevel(int levelIncrease) {
        if (level == null)
            level = Calculator.getRandomInt(attributesContainer.getLevelCap()) + 1;
        if (levelIncrease > 0)
            level += levelIncrease;
        if (!isTamed())
            mseEntity.setCustomName(getDefaultName());
    }

    /**
     * Starts food depletion for this entity.
     */
    public void startFoodTimer() {
        if (foodTimer != null)
            return;
        foodTimer = new FoodTimer(mseEntity);
        foodTimer.runTaskTimerAsynchronously(MSEMain.getInstance(), 0L, 100L);
    }


}
