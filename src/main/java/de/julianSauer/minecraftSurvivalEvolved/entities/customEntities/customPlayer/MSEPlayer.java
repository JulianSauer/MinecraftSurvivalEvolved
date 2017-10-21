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

    MSEPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void eatAnimation() {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
    }

    @Override
    public EntityAttributes getEntityAttributes() {
        return null;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public UnconsciousnessTimer getUnconsciousnessTimer() {
        return null;
    }

    @Override
    public void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer) {

    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
