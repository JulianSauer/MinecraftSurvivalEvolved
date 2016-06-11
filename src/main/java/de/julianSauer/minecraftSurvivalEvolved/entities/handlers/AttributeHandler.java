package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.BaseStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import de.julianSauer.minecraftSurvivalEvolved.utils.RandomGenerator;
import de.julianSauer.minecraftSurvivalEvolved.visuals.HologramHandler;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

/**
 * Implements generic functionality for entities that can be used to implement Tameable.
 */
public class AttributeHandler<T extends EntityInsentient & MSEEntity> {

    private T tameableEntity;

    private BaseStats baseStats;

    // Current stats
    private boolean unconscious;

    private int torpidity;
    private int tamingProgress;
    private Integer level;

    private float currentXp;

    private String name;

    private UUID owner;
    private UUID tamer;
    private String tamerName;

    // General stats
    private boolean tamed;

    private int tamingProgressInterval;
    private int torporDepletion;
    private int alphaPredatorMultiplier; // attributes multiplied by this value; set to 1 if not an alpha

    private String entityType;

    private UnconsciousnessTimer unconsciousnessTimer;
    private boolean currentlyRunning;
    private long unconsciousnessUpdateInterval;

    public AttributeHandler(T tameableEntity) {

        currentlyRunning = false;

        this.tameableEntity = tameableEntity;
        entityType = tameableEntity.getName();

        baseStats = BaseStats.getBaseStatsFor(tameableEntity.getName());
        if (RandomGenerator.getRandomInt(101) <= baseStats.getAlphaProbability()) {
            alphaPredatorMultiplier = 4;
        } else {
            alphaPredatorMultiplier = 1;
        }

        tamed = false;
        unconscious = false;

        torpidity = 0;
        torporDepletion = 1;
        tamingProgress = 0;
        tamingProgressInterval = 1;
        unconsciousnessUpdateInterval = 100L;
        currentXp = 0;

        updateLevel(0);

    }

    public boolean isTamed() {
        return tamed && !this.isAlpha();
    }

    public boolean isAlpha() {
        return (alphaPredatorMultiplier != 1);
    }

    public boolean isTameable() {
        return baseStats.isTameable() && !tamed && !this.isAlpha();
    }

    public boolean isUnconscious() {
        return unconscious && !this.isAlpha();
    }

    public int getTorpidity() {
        if (this.isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + tameableEntity.getName() + " at x:" + tameableEntity.locX + " y:" + tameableEntity.locY + " z:"
                    + tameableEntity.locZ + ")");
        return torpidity;
    }

    public int getMaxTorpidity() {
        return (int) calculateLevelDependentStatFor(baseStats.getMaxTorpidity());
    }

    public double getDamage() {
        return calculateLevelDependentStatFor(baseStats.getDamage());
    }

    public float getSpeed() {
        float speedMultiplier = baseStats.getLevelMultiplier();
        speedMultiplier /= 2;
        return (float) calculateLevelDependentStatFor(baseStats.getSpeed(), speedMultiplier);
    }

    public int getTamingProgress() {
        if (this.isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + tameableEntity.getName() + " at x:" + tameableEntity.locX + " y:" + tameableEntity.locY + " z:"
                    + tameableEntity.locZ + ")");
        return tamingProgress;
    }

    public int getLevel() {
        return level;
    }

    private int getFortitude() {
        return (int) calculateLevelDependentStatFor(baseStats.getFortitude());
    }

    public float getCurrentXp() {
        return currentXp;
    }

    public float getXpUntilLevelUp() {
        return baseStats.getXpUntilLevelUp();
    }

