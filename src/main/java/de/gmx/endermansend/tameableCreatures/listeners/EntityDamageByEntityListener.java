package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        Entity entity = e.getEntity();

        Entity damager = e.getDamager();

        if (isTranqEvent(entity, damager)) {

            Arrow arrow = (Arrow) e.getDamager();
            Tameable tameableEntity = (Tameable) ((CraftEntity) entity).getHandle();
            Player player = (Player) arrow.getShooter();

            double torpidity = e.getDamage();
            e.setDamage(EntityDamageEvent.DamageModifier.BASE, torpidity / 10);

            if (!tameableEntity.isTameable() || tameableEntity.isTamed())
                return;

            tameableEntity.increaseTorpidityBy((int) torpidity, player.getUniqueId());

        } else if (isMountedAttack(entity, damager)) {
            EntityInsentient tameableEntity = (EntityInsentient) ((CraftEntity) damager.getVehicle()).getHandle();
            tameableEntity.B(((CraftEntity) entity).getHandle());
            e.setDamage(0);
        }

    }

    /**
     * Checks if a player is attacking another entity while riding a tamed creature.
     *
     * @param entity  Target of the attack
     * @param damager Attacking player
     * @return True if player is riding a tamed entity
     */
    private boolean isMountedAttack(Entity entity, Entity damager) {

        if (!(damager instanceof Player))
            return false;
        Player player = (Player) damager;
        Entity vehicle = player.getVehicle();
        if (vehicle == null || !(vehicle instanceof CraftEntity) || !(((CraftEntity) vehicle).getHandle() instanceof Tameable))
            return false;
        return true;

    }

    /**
     * Checks if a player shoots tranq arrows at a tameable entity.
     *
     * @param entity  Target of the attack
     * @param damager Shot arrow
     * @return True if a tranq arrow is used against a tameable entity
     */
    private boolean isTranqEvent(Entity entity, Entity damager) {

        if (!(entity instanceof CraftEntity) || !(damager instanceof Arrow))
            return false;
        Arrow arrow = (Arrow) damager;
        if (!(((CraftEntity) entity).getHandle() instanceof Tameable) || !(arrow.getShooter() instanceof Player))
            return false;

        List<MetadataValue> titleData = arrow.getMetadata("Tranquilizer Arrow");

        for (MetadataValue metaValue : titleData)
            return metaValue.asBoolean();
        return false;

    }

}
