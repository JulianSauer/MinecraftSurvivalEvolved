package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimer;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.Unconsciousable;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Wrapper class for players.
 */
public class MSEPlayer implements Unconsciousable {

    Player player;

    EntityAttributes entityAttributes;

    UnconsciousnessTimer unconsciousnessTimer;

    MSEPlayer(Player player) {
        this.player = player;
        entityAttributes = new EntityAttributes(player);
    }

    @Override
    public void eatAnimation() {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
    }

    @Override
    public EntityAttributes getEntityAttributes() {
        return entityAttributes;
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
}
