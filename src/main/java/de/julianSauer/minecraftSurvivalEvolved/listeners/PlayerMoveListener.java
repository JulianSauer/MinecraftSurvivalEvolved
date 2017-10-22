package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerMoveListener implements BasicEventListener {

    private static List<UUID> unconsciousPlayers;

    public PlayerMoveListener() {
        unconsciousPlayers = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (unconsciousPlayers.contains(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }

    public static void addUnconsciousPlayer(UUID player) {
        if (!unconsciousPlayers.contains(player))
            unconsciousPlayers.add(player);
    }

    public static void removeUnconsciousPlayer(UUID player) {
        unconsciousPlayers.remove(player);
    }

    // TODO: Save unconscious players
    public static void wakeAllPlayersUp() {
        for (UUID player : unconsciousPlayers)
            MSEPlayerMap.getPlayerRegistry().getMSEPlayer(player).getUnconsciousnessTimer().cancel();
    }

}
