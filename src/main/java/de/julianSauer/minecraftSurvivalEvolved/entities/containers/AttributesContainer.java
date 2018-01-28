package de.julianSauer.minecraftSurvivalEvolved.entities.containers;

import de.julianSauer.minecraftSurvivalEvolved.config.ConfigHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.Persistentable;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.NBTTagCompound;

/**
 * Loads a basic set of attributes for entities from config.
 */
public class AttributesContainer implements Persistentable {

    private boolean initialized;

    private final boolean tameable;
    protected boolean unconscious;

    private final int maxLevel;
    private final int maxTorpidity;
    private int torporDepletion;
    protected int torpidity;
    private int fortitude;
    private double damage;
    protected int level;
    protected float currentXp;
    private final int xpUntilLevelUp;

    private final float levelMultiplier;
    private final float speed;

    public AttributesContainer(String entity) {

        torporDepletion = 1;

        ConfigHandler config = MSEMain.getInstance().getConfigHandler();

        // Prevent spaces in .yml files
        entity = entity.replace(" ", "");
        tameable = config.getTameableFor(entity);
        fortitude = config.getFortitudeFor(entity);
        maxLevel = config.getLevelCapFor(entity);
        maxTorpidity = config.getMaxToripidityFor(entity);
        xpUntilLevelUp = config.getXpUntilLevelUpFor(entity);
        levelMultiplier = config.getLevelMultiplierFor(entity);
        damage = config.getDamageFor(entity);
        speed = config.getSpeedFor(entity);

        initialized = false;

    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void initWithDefaults() {
        unconscious = false;
        torpidity = 0;
        level = 1;
        currentXp = 0;

        initialized = true;
    }

    @Override
    public void initWith(NBTTagCompound data) {

        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        unconscious = data.getBoolean("MSEUnconscious");
        torpidity = data.getInt("MSETorpidity");
        level = data.getInt("MSELevel");
        currentXp = data.getFloat("MSECurrentXp");

        initialized = true;
    }

    @Override
    public void saveData(NBTTagCompound data) {
        if (!isInitialized())
            initWithDefaults();

        data.setBoolean("MSEUnconscious", unconscious);
        data.setInt("MSETorpidity", torpidity);
        data.setInt("MSELevel", level);
        data.setFloat("MSECurrentXp", currentXp);
    }

    public boolean isUnconscious() {
        return unconscious;
    }

    public void setUnconscious(boolean unconscious) {
        this.unconscious = unconscious;
    }

    public int getTorporDepletion() {
        return torporDepletion;
    }

    public int getTorpidity() {
        return torpidity;
    }

    public void setTorpidity(int torpidity) {
        this.torpidity = torpidity;
    }

    public boolean isTameable() {
        return tameable;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMaxTorpidity() {
        return (int) Calculator.calculateLevelDependentStatFor(maxTorpidity, level, levelMultiplier);
    }

    public float getCurrentXp() {
        return currentXp;
    }

    public int getFortitude() {
        return (int) Calculator.calculateLevelDependentStatFor(fortitude, level, levelMultiplier);
    }

    public float getXpUntilLevelUp() {
        return xpUntilLevelUp;
    }

    public float getLevelMultiplier() {
        return levelMultiplier;
    }

    public float getSpeed() {
        float speedMultiplier = getMultiplier();
        speedMultiplier /= 2;
        return (float) Calculator.calculateLevelDependentStatFor(speed, level, speedMultiplier);
    }

    public int getLevel() {
        return level;
    }

    public double getDamage() {
        return Calculator.calculateLevelDependentStatFor(damage, level, levelMultiplier);
    }

    public double getMaxDamage() {
        return Calculator.calculateLevelDependentStatFor(damage, maxLevel, levelMultiplier);
    }

    public float getMultiplier() {
        return levelMultiplier;
    }

}
