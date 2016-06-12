package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.EntityInsentient;

/**
 * General stats of an entity. Also grands access to base stats of an entity.
 */
public class EntityStats<T extends EntityInsentient & MSEEntity> {

    private T mceEntity;

    private BaseStats baseStats;

    private int alphaPredatorMultiplier; // baseStats are multiplied by this value; set to 1 if not an alpha
    private Integer level;

    private float currentXp;

    private String entityType;

    public EntityStats(T mceEntity) {

        this.mceEntity = mceEntity;
        entityType = mceEntity.getName();

        baseStats = BaseStats.getBaseAttributesFor(mceEntity.getName());
        if (Calculator.getRandomInt(101) <= baseStats.getAlphaProbability())
            alphaPredatorMultiplier = 4;
        else
            alphaPredatorMultiplier = 1;


        currentXp = 0;
        updateLevel(0);

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
        if (!mceEntity.getTamingHandler().isTamed()) {
            mceEntity.setCustomName(getDefaultName());
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

}
