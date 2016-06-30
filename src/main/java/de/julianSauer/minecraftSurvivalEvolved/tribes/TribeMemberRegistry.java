package de.julianSauer.minecraftSurvivalEvolved.tribes;

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

    private TribeMemberRegistry() {
        tribesOfPlayers = new HashMap<>();
        tribeRegistry = TribeRegistry.getTribeRegistry();
    }

    public static TribeMemberRegistry getTribeMemberRegistry() {
        if (instance == null)
            instance = new TribeMemberRegistry();
        return instance;
    }

    public void registerPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        for (Tribe tribe : tribeRegistry.getTribes()) {
            for (UUID member : tribe.getMemberUUIDs()) {
                if (member.equals(playerUUID)) {
                    TribeMember tribeMember = new TribeMember(player, tribe);
                    tribesOfPlayers.put(member, tribeMember);
                    return;
                }
            }
        }
        tribesOfPlayers.put(playerUUID, new TribeMember(player, null));
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
        return getTribeMember(player.getUniqueId());
    }

    public Tribe getTribeOf(UUID playerUUID) {
        return getTribeMember(playerUUID).getTribe();
    }

    public Tribe getTribeOf(Player player) {
        return getTribeOf(player.getUniqueId());
    }

}
