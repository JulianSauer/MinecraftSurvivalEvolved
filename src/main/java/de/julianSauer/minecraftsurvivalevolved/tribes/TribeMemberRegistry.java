package de.juliansauer.minecraftsurvivalevolved.tribes;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Maps online players to tribes.
 */
public class TribeMemberRegistry {

    private static TribeMemberRegistry instance;

    private TribeRegistry tribeRegistry;

    private Map<UUID, TribeMember> tribesOfPlayers;

    private MSEPlayerMap msePlayerMap;

    private TribeMemberRegistry() {
        tribesOfPlayers = new HashMap<>();
        tribeRegistry = TribeRegistry.getTribeRegistry();
        msePlayerMap = MSEPlayerMap.getPlayerRegistry();
    }

    public static TribeMemberRegistry getTribeMemberRegistry() {
        if (instance == null)
            instance = new TribeMemberRegistry();
        return instance;
    }

    public void registerPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        TribeMember tribeMember = msePlayerMap.getMSEPlayer(playerUUID);
        for (Tribe tribe : tribeRegistry.getTribes()) {
            for (UUID member : tribe.getMemberUUIDs()) {
                if (member.equals(playerUUID)) {
                    tribeMember.setTribe(tribe);
                    tribesOfPlayers.put(member, tribeMember);
                    return;
                }
            }
        }
        tribesOfPlayers.put(playerUUID, tribeMember);
    }

    public void unregisterPlayer(UUID playerUUID) {
        if (tribesOfPlayers.containsKey(playerUUID))
            tribesOfPlayers.remove(playerUUID);
    }

    public void unregisterPlayer(Player player) {
        unregisterPlayer(player.getUniqueId());
    }

    public TribeMember getTribeMember(UUID playerUUID) {
        return tribesOfPlayers.get(playerUUID);
    }

    public TribeMember getTribeMember(Player player) {
        if (player == null || !player.isOnline())
            return null;
        return getTribeMember(player.getUniqueId());
    }

    public Tribe getTribeOf(UUID playerUUID) {
        return getTribeMember(playerUUID).getTribe();
    }

    public Tribe getTribeOf(Player player) {
        return getTribeOf(player.getUniqueId());
    }

}
