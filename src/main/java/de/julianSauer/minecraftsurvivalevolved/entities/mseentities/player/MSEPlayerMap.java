package de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Maps online players to MSEPlayers.
 */
public class MSEPlayerMap {

    private static MSEPlayerMap instance;

    private Map<UUID, MSEPlayer> playerToMSEPlayerMap;

    private MSEPlayerMap() {
        playerToMSEPlayerMap = new HashMap<>();
    }

    public static MSEPlayerMap getPlayerRegistry() {
        if (instance == null)
            instance = new MSEPlayerMap();
        return instance;
    }

    public void registerPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!playerToMSEPlayerMap.containsKey(playerUUID))
            playerToMSEPlayerMap.put(playerUUID, new MSEPlayer(player));
    }

    public void unregisterPlayer(UUID playerUUID) {
        if (playerToMSEPlayerMap.containsKey(playerUUID))
            playerToMSEPlayerMap.remove(playerUUID);
    }

    public void unregisterPlayer(Player player) {
        unregisterPlayer(player.getUniqueId());
    }

    public MSEPlayer getMSEPlayer(UUID playerUUID) {
        return playerToMSEPlayerMap.get(playerUUID);
    }

    public MSEPlayer getMSEPlayer(Player player) {
        if (player == null || !player.isOnline())
            return null;
        return getMSEPlayer(player.getUniqueId());
    }

    public boolean isMSEPlayer(Entity entity) {
        return playerToMSEPlayerMap.containsKey(entity.getUniqueId());
    }

}
