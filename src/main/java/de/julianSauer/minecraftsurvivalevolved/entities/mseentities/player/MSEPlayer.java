package de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player;

import de.juliansauer.minecraftsurvivalevolved.entities.UnconsciousnessTimer;
import de.juliansauer.minecraftsurvivalevolved.entities.containers.AttributesContainer;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.AttributedEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.Unconsciousable;
import net.minecraft.server.v1_9_R1.EntityLiving;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * Wrapper class for players.
 */
public class MSEPlayer extends AttributesContainer implements Unconsciousable, AttributedEntity {

    private boolean initialized;

    Player player;

    UnconsciousnessTimer unconsciousnessTimer;

    MSEPlayer(Player player) {
        super("Player");
        this.player = player;
        level = player.getLevel();
        currentXp = player.getExp();
        initialized = false;
        this.player = player;
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
    public float getSpeed() {
        return player.getWalkSpeed();
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
        return 30;
    }

    @Override
    public int getFoodDepletion() {
        return 0;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getDefaultName() {
        return player.getName();
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
