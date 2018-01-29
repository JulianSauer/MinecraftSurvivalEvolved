package de.juliansauer.minecraftsurvivalevolved.gui.visuals;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.AttributedEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.Unconsciousable;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardHandler {

    private static Map<UUID, ScoreboardUpdater> activeScoreboards;

    private static Scoreboard emptyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    private ScoreboardHandler() {
    }

    /**
     * Adds a player to the scoreboard of this entity. If the scoreboard doesn't exist yet, a new one will be created
     * and added to activeScoreboards as a cache.
     *
     * @param entity Reference to the entity which's values should be displayed in the scoreboard
     * @param player    Player that will see the scoreboard
     */
    public static<T extends AttributedEntity & Unconsciousable> void addPlayer(T entity, Player player) {
        ScoreboardUpdater scoreboardUpdater = getScoreboardFor(entity);
        player.setScoreboard(scoreboardUpdater.scoreboard);
    }

    /**
     * Resets a player's scoreboard.
     *
     * @param player Player that won't see a scoreboard anymore
     */
    public static void removePlayer(Player player) {
        player.setScoreboard(emptyScoreboard);
    }

    /**
     * Returns the scoreboard of this entity from cache or creates a new one, if it doesn't exist yet.
     *
     * @param entity Reference to the entity which's values should be displayed in the scoreboard
     * @return A thread that is updating the scoreboard
     */
    private static <T extends AttributedEntity & Unconsciousable> ScoreboardUpdater getScoreboardFor(T entity) {

        UUID key = entity.getUniqueID();
        if (activeScoreboards == null)
            activeScoreboards = new HashMap<>();
        else if (activeScoreboards.containsKey(key))
            return activeScoreboards.get(key);

        ScoreboardUpdater scoreboardUpdater = new ScoreboardUpdater(entity);
        activeScoreboards.put(key, scoreboardUpdater);
        scoreboardUpdater.runTaskTimerAsynchronously(MSEMain.getInstance(), 0L, 100L);
        return scoreboardUpdater;

    }

    /**
     * Continuously updates the scoreboard of an entity.
     */
    private static class ScoreboardUpdater<T extends AttributedEntity & Unconsciousable> extends BukkitRunnable {

        public final Scoreboard scoreboard;
        final T entity;
        final Objective objective;

        public ScoreboardUpdater(T entity) {
            this.entity = entity;

            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreboard.registerNewObjective(entity.getName(), "Dummy");
            objective.setDisplayName(entity.getDefaultName());
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.getScore("Health").setScore((int) entity.getHealth()); // TODO: Replace with MSEEntity#getHealth()
            objective.getScore("Torpor").setScore(entity.getTorpidity());
            objective.getScore("Food").setScore(entity.getFood());
        }

        @Override
        public void run() {
            if (!entity.getHandle().isAlive()) {
                this.cancel();
                return;
            }
            objective.getScore("Health").setScore((int) entity.getHealth());
            objective.getScore("Torpor").setScore(entity.getTorpidity());
            objective.getScore("Food").setScore(entity.getFood());
        }

        @Override
        public void cancel() {
            objective.unregister();
            activeScoreboards.remove(entity.getUniqueID());
            super.cancel();
        }

    }

}
