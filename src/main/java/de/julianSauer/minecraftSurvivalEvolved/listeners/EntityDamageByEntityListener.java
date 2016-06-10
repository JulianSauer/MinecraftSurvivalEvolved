package de.julianSauer.minecraftSurvivalEvolved.listeners;

import de.julianSauer.minecraftSurvivalEvolved.entities.Tameable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityDamageByEntityListener extends BasicListener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        Entity target = e.getEntity();

        Entity damager = e.getDamager();

        if (isTranqEvent(target, damager)) {

            Arrow arrow = (Arrow) e.getDamager();
            Tameable tameableEntity = getTameableEntityFromEntity(target);
            Player player = (Player) arrow.getShooter();

            double torpidity = e.getDamage();
            e.setDamage(EntityDamageEvent.DamageModifier.BASE, torpidity / 10);

            if (!tameableEntity.isTameable())
                return;

            tameableEntity.increaseTorpidityBy((int) torpidity, player.getUniqueId(), player.getName());

        } else if (isMountedAttack(damager)) {
            e.setDamage(getTameableEntityFromVehicle(damager).getDamage());

        } else if (isNPCAttackEvent(damager)) {
            e.setDamage(getTameableEntityFromEntity(damager).getDamage());

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
        if (getTameableEntityFromVehicle(player) == null)
            return false;
        return true;

    }

    /**
     * Checks if a player shoots tranq arrows at a tameable entity.
     *
     * @param target  Target of the attack
     * @param damager Shot arrow
     * @return True if a tranq arrow is used against a tameable entity
     */
    private boolean isTranqEvent(Entity target, Entity damager) {

        if (!(damager instanceof Arrow))
            return false;
        Arrow arrow = (Arrow) damager;
        if (getTameableEntityFromEntity(target) == null || !(arrow.getShooter() instanceof Player))
            return false;

        List<MetadataValue> titleData = arrow.getMetadata("Tranquilizer Arrow");

        for (MetadataValue metaValue : titleData)
            return metaValue.asBoolean();
        return false;

    }

    /**
     * Checks if an NPC is attacking.
     *
     * @param damager Attacking NPC
     * @return True if the damager is not a player
     */
    private boolean isNPCAttackEvent(Entity damager) {
        return (damager instanceof LivingEntity && !(damager instanceof Player) && getTameableEntityFromEntity(damager) != null);
    }

}
