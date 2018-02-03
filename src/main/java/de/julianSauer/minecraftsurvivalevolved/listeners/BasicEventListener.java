package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayer;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

interface BasicEventListener extends Listener, BasicListener {

    default boolean playerIsUnconscious(Player player) {
        return playerIsUnconscious(player.getUniqueId());
    }

    default boolean playerIsUnconscious(UUID uuid) {
        MSEPlayer player = MSEPlayerMap.getPlayerRegistry().getMSEPlayer(uuid);
        return player != null && player.isUnconscious();
    }

    default boolean entityIsUnconscious(Entity entity) {
        return entity instanceof CraftEntity
                && ((CraftEntity) entity).getHandle() instanceof MSEEntity
                && ((MSEEntity) ((CraftEntity) entity).getHandle()).isUnconscious();
    }

}
