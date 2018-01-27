package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.entity.Player;

/**
 * Represents all current and maximum attribute values of an entity.
 */
public class EntityAttributesContainer {

    private Object entity;

    AttributesContainer attributesContainer;

    Integer level;
    float currentXp;
    boolean unconscious;
    int torporDepletion;
    int torpidity;

    public EntityAttributesContainer(Object entity) {

        this.entity = entity;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            attributesContainer = AttributesContainer.getBaseAttributesFor("Player");
            level = player.getLevel();
            currentXp = player.getExp();
            unconscious = false;
            torpidity = 0;
        }
        torporDepletion = 1;

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

    public AttributesContainer getAttributesContainer() {
        return attributesContainer;
    }

    public int getTorpidity() {
        return torpidity;
    }

    public void setTorpidity(int torpidity) {
        this.torpidity = torpidity;
    }

    public int getMaxTorpidity() {
        return (int) Calculator.calculateLevelDependentStatFor(attributesContainer.getMaxTorpidity(), getLevel(), getMultiplier());
    }

    public Integer getFortitude() {
        return (int) Calculator.calculateLevelDependentStatFor(attributesContainer.getFortitude(), level, getMultiplier());
    }

    public int getLevel() {
        return level;
    }

    public float getCurrentXp() {
        return currentXp;
    }

    public float getXpUntilLevelUp() {
        return attributesContainer.getXpUntilLevelUp();
    }

    public double getDamage() {
        return Calculator.calculateLevelDependentStatFor(attributesContainer.getDamage(), level, getMultiplier());
    }

    public double getMaxDamage() {
        return Calculator.calculateLevelDependentStatFor(attributesContainer.getDamage(), attributesContainer.getLevelCap(), getMultiplier());
    }

    public String getDefaultName() {
        if (entity instanceof EntityInsentient)
            return ((EntityInsentient) entity).getName();
        else if (entity instanceof Player)
            return ((Player) entity).getName();
        return null;
    }

    public float getMultiplier() {
        return attributesContainer.getLevelMultiplier();
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    public void updateLevel(int levelIncrease) {
        if (level == null)
            level = Calculator.getRandomInt(attributesContainer.getLevelCap()) + 1;
        if (levelIncrease > 0)
            level += levelIncrease;
    }

    /**
     * Increases the xp for this entity. Also handles level ups.
     *
     * @param xpIncrease Amount of increase
     */
    public void increaseXp(float xpIncrease) {
        currentXp += xpIncrease;
        float currentXpForLevelUp = (float) Calculator.calculateLevelDependentStatFor(attributesContainer.getXpUntilLevelUp(), level, getMultiplier());
        while (currentXp >= currentXpForLevelUp) {
            currentXp -= currentXpForLevelUp;
            updateLevel(1);
            currentXpForLevelUp = (float) Calculator.calculateLevelDependentStatFor(attributesContainer.getXpUntilLevelUp(), level, getMultiplier());
        }
    }

}
