package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.gui.SignGUI;
import de.julianSauer.minecraftSurvivalEvolved.gui.inventories.InventoryGUI;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.HologramHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeRegistry;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
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
public class TamingHandler<T extends EntityInsentient & MSEEntity> implements Persistentable {

    private final T mseEntity;

    private int tamingProgress;

    private boolean unconscious;
    private boolean tamed;

    private int torporDepletion;
    private int torpidity;

    private UUID tamer;
    private UUID owner;
    private UUID tribe;

    private UnconsciousnessTimer unconsciousnessTimer;
    private boolean threadCurrentlyRunning;
    private long unconsciousnessUpdateInterval;

    private boolean initialized;
    private boolean resumeConsciousness; // True if the entity was being tamed before a server restart

    public TamingHandler(T mseEntity) {
        this.mseEntity = mseEntity;
        initialized = false;
        threadCurrentlyRunning = false;
        torporDepletion = 1;
        unconsciousnessUpdateInterval = 100L;
        resumeConsciousness = false;
    }

    @Override
    public void initWithDefaults() {
        initialized = true;
        tamed = false;
    }

    @Override
    public void initWith(NBTTagCompound data) {

        if (!data.getBoolean("MSEInitialized")) {
            initWithDefaults();
            return;
        }

        initialized = true;

        tamed = data.getBoolean("MSETamed");
        torpidity = data.getInt("MSETorpidity");
        unconscious = data.getBoolean("MSEUnconscious");
        resumeConsciousness = unconscious;

        if (tamed) {

            String ownerUUID = data.getString("MSEOwner");
            String tribeUUID = data.getString("MSETribe");
            if (ownerUUID != null && !ownerUUID.isEmpty())
                owner = UUID.fromString(ownerUUID);
            if (tribeUUID != null && !tribeUUID.isEmpty())
                tribe = UUID.fromString(tribeUUID);

        } else if (unconscious) {
            tamingProgress = data.getInt("MSETamingProgress");
            tamer = UUID.fromString(data.getString("MSETamer"));
        } else
            tamingProgress = 0;

        updateConsciousness();

    }

    @Override
    public void saveData(NBTTagCompound data) {
        data.setBoolean("MSETamed", isTamed());
        data.setBoolean("MSEUnconscious", unconscious);
        data.setInt("MSETorpidity", getTorpidity());
        if (isTamed()) {
            if (owner != null)
                data.setString("MSEOwner", getOwner().toString());
            if (tribe != null)
                data.setString("MSETribe", tribe.toString());
        } else if (isUnconscious() && !mseEntity.getGeneralBehaviorHandler().isAlpha()) {
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
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return tamed && !mseEntity.getGeneralBehaviorHandler().isAlpha();
    }

    public boolean isTameable() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return mseEntity.getGeneralBehaviorHandler().getBaseStats().isTameable() && !tamed && !mseEntity.getGeneralBehaviorHandler().isAlpha();
    }

