package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import net.minecraft.server.v1_9_R1.EntityLiving;

import java.util.UUID;

public interface AttributedEntity {

    UUID getUniqueID();

    float getHealth();

    double getDamage();

    double getMaxDamage();

    float getSpeed();

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

    EntityLiving getHandle();

}
