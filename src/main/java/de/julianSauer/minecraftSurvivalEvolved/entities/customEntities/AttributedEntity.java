package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import java.util.UUID;

public interface AttributedEntity {

    UUID getUniqueID();

    float getHealth();

    double getDamage();

    double getMaxDamage();

    float getSpeed();

    int getTorpidity();

    void setTorpidity(int torpidity);

    int getMaxTorpidity();

    int getTorporDepletion();

    boolean isUnconscious();

    void setUnconscious(boolean unconscious);

    int getFortitude();

    int getFood();

    void setFood(int food);

    int getMaxFood();

    int getFoodDepletion();

    int getLevel();

    float getLevelMultiplier();

    float getXpUntilLevelUp();

    int getLevelCap();

    String getName();

    String getDefaultName();

    String getEntityType();

}
