package de.juliansauer.minecraftsurvivalevolved.gui.visuals;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayer;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player.MSEPlayerMap;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import de.juliansauer.minecraftsurvivalevolved.messages.BarMessages;
import de.juliansauer.minecraftsurvivalevolved.tribes.Tribe;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

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
     * @param tribe      Message will be written to tribe log
     * @param owner      The tribe member who tamed the entity
     * @param entityType Type of the tamed entity
     */
    public static void sendEntityTamedMessageTo(Tribe tribe, Player owner, String entityType) {
        BarMessages message = BarMessages.TRIBE_MEMBER_TAMED_SUCCESSFULLY.presetParams(owner.getName(), entityType);
        sendMessageTo(tribe.getMembers(), message);
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
     * @param tribe      Message will be written to tribe log
     * @param deadEntity Entity that died
     */
    public static void sendEntityDeathMessageTo(Tribe tribe, String deadEntity) {
        BarMessages message = BarMessages.TAME_DIED.presetParams(deadEntity);
        sendMessageTo(tribe.getMembers(), message);
        tribe.getLogger().log(message.getChatColor() + message.toString());
    }

    /**
     * Informs a tribe that one of their members died.
     *
     * @param tribe        Message will be written to tribe log
     * @param deathMessage Contains reason for death
     */
    public static void sendPlayerDeathMessageTo(Tribe tribe, String deathMessage) {
        BarMessages message = BarMessages.CUSTOM_MESSAGE;
        message.setCustomValues(deathMessage, BarColor.RED);
        sendMessageTo(tribe.getMembers(), message);
        tribe.getLogger().log(message.getChatColor() + message.toString());
    }

    public static void sendPlayerUnconsciousMessageTo(Player player) {

        MSEPlayer msePlayer = MSEPlayerMap.getPlayerRegistry().getMSEPlayer(player);
        if (msePlayer == null)
            return;

        BarMessages message = BarMessages.UNCONSCIOUS;
        float progress = ((float) msePlayer.getTorpidity()) / ((float) msePlayer.getMaxTorpidity());
        if (progress > 1)
            progress = 1;
        message.setPercentage(progress);
        BossBar bossBar = Bukkit.createBossBar(message.toString(), message.getColor(), message.getStyle());
        bossBar.setProgress(progress);
        bossBar.addPlayer(player);

        (new BukkitRunnable() {

            @Override
            public void run() {
                if (player.isDead() || !msePlayer.isUnconscious())
                    cancel();
                float percentage = ((float) msePlayer.getTorpidity()) / ((float) msePlayer.getMaxTorpidity());
                if (percentage > 1)
                    percentage = 1;
                BarColor updatedColor = message.setPercentage(percentage);
                bossBar.setColor(updatedColor);
                bossBar.setProgress(percentage);
            }

            @Override
            public void cancel() {
                bossBar.removeAll();
                super.cancel();
            }

        }).runTaskTimerAsynchronously(MSEMain.getInstance(), 0L, 100L);
    }

    /**
     * Sends a message to a single person.
     *
     * @param receiver   Player who receives the message
     * @param barMessage Content of the message
     */
    private static void sendMessageTo(Player receiver, BarMessages barMessage) {
        Set receiverAsList = new HashSet<>();
        receiverAsList.add(receiver);
        sendMessageTo(receiverAsList, barMessage);
    }

    /**
     * Sends a message to multiple players.
     *
     * @param receivers  Players who receive the message
     * @param barMessage Content of the message
     */
    private static void sendMessageTo(Set<OfflinePlayer> receivers, BarMessages barMessage) {
        BossBar bossBar = Bukkit.createBossBar(barMessage.toString(), barMessage.getColor(), barMessage.getStyle());
        receivers.stream()
                .filter(OfflinePlayer::isOnline)
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

        int progressInPercent = Math.round((currentValue / maxValue) * barSize);
        int i = 0;
        StringBuilder bar = new StringBuilder("|");
        for (; i < progressInPercent; i++)
            bar.append("=");
        for (; i <= barSize; i++)
            bar.append("-");
        bar.append("|");
        return bar.toString();

    }

}
