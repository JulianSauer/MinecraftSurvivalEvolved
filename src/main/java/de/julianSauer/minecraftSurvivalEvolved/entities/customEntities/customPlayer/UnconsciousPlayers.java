package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer;

import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnconsciousPlayers {

    private static List<UUID> unconsciousPlayersList = new ArrayList<>();

    private UnconsciousPlayers() {
    }

    public static void addUnconsciousPlayer(Player player) {
        if (!unconsciousPlayersList.contains(player.getUniqueId())) {
            unconsciousPlayersList.add(player.getUniqueId());
            BarHandler.sendPlayerUnconsciousMessageTo(player);
        }
    }

    public static void removeUnconsciousPlayer(Player player) {
        unconsciousPlayersList.remove(player.getUniqueId());
        List<HumanEntity> viewers = new ArrayList<>(player.getInventory().getViewers());
        viewers.forEach(HumanEntity::closeInventory);
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
