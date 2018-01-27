package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.customPlayer.MSEPlayerMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 * Handles attacks with tranquilizer arrows. Receives metadata from BowShootListener.
 */
public class EntityDamageByEntityListener implements ArrowListener {

    private MSEPlayerMap msePlayerMap = MSEPlayerMap.getPlayerRegistry();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        Entity target = e.getEntity();

        Entity damager = e.getDamager();

        if (isTranqEvent(target, damager)) {

            TippedArrow arrow = (TippedArrow) e.getDamager();
            arrow.setBasePotionData((new PotionData(PotionType.AWKWARD)));
            Player player = (Player) arrow.getShooter();

            // Reduce damage
            double torpidity = e.getDamage();
            e.setDamage(EntityDamageEvent.DamageModifier.BASE, torpidity / 10);

            // Increase torpor
            MSEEntity mseEntity = getMSEEntityFromEntity(target);
            if (mseEntity != null && mseEntity.isTameable())
                mseEntity.getTamingHandler().increaseTorpidityBy((int) torpidity, player.getUniqueId());
            else if (msePlayerMap.isMSEPlayer(target))
                msePlayerMap.getMSEPlayer(target.getUniqueId()).increaseTorpidityBy((int) torpidity);

        } else if (isMountedAttack(damager)) {
            MSEEntity mseEntity = getMSEEntityFromVehicle(damager);
            e.setDamage(mseEntity.getDamage());
            mseEntity.playAttackSound();

        } else if (isNPCAttackEvent(damager)) {
            e.setDamage(getMSEEntityFromEntity(damager).getDamage());

        }

    }

    /**
     * Checks if a player is attacking another entity while riding a tamed creature.
     *
     * @param damager Attacking player
     * @return True if player is riding a tamed entity
     */
    private boolean isMountedAttack(Entity damager) {

        if (!(damager instanceof Player))
            return false;
        Player player = (Player) damager;
        return !(getMSEEntityFromVehicle(player) == null);

    }

    /**
     * Checks if an NPC is attacking.
     *
     * @param damager Attacking NPC
     * @return True if the damager is not a player
     */
    private boolean isNPCAttackEvent(Entity damager) {
        return (damager instanceof LivingEntity && !(damager instanceof Player) && getMSEEntityFromEntity(damager) != null);
    }

}