    public boolean isUnconscious() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return unconscious;
    }

    public int getTamingProgress() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        if (mseEntity.getGeneralBehaviorHandler().isAlpha())
            MSEMain.getInstance().getLogger().info("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return tamingProgress;
    }

    public int getMaxTamingProgress() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        if (mseEntity.getGeneralBehaviorHandler().isAlpha())
            MSEMain.getInstance().getLogger().info("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return (int) Calculator.calculateLevelDependentStatFor(mseEntity.getGeneralBehaviorHandler().getBaseStats().getMaxTamingProgress(), mseEntity.getGeneralBehaviorHandler().getLevel(), mseEntity.getGeneralBehaviorHandler().getMultiplier());
    }

    public UUID getTamer() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return tamer;
    }

    public UUID getOwner() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        if (tamed && !mseEntity.getGeneralBehaviorHandler().isAlpha())
            return owner;
        return null;
    }

    public UUID getTribe() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return tribe;
    }

    public int getTorpidity() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return torpidity;
    }

    public int getMaxTorpidity() {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();
        return (int) Calculator.calculateLevelDependentStatFor(mseEntity.getGeneralBehaviorHandler().getBaseStats().getMaxTorpidity(), mseEntity.getGeneralBehaviorHandler().getLevel(), mseEntity.getGeneralBehaviorHandler().getMultiplier());
    }

    /**
     * Checks if a player is the owner or a member of the owning tribe of this entity.
     *
     * @param player The possible owner
     * @return True if the player is an owner
     */
    public boolean isOwner(UUID player) {
        if (owner != null)
            return owner.equals(player);
        else if (tribe != null) {
            Tribe owningTribe = TribeRegistry.getTribeRegistry().getTribe(tribe);
            if (owningTribe == null)
                return false;
            return owningTribe.isMember(player);
        }
        return false;
    }

    /**
     * Checks if this one and another entity have the same owners.
     *
     * @param mseEntity The other entity
     * @return True if they have the same owners
     */
    public boolean sameOwner(MSEEntity mseEntity) {
        if (owner != null && mseEntity.getTamingHandler().getOwner() != null)
            return owner.equals(mseEntity.getTamingHandler().getOwner());
        else if (tribe != null && mseEntity.getTamingHandler().getTribe() != null)
            return tribe.equals(mseEntity.getTamingHandler().getTribe());

        return false;
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is successfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        if (!mseEntity.getGeneralBehaviorHandler().isAlpha())
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
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        torpidity += torpidityIncrease;
        if (torpidity > getMaxTorpidity())
            torpidity = getMaxTorpidity();
        updateConsciousness();
    }

    public void decreaseTorpidityBy(int torpidityDecrease) {
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

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
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

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
        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        tamer = newOwner.getUniqueId();

        setSuccessfullyTamed();
    }

    /**
     * Sets the owner of an entity and wakes it up.
     *
     * @return False if the entity could not be tamed
     */
    private void setSuccessfullyTamed() {

        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        if (isTameable() && tamer != null && !mseEntity.getGeneralBehaviorHandler().isAlpha()) {
            tamed = true;

            NameChanger.markEntityForNameChange(tamer, mseEntity);
            SignGUI.sendSignToPlayer(Bukkit.getPlayer(tamer));

            Tribe tribe = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(tamer).getTribe();
            if (tribe != null) {
                this.tribe = tribe.getUniqueID();
                BarHandler.sendEntityTamedMessageTo(new ArrayList<>(tribe.getMembers()), Bukkit.getPlayer(tamer), mseEntity.getName());
            } else {
                owner = tamer;
                BarHandler.sendEntityTamedMessageTo(Bukkit.getPlayer(owner), mseEntity.getName());
            }

            decreaseTorpidityBy(getMaxTorpidity());
            mseEntity.getWorld().getWorld().playSound(mseEntity.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            mseEntity.setCustomName(mseEntity.getGeneralBehaviorHandler().getDefaultName());
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

        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

        if (isUnconscious() && torpidity <= 0) {
            // Wake up
            if (mseEntity.getCraftEntity() instanceof LivingEntity) {
                boolean removeNoneAlphas = !mseEntity.getGeneralBehaviorHandler().isAlpha();
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(removeNoneAlphas);
            }
            if (!isTamed() && tamer != null) {
                (new InventoryGUI()).closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
                tamer = null;
            }

            tamingProgress = 0;
            unconscious = false;
            if (unconsciousnessTimer != null && threadCurrentlyRunning)
                unconsciousnessTimer.cancel();

        } else if ((!isUnconscious() && mseEntity.getGeneralBehaviorHandler().getFortitude() != null && torpidity >= mseEntity.getGeneralBehaviorHandler().getFortitude())
                || resumeConsciousness) {
            // Fall asleep
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
            if (!mseEntity.getCraftEntity().isEmpty())
                mseEntity.getCraftEntity().eject();
            resumeConsciousness = false;
            unconscious = true;
            unconsciousnessTimer = new UnconsciousnessTimer();
            unconsciousnessTimer.runTaskTimer(MSEMain.getInstance(), 0, unconsciousnessUpdateInterval);
        }

    }

    /**
     * Shows an eat animation by moving the head upwards and playing a sound.
     */
    private void eatAnimation() {

        if (!initialized)
            new IllegalStateException(mseEntity.getName() + " has not been initialized properly.").printStackTrace();

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
            if (isTamed() || mseEntity.getGeneralBehaviorHandler().isAlpha())
                hologram = HologramHandler.spawnHologramAt(mseEntity.getLocation(), ChatColor.RED + "Unconscious");
            else {
                mseEntity.getGeneralBehaviorHandler().startFoodTimer();
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

            decreaseTorpidityBy(torporDepletion);
            if (isTamed() || mseEntity.getGeneralBehaviorHandler().isAlpha())
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
            mseEntity.setCustomName(mseEntity.getGeneralBehaviorHandler().getDefaultName());
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

            int tamingIncrease = mseEntity.getGeneralBehaviorHandler().updateHunger();
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
            if (!mseEntity.getGeneralBehaviorHandler().isInitialized())
                return new String[]{
                        ChatColor.DARK_AQUA + "", // Name dummy
                        ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                        ChatColor.DARK_PURPLE + "",
                        ChatColor.GOLD + ""
                };
            return new String[]{
                    ChatColor.DARK_AQUA + mseEntity.getGeneralBehaviorHandler().getDefaultName(),
                    ChatColor.DARK_AQUA + "Health: " + (int) mseEntity.getHealth() + "/" + (int) mseEntity.getMaxHealth(),
                    ChatColor.DARK_PURPLE + BarHandler.getProgressBar(getTorpidity(), getMaxTorpidity()),
                    ChatColor.GOLD + BarHandler.getProgressBar(getTamingProgress(), getMaxTamingProgress())
            };
        }

    }

}
