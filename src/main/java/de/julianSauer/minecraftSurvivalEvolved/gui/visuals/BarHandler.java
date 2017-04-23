package de.julianSauer.minecraftSurvivalEvolved.gui.visuals;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.messages.BarMessages;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BarHandler {

    private BarHandler() {
    }

    /**
     * Informs a player that he has tamed an entity with success.
     *
     * @param receiver   Player who receives the message
     * @param entityType Type of the tamed entity
     */
    public static void sendEntityTamedMessageTo(Player receiver, String entityType) {
        sendMessageTo(receiver, BarMessages.TAMED_SUCCESSFULLY.presetParams(entityType));
    }

    /**
     * Informs a tribe that a member has tamed an entity with success.
     *
     * @param tribe        Message will be written to tribe log
     * @param tribeMembers Tribe members who receive the message
     * @param owner        The tribe member who tamed the entity
     * @param entityType   Type of the tamed entity
     */
    public static void sendEntityTamedMessageTo(Tribe tribe, List<OfflinePlayer> tribeMembers, Player owner, String entityType) {
        BarMessages message = BarMessages.TRIBE_MEMBER_TAMED_SUCCESSFULLY.presetParams(owner.getName(), entityType);
        sendMessageTo(tribeMembers, message);
        tribe.getLogger().log(message.getChatColor() + message.toString());
    }

    /**
     * Informs a player that one of his entities died.
     *
     * @param receiver   Player who receives the message
     * @param deadEntity Entity that died
     */
    public static void sendEntityDeathMessageTo(Player receiver, String deadEntity) {
        sendMessageTo(receiver, BarMessages.TAME_DIED.presetParams(deadEntity));
    }

    /**
     * Informs a tribe that one of their entities died.
     *
     * @param tribe        Message will be written to tribe log
     * @param tribeMembers Tribe members who recieve the message
     * @param deadEntity   Entity that died
     */
    public static void sendEntityDeathMessageTo(Tribe tribe, List<OfflinePlayer> tribeMembers, String deadEntity) {
        BarMessages message = BarMessages.TAME_DIED.presetParams(deadEntity);
        sendMessageTo(tribeMembers, message);
        tribe.getLogger().log(message.getChatColor() + message.toString());
    }

    /**
     * Sends a message to a single person.
     *
     * @param receiver   Player who receives the message
     * @param barMessage Content of the message
     */
    private static void sendMessageTo(Player receiver, BarMessages barMessage) {
        List receiverAsList = new ArrayList<OfflinePlayer>();
        receiverAsList.add(receiver);
        sendMessageTo(receiverAsList, barMessage);
    }

    /**
     * Sends a message to multiple players.
     *
     * @param receivers  Players who receive the message
     * @param barMessage Content of the message
     */
    private static void sendMessageTo(List<OfflinePlayer> receivers, BarMessages barMessage) {
        BossBar bossBar = Bukkit.createBossBar(barMessage.toString(), barMessage.getColor(), barMessage.getStyle());
        receivers.stream()
                .filter(player -> player.isOnline())
                .forEach(player -> bossBar.addPlayer((Player) player));

        (new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.removeAll();
            }
        }).runTaskTimerAsynchronously(MSEMain.getInstance(), 100L, 0L);
    }

    /**
     * Creates a string representing a default progress bar.
     *
     * @param currentValue Defines how much of the bar will be filled out
     * @param maxValue     Defines 100%
     * @return Progress bar as string with a size of 10
     */
    public static String getProgressBar(float currentValue, float maxValue) {
        return getProgressBar(currentValue, maxValue, 10);
    }

    /**
     * Creates a string representing a progress bar.
     *
     * @param currentValue Defines how much of the bar will be filled out
     * @param maxValue     Defines 100%
     * @param barSize      Number of elements that the bar will contain
     * @return Progress bar as string
     */
    public static String getProgressBar(float currentValue, float maxValue, float barSize) {

        if (currentValue > maxValue)
            currentValue = maxValue;
        else if (currentValue < 0)
            currentValue = 0;

        String bar = "|";
        int progressInPercent = Math.round((currentValue / maxValue) * barSize);
        int i = 0;
        for (; i < progressInPercent; i++)
            bar += "=";
        for (; i <= barSize; i++)
            bar += "-";
        return bar + "|";

    }

}