    public String getName() {
        System.out.println("get name: " + name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
        tameableEntity.setCustomName(name);
    }

    public String getDefaultname() {
        if (this.isAlpha())
            return "Alpha " + entityType + " (" + level + ")";
        else
            return entityType + " (" + level + ")";
    }

    public int getMaxTamingProgress() {
        if (this.isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + tameableEntity.getName() + " at x:" + tameableEntity.locX + " y:" + tameableEntity.locY + " z:"
                    + tameableEntity.locZ + ")");
        return (int) calculateLevelDependentStatFor(baseStats.getMaxTamingProgress());
    }

    public UUID getOwner() {
        if (tamed && !this.isAlpha())
            return owner;
        return null;
    }

    public List<Material> getPreferredFood() {
        if (this.isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + tameableEntity.getName() + " at x:" + tameableEntity.locX + " y:" + tameableEntity.locY + " z:"
                    + tameableEntity.locZ + ")");
        return baseStats.getPreferredFood();
    }

    public List<Material> getMineableBlocks() {
        if (this.isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + tameableEntity.getName() + " at x:" + tameableEntity.locX + " y:" + tameableEntity.locY + " z:"
                    + tameableEntity.locZ + ")");
        return baseStats.getMineableBlocks();
    }

    /**
     * Calculates the value for an attribute level dependent.
     *
     * @param baseValue Normal value of the entity
     * @return Level dependent value of the entity
     */
    private double calculateLevelDependentStatFor(double baseValue) {
        return calculateLevelDependentStatFor(baseValue, baseStats.getLevelMultiplier());
    }

    /**
     * Calculates the value for an attribute with a custom multiplier.
     *
     * @param baseValue  Normal value of the entity
     * @param multiplier Level multiplier of the entity
     * @return Multiplier dependet value of the entity
     */
    private double calculateLevelDependentStatFor(double baseValue, float multiplier) {
        return (1 + (level - 1) * multiplier) * baseValue * alphaPredatorMultiplier;
    }

    /**
     * Increases the level. Can also be used for initialization.
     *
     * @param levelIncrease Use 0 to initialize
     */
    private void updateLevel(int levelIncrease) {
        if (level == null)
            level = RandomGenerator.getRandomInt(baseStats.getLevelCap()) + 1;
        if (levelIncrease > 0)
            level += levelIncrease;
        if (!tamed) {
            setName(getDefaultname());
        }
    }

    /**
     * Increases the xp for this entity. Also handles level ups.
     *
     * @param xpIncrease
     */
    public void increaseXp(float xpIncrease) {
        currentXp += xpIncrease;
        float currentXpForLevelUp = (float) calculateLevelDependentStatFor(baseStats.getXpUntilLevelUp());
        while (currentXp >= currentXpForLevelUp) {
            currentXp -= currentXpForLevelUp;
            updateLevel(1);
            currentXpForLevelUp = (float) calculateLevelDependentStatFor(baseStats.getXpUntilLevelUp());
        }
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is succesfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager, String lastDamagerName) {

        if (this.isAlpha())
            return;
        tamer = lastDamager;
        tamerName = lastDamagerName;
        torpidity += torpidityIncrease;
        if (torpidity > getMaxTorpidity())
            torpidity = getMaxTorpidity();
        updateConsciousness();

    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        torpidity -= torpidityDecrease;
        if (torpidity < 0)
            torpidity = 0;
        updateConsciousness();
    }

    /**
     * Sets the owner of an entity and wakes it up.
     *
     * @return False if the entity could not be tamed
     */
    private boolean setSuccessfullyTamed() {

        if (isTameable() && tamer != null && !this.isAlpha()) {
            tamed = true;
            owner = tamer;
            decreaseTorpidityBy(getMaxTorpidity());
            tameableEntity.getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            setName(getDefaultname());
            return true;
        } else {
            return false;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    private void updateConsciousness() {

        if (this.isAlpha())
            return;

        if (isUnconscious() && torpidity <= 0) {
            unconscious = false;
            if (unconsciousnessTimer != null && currentlyRunning) {
                unconsciousnessTimer.cancel();
            }
        } else if (!isUnconscious() && torpidity >= getFortitude()) {
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(ThisPlugin.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    /**
     * Tries to eat food from the inventory.
     *
     * @return True if food was found.
     */
    private boolean updateHunger() {

        if (this.isAlpha())
            return false;

        Inventory inventory = tameableEntity.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null)
                continue;
            if (baseStats.getPreferredFood().contains(item.getType())) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);

                // Eat animation
                tameableEntity.setPitchWhileTaming(-30F);
                tameableEntity.getWorld().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                (new BukkitRunnable() {
                    @Override
                    public void run() {
                        tameableEntity.setPitchWhileTaming(0F);
                        this.cancel();
                    }
                }).runTaskTimerAsynchronously(ThisPlugin.getInstance(), 4L, 0L);

                return true;
            }
        }
        return false;
    }

    private Location getLocation() {
        return new Location(tameableEntity.getWorld().getWorld(), tameableEntity.locX, tameableEntity.locY, tameableEntity.locZ);
    }

    class UnconsciousnessTimer extends BukkitRunnable {

        UUID hologram;

        public UnconsciousnessTimer() {
            World world = tameableEntity.getWorld().getWorld();
            setName("");
            hologram = HologramHandler.spawnHologramAt(getLocation(), getHologramText());
            currentlyRunning = true;
        }

        public void run() {
            currentlyRunning = true;
            if (!tameableEntity.isAlive())
                this.cancel();

            decreaseTorpidityBy(torporDepletion);
            updateTamingProcess();
            if (!HologramHandler.updateHologram(hologram, getHologramText()))
                this.cancel();
        }

        /**
         * Stops thread and despawns the hologram.
         */
        @Override
        public void cancel() {
            HologramHandler.despawnHologram(hologram);
            updateLevel(0); // Resets the name
            currentlyRunning = false;
            super.cancel();
        }

        /**
         * Returns the game ticks until the entity becomes conscious again.
         *
         * @return Remaining time in game ticks
         */
        public long getTimeUntilWakeUp() {
            return (torpidity / torporDepletion) * unconsciousnessUpdateInterval;
        }

        /**
         * Updates the progress if the entity is still tameable until it is tamed.
         */
        private void updateTamingProcess() {

            if (!isTameable())
                return;

            if (updateHunger())
                tamingProgress += tamingProgressInterval;
            else
                tamingProgress = (tamingProgress - tamingProgressInterval) < 0 ? 0 : tamingProgress - tamingProgressInterval;

            if (tamingProgress >= getMaxTamingProgress()) {
                setSuccessfullyTamed();
            }

        }

        private String[] getHologramText() {
            return new String[]{
                    ChatColor.DARK_AQUA + getDefaultname(),
                    ChatColor.DARK_AQUA + "Health: " + (int) tameableEntity.getHealth() + "/" + (int) tameableEntity.getMaxHealth(),
                    ChatColor.DARK_PURPLE + HologramHandler.getProgressBar(getTorpidity(), getMaxTorpidity()),
                    ChatColor.GOLD + HologramHandler.getProgressBar(getTamingProgress(), getMaxTamingProgress())
            };
        }

    }

}
