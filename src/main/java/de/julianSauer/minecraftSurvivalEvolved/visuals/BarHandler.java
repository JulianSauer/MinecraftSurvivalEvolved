package de.julianSauer.minecraftSurvivalEvolved.visuals;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BarHandler {

    private BarHandler() {
    }

    /**
     * Sends a message to a player that he has tamed an entity with success.
     *
     * @param receiver   Player who receives the message
     * @param entityType Type of the tamed entity to be mentioned in the message
     */
    public static void sendTamedTextTo(Player receiver, String entityType) {
        BossBar bossBar = Bukkit.createBossBar("You have tamed a " + entityType + "!", BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(receiver);
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
