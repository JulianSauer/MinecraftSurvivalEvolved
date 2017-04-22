package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.gui.SignGUI;
import de.julianSauer.minecraftSurvivalEvolved.gui.inventories.InventoryGUI;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.HologramHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import de.julianSauer.minecraftSurvivalEvolved.utils.NameChanger;
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

import java.util.ArrayList;
import java.util.UUID;

/**
 * Implements taming functionality for entities that can be used to implement Tameable.
 * <p>
 * Note: The weird error printing is due to Minecraft catching all exceptions when loading an entity and simply removing
 * it instead of printing it there.
 */
public class TamingHandler<T extends EntityInsentient & MSEEntity> {

    private final T mseEntity;

    private EntityAttributes entityAttributes;

    private UnconsciousnessTimer unconsciousnessTimer;
    private boolean threadCurrentlyRunning;
    private long unconsciousnessUpdateInterval;

    private UUID tamer;
    private int tamingProgress;

    private boolean resumeConsciousness; // True if the entity was being tamed before a server restart

    public TamingHandler(T mseEntity) {
        this.mseEntity = mseEntity;
        threadCurrentlyRunning = false;
        unconsciousnessUpdateInterval = 100L;
        resumeConsciousness = false;
        entityAttributes = mseEntity.getEntityAttributes();
    }

    public void initWith(NBTTagCompound data) {

        if (!entityAttributes.isTamed()) {
            if (entityAttributes.isUnconscious()) {
                tamingProgress = data.getInt("MSETamingProgress");
                tamer = UUID.fromString(data.getString("MSETamer"));
            } else
                tamingProgress = 0;
        }
        resumeConsciousness = entityAttributes.isUnconscious();
        updateConsciousness();

    }

    public void saveData(NBTTagCompound data) {
        if (!entityAttributes.isTamed() && entityAttributes.isUnconscious() && !entityAttributes.isAlpha()) {
            data.setInt("MSETamingProgress", getTamingProgress());
            data.setString("MSETamer", getTamer().toString());
        }
    }

