package de.julianSauer.minecraftSurvivalEvolved.visuals;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
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
     * @param mseEntity Reference to the entity which's values should be displayed in the scoreboard
     * @param player    Player that will see the scoreboard
     */
    public static void addPlayer(MSEEntity mseEntity, Player player) {
        ScoreboardUpdater scoreboardUpdater = getScoreboardFor(mseEntity);
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
     * @param mseEntity Reference to the entity which's values should be displayed in the scoreboard
     * @return A thread that is updating the scoreboard
     */
    private static ScoreboardUpdater getScoreboardFor(MSEEntity mseEntity) {

        UUID key = mseEntity.getUniqueID();
        if (activeScoreboards == null)
            activeScoreboards = new HashMap<>();
        else if (activeScoreboards.containsKey(key))
            return activeScoreboards.get(key);

        ScoreboardUpdater scoreboardUpdater = new ScoreboardUpdater(mseEntity);
        activeScoreboards.put(key, scoreboardUpdater);
        scoreboardUpdater.runTaskTimerAsynchronously(ThisPlugin.getInstance(), 0L, 100L);
        return scoreboardUpdater;

    }

    /**
     * Continuously updates the scoreboard of an entity.
     */
    private static class ScoreboardUpdater extends BukkitRunnable {

        public Scoreboard scoreboard;
        MSEEntity mseEntity;
        Objective objective;

        public ScoreboardUpdater(MSEEntity mseEntity) {
            this.mseEntity = mseEntity;

            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreboard.registerNewObjective(mseEntity.getEntityStats().getEntityType(), "Dummy");
            objective.setDisplayName(mseEntity.getEntityStats().getDefaultName());
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.getScore("Health").setScore((int) ((EntityLiving) mseEntity).getHealth()); // TODO: Replace with MSEEntity#getHealth()
            objective.getScore("Torpor").setScore(mseEntity.getTamingHandler().getTorpidity());
            objective.getScore("Food").setScore(mseEntity.getEntityStats().getCurrentFoodValue());
        }

        @Override
        public void run() {
            if (!((Entity) mseEntity).isAlive()) {
                this.cancel();
                return;
            }
            objective.getScore("Health").setScore((int) ((EntityLiving) mseEntity).getHealth());
            objective.getScore("Torpor").setScore(mseEntity.getTamingHandler().getTorpidity());
            objective.getScore("Food").setScore(mseEntity.getEntityStats().getCurrentFoodValue());
        }

        @Override
        public void cancel() {
            objective.unregister();
            activeScoreboards.remove(mseEntity.getUniqueID());
            super.cancel();
        }

    }

}
