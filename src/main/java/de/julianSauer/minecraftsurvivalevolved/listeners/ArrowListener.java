package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.Unconsciousable;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public interface ArrowListener extends BasicEventListener {

    default boolean isTranqArrow(Entity arrow) {
        if (!(arrow instanceof TippedArrow))
            return false;
        List<MetadataValue> titleData = arrow.getMetadata("Tranquilizer Arrow");
        if (titleData.size() == 1)
            return titleData.get(0).asBoolean();
        return false;
    }

    /**
     * Checks if a player shoots tranq arrows at a tameable entity.
     *
     * @param target  Target of the attack
     * @param damager Shot arrow
     * @return True if a tranq arrow is used against a tameable entity
     */
    default boolean isTranqEvent(Entity target, Entity damager) {

        if (!isTranqArrow(damager))
            return false;
        TippedArrow arrow = (TippedArrow) damager;
        if (!(arrow.getShooter() instanceof Player))
            return false;
        MSEEntity mseEntity = getMSEEntityFromEntity(target);
        return mseEntity instanceof Unconsciousable || MSEPlayerMap.getPlayerRegistry().isMSEPlayer(target);

    }

}