    public int getTamingProgress() {
        if (mseEntity.getEntityAttributes().isAlpha())
            MSEMain.getInstance().getLogger().info("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return tamingProgress;
    }


    public UUID getTamer() {
        return tamer;
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is successfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        if (!entityAttributes.isAlpha())
            tamer = lastDamager;
        increaseTorpidityBy(torpidityIncrease);
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity.
     *
     * @param torpidityIncrease
     */
    private void increaseTorpidityBy(int torpidityIncrease) {
        entityAttributes.setTorpidity(entityAttributes.getTorpidity() + torpidityIncrease);
        if (entityAttributes.getTorpidity() > entityAttributes.getMaxTorpidity())
            entityAttributes.setTorpidity(entityAttributes.getMaxTorpidity());
        updateConsciousness();
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        entityAttributes.setTorpidity(entityAttributes.getTorpidity() - torpidityDecrease);
        if (entityAttributes.getTorpidity() < 0)
            entityAttributes.setTorpidity(0);
        updateConsciousness();
    }

    /**
     * Makes the entity eat one narcotic out of the inventory.
     */
    public void feedNarcotics() {
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
     * Tames this entity.
     *
     * @param newOwner Player that will own the entity
     */
    public void forceTame(Player newOwner) {
        tamer = newOwner.getUniqueId();

        setSuccessfullyTamed();
    }

    /**
     * Sets the owner of an entity and wakes it up.
     *
     * @return False if the entity could not be tamed
     */
    private void setSuccessfullyTamed() {
        if (entityAttributes.isTameable() && tamer != null && !entityAttributes.isAlpha()) {
            entityAttributes.setTamed(true);

            NameChanger.markEntityForNameChange(tamer, mseEntity);
            SignGUI.sendSignToPlayer(Bukkit.getPlayer(tamer));

            Tribe tribe = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(tamer).getTribe();
            if (tribe != null) {
                entityAttributes.setTribe(tribe.getUniqueID());
                BarHandler.sendEntityTamedMessageTo(new ArrayList<>(tribe.getMembers()), Bukkit.getPlayer(tamer), mseEntity.getName());
            } else {
                BarHandler.sendEntityTamedMessageTo(Bukkit.getPlayer(tamer), mseEntity.getName());
                entityAttributes.setOwner(tamer);
            }

            entityAttributes.setTorpidity(0);
            updateConsciousness();
            mseEntity.getWorld().getWorld().playSound(mseEntity.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            mseEntity.setCustomName(entityAttributes.getDefaultName());
            (new InventoryGUI()).closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
            mseEntity.setPassiveGoals();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
            return;
        }

    }

    /**
     * Updates the consciousness of the entity.
     */
    public void updateConsciousness() {

        if (entityAttributes.isUnconscious() && entityAttributes.getTorpidity() <= 0) {
            // Wake up
            if (mseEntity.getCraftEntity() instanceof LivingEntity) {
                boolean removeNoneAlphas = !entityAttributes.isAlpha();
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(removeNoneAlphas);
            }
            if (!entityAttributes.isTamed() && tamer != null) {
                (new InventoryGUI()).closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
                tamer = null;
            }

            tamingProgress = 0;
            entityAttributes.setUnconscious(false);
            if (unconsciousnessTimer != null && threadCurrentlyRunning)
                unconsciousnessTimer.cancel();

        } else if ((!entityAttributes.isUnconscious() && entityAttributes.getFortitude() != null && entityAttributes.getTorpidity() >= entityAttributes.getFortitude())
                || resumeConsciousness) {
            // Fall asleep
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
            if (!mseEntity.getCraftEntity().isEmpty())
                mseEntity.getCraftEntity().eject();
            resumeConsciousness = false;
            entityAttributes.setUnconscious(true);
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimer(MSEMain.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    /**
     * Shows an eat animation by moving the head upwards and playing a sound.
     */
    private void eatAnimation() {
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

        private boolean initialized;

        public UnconsciousnessTimer() {

            threadCurrentlyRunning = true;
            this.initialized = false;

            if (mseEntity.getWorld().isLoaded(mseEntity.getChunkCoordinates()))
                init();

        }

        private void init() {
            this.initialized = true;
            if (entityAttributes.isTamed() || entityAttributes.isAlpha())
                hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), ChatColor.RED + "Unconscious");
            else {
                entityAttributes.startFoodTimer();
                mseEntity.setCustomName("");
                hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), getHologramText());
            }
        }

        public void run() {
            threadCurrentlyRunning = true;
            if (!mseEntity.isAlive()) {
                this.cancel();
                return;
            }

            if (!initialized)
                init();

            decreaseTorpidityBy(entityAttributes.getTorporDepletion());
            if (entityAttributes.isTamed() || entityAttributes.isAlpha())
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
            mseEntity.setCustomName(entityAttributes.getDefaultName());
            threadCurrentlyRunning = false;
            super.cancel();
        }

        /**
         * Returns the game ticks until the entity becomes conscious again.
         *
         * @return Remaining time in game ticks
         */
        public long getTimeUntilWakeUp() {
            return (entityAttributes.getTorpidity() / entityAttributes.getTorporDepletion()) * unconsciousnessUpdateInterval;
        }

        /**
         * Updates the progress if the entity is still tameable until it is tamed.
         */
        private void updateTamingProcess() {

            if (!entityAttributes.isTameable())
                return;

            int tamingIncrease = entityAttributes.getFoodTimer().updateHunger();
            if (tamingIncrease > 0) {
                eatAnimation();
            }
            tamingProgress += tamingIncrease;
            if (tamingProgress < 0)
                tamingProgress = 0;
            if (tamingProgress >= entityAttributes.getMaxTamingProgress())
                setSuccessfullyTamed();

        }

        private String[] getHologramText() {
            return new String[]{
                    ChatColor.DARK_AQUA + entityAttributes.getDefaultName(),
                    ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                    ChatColor.DARK_PURPLE + BarHandler.getProgressBar(entityAttributes.getTorpidity(), entityAttributes.getMaxTorpidity()),
                    ChatColor.GOLD + BarHandler.getProgressBar(getTamingProgress(), entityAttributes.getMaxTamingProgress())
            };
        }

    }

}
