package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer;

import de.julianSauer.minecraftSurvivalEvolved.entities.containers.EntityAttributesContainer;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimer;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.AttributedEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.Unconsciousable;
import net.minecraft.server.v1_9_R1.EntityLiving;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * Wrapper class for players.
 */
public class MSEPlayer implements Unconsciousable, AttributedEntity {

    Player player;

    EntityAttributesContainer entityAttributesContainer;

    UnconsciousnessTimer unconsciousnessTimer;

    MSEPlayer(Player player) {
        this.player = player;
        entityAttributesContainer = new EntityAttributesContainer(player);
    }

    @Override
    public AttributedEntity getAttributedEntity() {
        return this;
    }

    @Override
    public UUID getUniqueID() {
        return player.getUniqueId();
    }

    @Override
    public float getHealth() {
        return (float) player.getHealth();
    }

    @Override
    public double getDamage() {
        return entityAttributesContainer.getDamage();
    }

    @Override
    public double getMaxDamage() {
        return entityAttributesContainer.getMaxDamage();
    }

    @Override
    public float getSpeed() {
        return player.getWalkSpeed();
    }

    @Override
    public int getTorpidity() {
        return entityAttributesContainer.getTorpidity();
    }

    @Override
    public void setTorpidity(int torpidity) {
        entityAttributesContainer.setTorpidity(torpidity);
    }

    @Override
    public int getMaxTorpidity() {
        return entityAttributesContainer.getMaxTorpidity();
    }

    @Override
    public int getTorporDepletion() {
        return entityAttributesContainer.getTorporDepletion();
    }

    @Override
    public boolean isUnconscious() {
        return entityAttributesContainer.isUnconscious();
    }

    @Override
    public void setUnconscious(boolean unconscious) {
        entityAttributesContainer.setUnconscious(unconscious);
    }

    @Override
    public int getFortitude() {
        return entityAttributesContainer.getFortitude();
    }

    @Override
    public int getFood() {
        return player.getFoodLevel();
    }

    @Override
    public void setFood(int food) {
        player.setFoodLevel(food);
    }

    @Override
    public int getMaxFood() {
        return entityAttributesContainer.getAttributesContainer().getMaxFoodValue();
    }

    @Override
    public int getFoodDepletion() {
        return 0;
    }

    @Override
    public int getLevel() {
        return entityAttributesContainer.getLevel();
    }

    @Override
    public float getLevelMultiplier() {
        return entityAttributesContainer.getMultiplier();
    }

    @Override
    public float getXpUntilLevelUp() {
        return entityAttributesContainer.getXpUntilLevelUp();
    }

    @Override
    public int getLevelCap() {
        return entityAttributesContainer.getAttributesContainer().getLevelCap();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getDefaultName() {
        return entityAttributesContainer.getDefaultName();
    }

    @Override
    public String getEntityType() {
        return "Player";
    }

    @Override
    public void eatAnimation() {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public UnconsciousnessTimer getUnconsciousnessTimer() {
        return unconsciousnessTimer;
    }

    @Override
    public void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer) {
        this.unconsciousnessTimer = unconsciousnessTimer;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public EntityLiving getHandle() {
        if (player instanceof CraftPlayer)
            return ((CraftPlayer) player).getHandle();
        return null;
    }

}
