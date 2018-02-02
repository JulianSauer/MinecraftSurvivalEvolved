package de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player;

import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
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

    private Map<UUID, Map<String, Object>> offlinePlayers;

    private MSEPlayerMap() {
        playerToMSEPlayerMap = new HashMap<>();
        offlinePlayers = new HashMap<>();
        loadPlayers();
    }

    public static MSEPlayerMap getPlayerRegistry() {
        if (instance == null)
            instance = new MSEPlayerMap();
        return instance;
    }

    /**
     * Loads all players from config.
     */
    public void loadPlayers() {
        MSEMain plugin = MSEMain.getInstance();
        plugin.getLogger().info("Loading Players");
        Map<String, Map<String, Object>> players = plugin.getConfigHandler().getPlayers();
        for (String playerUUID : players.keySet())
            offlinePlayers.put(UUID.fromString(playerUUID), players.get(playerUUID));
        plugin.getLogger().info("Players loaded");
    }

    /**
     * Saves all players to config.
     */
    public void savePlayers() {
        MSEMain plugin = MSEMain.getInstance();
        plugin.getLogger().info("Saving Players");
        for (MSEPlayer player : playerToMSEPlayerMap.values())
            offlinePlayers.put(player.getUniqueID(), player.getAttributesMap());
        plugin.getConfigHandler().setPlayers(offlinePlayers);
        plugin.getLogger().info("Players saved");
    }

    public void registerPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!playerToMSEPlayerMap.containsKey(playerUUID)) {
            MSEPlayer msePlayer = new MSEPlayer(player);
            if (offlinePlayers.containsKey(playerUUID))
                msePlayer.initWithAttributeMap(offlinePlayers.get(playerUUID));
            playerToMSEPlayerMap.put(playerUUID, msePlayer);
            msePlayer.updateConsciousness();
        }
    }

    public void unregisterPlayer(UUID playerUUID) {
        if (playerToMSEPlayerMap.containsKey(playerUUID)) {
            MSEPlayer player = playerToMSEPlayerMap.get(playerUUID);
            offlinePlayers.put(playerUUID, player.getAttributesMap());
            playerToMSEPlayerMap.remove(playerUUID);
        }
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
