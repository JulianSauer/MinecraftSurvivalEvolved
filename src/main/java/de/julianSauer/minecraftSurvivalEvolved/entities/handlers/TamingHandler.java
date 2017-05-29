package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.TameableEntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimerTameable;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.gui.SignGUI;
import de.julianSauer.minecraftSurvivalEvolved.gui.inventories.InventoryGUI;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.BarHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import de.julianSauer.minecraftSurvivalEvolved.utils.NameChanger;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Contains taming data for an {@link MSEEntity} and manages the taming process together with {@link UnconsciousnessTimerTameable}.
 */
public class TamingHandler<T extends EntityInsentient & MSEEntity> {

    private final T mseEntity;

    private TameableEntityAttributes tameableEntityAttributes;

    private UnconsciousnessTimerTameable unconsciousnessTimerTameable;

    private UUID tamer;
    private int tamingProgress;

    private boolean resumeConsciousness; // True if the entity was being tamed before a server restart

    public TamingHandler(T mseEntity) {
        this.mseEntity = mseEntity;
        resumeConsciousness = false;
        tameableEntityAttributes = mseEntity.getTameableEntityAttributes();
    }

    public void initWith(NBTTagCompound data) {

        if (!tameableEntityAttributes.isTamed()) {
            if (tameableEntityAttributes.isUnconscious()) {
                tamingProgress = data.getInt("MSETamingProgress");
                tamer = UUID.fromString(data.getString("MSETamer"));
            } else
                tamingProgress = 0;
        }
        resumeConsciousness = tameableEntityAttributes.isUnconscious();
        updateConsciousness();

    }

    public void saveData(NBTTagCompound data) {
        if (!tameableEntityAttributes.isTamed() && tameableEntityAttributes.isUnconscious() && !tameableEntityAttributes.isAlpha()) {
            data.setInt("MSETamingProgress", getTamingProgress());
            data.setString("MSETamer", getTamer().toString());
        }
    }

    public int getTamingProgress() {
        if (mseEntity.getTameableEntityAttributes().isAlpha())
            MSEMain.getInstance().getLogger().info("Tried accessing functionality that is limited to non-alpha entities ("
                    + mseEntity.getName() + " at x:" + mseEntity.locX + " y:" + mseEntity.locY + " z:"
                    + mseEntity.locZ + ")");
        return tamingProgress;
    }

    public void setTamingProgress(int tamingProgress) {
        this.tamingProgress = tamingProgress;
    }


    public UUID getTamer() {
        return tamer;
    }

    /**
     * Increases the torpidity and updates the consciousness of an entity. May start a taming progress.
     *
     * @param torpidityIncrease Amount of torpidity dealt
     * @param lastDamager       Player is saved in case the entity becomes unconscious and is successfully tamed
     */
    public void increaseTorpidityBy(int torpidityIncrease, UUID lastDamager) {
        if (!tameableEntityAttributes.isAlpha())
            tamer = lastDamager;
        mseEntity.increaseTorpidityBy(torpidityIncrease);
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
     */
    public void setSuccessfullyTamed() {

        if (tameableEntityAttributes.isTameable() && tamer != null && !tameableEntityAttributes.isAlpha()) {
            tameableEntityAttributes.setTamed(true);

            NameChanger.markEntityForNameChange(tamer, mseEntity);
            SignGUI.sendSignToPlayer(Bukkit.getPlayer(tamer));

            Tribe tribe = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(tamer).getTribe();
            if (tribe != null) {
                tameableEntityAttributes.setTribe(tribe.getUniqueID());
                BarHandler.sendEntityTamedMessageTo(tribe, Bukkit.getPlayer(tamer), mseEntity.getName());
            } else {
                BarHandler.sendEntityTamedMessageTo(Bukkit.getPlayer(tamer), mseEntity.getName());
                tameableEntityAttributes.setOwner(tamer);
            }

            tameableEntityAttributes.setTorpidity(0);
            updateConsciousness();
            mseEntity.getWorld().getWorld().playSound(mseEntity.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5F, 0.5F);
            mseEntity.setCustomName(tameableEntityAttributes.getDefaultName());
            (new InventoryGUI()).closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
            mseEntity.setPassiveGoals();
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
        }

    }

    public UnconsciousnessTimerTameable getUnconsciousnessTimerTameable() {
        return unconsciousnessTimerTameable;
    }

    public void setUnconsciousnessTimerTameable(UnconsciousnessTimerTameable unconsciousnessTimerTameable) {
        this.unconsciousnessTimerTameable = unconsciousnessTimerTameable;
    }

    /**
     * Updates the consciousness of the entity.
     */
    public void updateConsciousness() {

        if (tameableEntityAttributes.isUnconscious() && tameableEntityAttributes.getTorpidity() <= 0) {
            // Wake up
            if (mseEntity.getCraftEntity() instanceof LivingEntity) {
                boolean removeNoneAlphas = !tameableEntityAttributes.isAlpha();
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(removeNoneAlphas);
            }
            if (!tameableEntityAttributes.isTamed() && tamer != null) {
                (new InventoryGUI()).closeTamingInventoriesOf(mseEntity, Bukkit.getPlayer(tamer));
                tamer = null;
            }

            tamingProgress = 0;
            tameableEntityAttributes.setUnconscious(false);
            if (unconsciousnessTimerTameable != null && unconsciousnessTimerTameable.isThreadCurrentlyRunning())
                unconsciousnessTimerTameable.cancel();

        } else if ((!tameableEntityAttributes.isUnconscious() && tameableEntityAttributes.getFortitude() != null && tameableEntityAttributes.getTorpidity() >= tameableEntityAttributes.getFortitude())
                || resumeConsciousness) {
            // Fall asleep
            if (mseEntity.getCraftEntity() instanceof LivingEntity)
                ((LivingEntity) mseEntity.getCraftEntity()).setRemoveWhenFarAway(false);
            if (!mseEntity.getCraftEntity().isEmpty())
                mseEntity.getCraftEntity().eject();
            resumeConsciousness = false;
            tameableEntityAttributes.setUnconscious(true);
            unconsciousnessTimerTameable = new UnconsciousnessTimerTameable(this, mseEntity);
            unconsciousnessTimerTameable.runTaskTimer(MSEMain.getInstance(), 0, unconsciousnessTimerTameable.getUnconsciousnessUpdateInterval());
        }

    }

}
