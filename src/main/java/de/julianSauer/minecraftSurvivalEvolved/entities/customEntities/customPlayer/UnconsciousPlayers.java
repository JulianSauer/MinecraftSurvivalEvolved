package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnconsciousPlayers {

    private static List<UUID> unconsciousPlayersList = new ArrayList<>();

    private UnconsciousPlayers() {
    }

    public static void addUnconsciousPlayer(UUID player) {
        if (!unconsciousPlayersList.contains(player))
            unconsciousPlayersList.add(player);
    }

    public static void removeUnconsciousPlayer(UUID player) {
        unconsciousPlayersList.remove(player);
    }

    public static boolean contains(UUID player) {
        return unconsciousPlayersList.contains(player);
    }

    // TODO: Save unconscious players
    public static void wakeAllPlayersUp() {
        for (UUID player : unconsciousPlayersList)
            MSEPlayerMap.getPlayerRegistry().getMSEPlayer(player).getUnconsciousnessTimer().cancel();
    }

}
