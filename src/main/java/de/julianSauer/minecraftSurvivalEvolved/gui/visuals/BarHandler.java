package de.julianSauer.minecraftSurvivalEvolved.gui.visuals;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
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
        sendMessageTo(receiver, "You have tamed a " + entityType + "!", BarColor.GREEN, BarStyle.SOLID);
    }

    /**
     * Informs a tribe that a member has tamed an entity with success.
     *
     * @param tribeMembers Tribe members who receive the message
     * @param owner        The tribe member who tamed the entity
     * @param entityType   Type of the tamed entity
     */
    public static void sendEntityTamedMessageTo(List<OfflinePlayer> tribeMembers, Player owner, String entityType) {
        sendMessageTo(tribeMembers, "Your tribe member " + owner.getName() + " has tamed a " + entityType + "!", BarColor.GREEN, BarStyle.SOLID);
    }

    /**
     * Informs a player that one of his entities died.
     *
     * @param receiver   Player who receives the message
     * @param deadEntity Entity that died
     */
    public static void sendEntityDeathMessageTo(Player receiver, String deadEntity) {
        sendMessageTo(receiver, "Your " + deadEntity + " died", BarColor.RED, BarStyle.SOLID);
    }

    /**
     * Informs a tribe that one of their entities died.
     *
     * @param tribeMembers Tribe members who recieve the message
     * @param deadEntity   Entity that died
     */
    public static void sendEntityDeathMessageTo(List<OfflinePlayer> tribeMembers, String deadEntity) {
        sendMessageTo(tribeMembers, "Your " + deadEntity + " died", BarColor.RED, BarStyle.SOLID);
    }

    /**
     * Sends a message to a single person.
     *
     * @param receiver Player who receives the message
     * @param message  Content of the text
     * @param barColor Color of the Bar
     * @param barStyle Style of the Bar
     */
    private static void sendMessageTo(Player receiver, String message, BarColor barColor, BarStyle barStyle) {
        List receiverAsList = new ArrayList<OfflinePlayer>();
        receiverAsList.add(receiver);
        sendMessageTo(receiverAsList, message, barColor, barStyle);
    }

    /**
     * Sends a message to multiple players.
     *
     * @param receivers Players who receive the message
     * @param message   Content of the message
     * @param barColor  Color of the bar
     * @param barStyle  Style of the bar
     */
    private static void sendMessageTo(List<OfflinePlayer> receivers, String message, BarColor barColor, BarStyle barStyle) {
        BossBar bossBar = Bukkit.createBossBar(message, barColor, barStyle);
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
