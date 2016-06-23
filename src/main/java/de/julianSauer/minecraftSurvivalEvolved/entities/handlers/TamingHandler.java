package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import de.julianSauer.minecraftSurvivalEvolved.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.visuals.HologramHandler;
import de.julianSauer.minecraftSurvivalEvolved.visuals.inventories.InventoryGUI;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Implements taming functionality for entities that can be used to implement Tameable.
 */
public class TamingHandler<T extends EntityInsentient & MSEEntity> implements Persistentable {

    private T mseEntity;

    private int tamingProgress;

    private boolean unconscious;
    private boolean tamed;

    private int torporDepletion;
    private int torpidity;

    private UUID owner;
    private UUID tamer;

    private UnconsciousnessTimer unconsciousnessTimer;
    private boolean threadCurrentlyRunning;
    private long unconsciousnessUpdateInterval;

    private boolean initialized;

    public TamingHandler(T mseEntity) {
        this.mseEntity = mseEntity;
        initialized = false;
        threadCurrentlyRunning = false;
        torporDepletion = 1;
        unconsciousnessUpdateInterval = 100L;
    }

    @Override
    public void initWithDefaults() {
        initialized = true;
        unconscious = false;
        tamed = false;
        torpidity = 0;
        tamingProgress = 0;
    }

    @Override
    public void initWith(NBTTagCompound data) {

        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        initialized = true;

        unconscious = data.getBoolean("MSEUnconscious");
        tamed = data.getBoolean("MSETamed");

        if (unconscious)
            torpidity = data.getInt("MSETorpidity");
        else
            torpidity = 0;

        if (tamed)
            owner = UUID.fromString(data.getString("MSEOwner"));
        else if (unconscious) {
            tamingProgress = data.getInt("MSETamingProgress");
            tamer = UUID.fromString(data.getString("MSETamer"));
        } else
            tamingProgress = 0;

    }

    @Override
    public void saveData(NBTTagCompound data) {
        if (!isInitialized())
            initWithDefaults();

        data.setBoolean("MSEUnconscious", isUnconscious());
        data.setBoolean("MSETamed", isTamed());
        if (isTamed()) {
            data.setString("MSEOwner", getOwner().toString());
        } else if (isUnconscious()) {
            data.setInt("MSETamingProgress", getTamingProgress());
            data.setString("MSETamer", getTamer().toString());
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public boolean isTamed() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        return tamed && !mseEntity.getEntityStats().isAlpha();
    }

    public boolean isTameable() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        return mseEntity.getEntityStats().getBaseStats().isTameable() && !tamed && !mseEntity.getEntityStats().isAlpha();
    }

    public boolean isUnconscious() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        return unconscious && !mseEntity.getEntityStats().isAlpha();
    }

    public int getTamingProgress() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        if (mseEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return tamingProgress;
    }

    public int getMaxTamingProgress() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        if (mseEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return (int) Calculator.calculateLevelDependentStatFor(mseEntity.getEntityStats().getBaseStats().getMaxTamingProgress(), mseEntity.getEntityStats().getLevel(), mseEntity.getEntityStats().getMultiplier());
    }

    public UUID getOwner() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        if (tamed && !mseEntity.getEntityStats().isAlpha())
            return owner;
        return null;
    }

    public UUID getTamer() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        return tamer;
    }

    public int getTorpidity() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        if (mseEntity.getEntityStats().isAlpha())
            throw new IllegalStateException("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return torpidity;
    }

    public int getMaxTorpidity() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");
        return (int) Calculator.calculateLevelDependentStatFor(mseEntity.getEntityStats().getBaseStats().getMaxTorpidity(), mseEntity.getEntityStats().getLevel(), mseEntity.getEntityStats().getMultiplier());
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is successfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        if (mseEntity.getEntityStats().isAlpha())
            return;
        tamer = lastDamager;
        increaseTorpidityBy(torpidityIncrease);
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity.
     *
     * @param torpidityIncrease
     */
    private void increaseTorpidityBy(int torpidityIncrease) {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        torpidity += torpidityIncrease;
        if (torpidity > getMaxTorpidity())
            torpidity = getMaxTorpidity();
        updateConsciousness();
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        torpidity -= torpidityDecrease;
        if (torpidity < 0)
            torpidity = 0;
        updateConsciousness();
    }

    /**
     * Makes the entity eat one narcotic out of the inventory.
     */
    public void feedNarcotics() {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        Inventory inventory = mseEntity.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.POTION) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equalsIgnoreCase("Narcotics")) {
                    if (item.getAmount() <= 1)
                        item = new ItemStack(Material.AIR);
                    else
                        item.setAmount(item.getAmount() - 1);
                    inventory.setItem(i, item);
                    eatAnimation();
                    increaseTorpidityBy(20);
                    break;
                }
            }
        }
    }

    /**
     * Tames this entity. Ignores if the entity is tameable or already tamed.
     *
     * @param newOwner Player that will own the entity
     */
    public void forceTame(Player newOwner) {
        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        tamed = true;
        this.owner = newOwner.getUniqueId();
        decreaseTorpidityBy(getMaxTorpidity());
        mseEntity.getWorld().getWorld().playSound(mseEntity.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
        mseEntity.setCustomName(mseEntity.getEntityStats().getDefaultName());
        BarHandler.sendTamedTextTo(newOwner, mseEntity.getName());
        InventoryGUI.closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
        mseEntity.setPassiveGoals();
    }

    /**
     * Sets the owner of an entity and wakes it up.
     *
     * @return False if the entity could not be tamed
     */
    private boolean setSuccessfullyTamed() {

        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        if (isTameable() && tamer != null && !mseEntity.getEntityStats().isAlpha()) {
            tamed = true;
            owner = tamer;
            decreaseTorpidityBy(getMaxTorpidity());
            mseEntity.getWorld().getWorld().playSound(mseEntity.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            mseEntity.setCustomName(mseEntity.getEntityStats().getDefaultName());
            BarHandler.sendTamedTextTo(Bukkit.getPlayer(owner), mseEntity.getName());
            InventoryGUI.closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
            mseEntity.setPassiveGoals();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    private void updateConsciousness() {

        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        if (mseEntity.getEntityStats().isAlpha())
            return;

        if (isUnconscious() && torpidity <= 0) {
            unconscious = false;
            if (unconsciousnessTimer != null && threadCurrentlyRunning) {
                unconsciousnessTimer.cancel();
            }
        } else if (!isUnconscious() && torpidity >= mseEntity.getEntityStats().getFortitude()) {
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimerAsynchronously(MSEMain.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    /**
     * Shows an eat animation by moving the head upwards and playing a sound.
     */
    private void eatAnimation() {

        if (!initialized)
            throw new IllegalStateException(mseEntity.getName() + " has not been initialized properly.");

        mseEntity.setPitchWhileTaming(-30F);
        mseEntity.getWorld().getWorld().playSound(mseEntity.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
        (new BukkitRunnable() {
            @Override
            public void run() {
                mseEntity.setPitchWhileTaming(0F);
                this.cancel();
            }
        }).runTaskTimerAsynchronously(MSEMain.getInstance(), 4L, 0L);

    }

    class UnconsciousnessTimer extends BukkitRunnable {

        UUID hologram;

        public UnconsciousnessTimer() {
            threadCurrentlyRunning = true;

            if (!isTamed()) {
                mseEntity.getEntityStats().startFoodTimer();
                mseEntity.setCustomName("");
                hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), getHologramText());
            } else
                hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), ChatColor.RED + "Unconscious");
        }

        public void run() {
            threadCurrentlyRunning = true;
            if (!mseEntity.isAlive()) {
                this.cancel();
                return;
            }

            decreaseTorpidityBy(torporDepletion);
            if (isTamed())
                return;

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
            mseEntity.setCustomName(mseEntity.getEntityStats().getDefaultName());
            threadCurrentlyRunning = false;
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

            int tamingIncrease = mseEntity.getEntityStats().updateHunger();
            if (tamingIncrease > 0) {
                eatAnimation();
            }
            tamingProgress += tamingIncrease;
            if (tamingProgress < 0)
                tamingProgress = 0;
            if (tamingProgress >= getMaxTamingProgress())
                setSuccessfullyTamed();

        }

        private String[] getHologramText() {
            return new String[]{
                    ChatColor.DARK_AQUA + mseEntity.getEntityStats().getDefaultName(),
                    ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                    ChatColor.DARK_PURPLE + BarHandler.getProgressBar(getTorpidity(), getMaxTorpidity()),
                    ChatColor.GOLD + BarHandler.getProgressBar(getTamingProgress(), getMaxTamingProgress())
            };
        }

    }

}
