package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
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
        if (!(entity instanceof CraftEntity) || !(e.getDamager() instanceof Arrow))
            return;
        Arrow arrow = (Arrow) e.getDamager();
        if (!(((CraftEntity) entity).getHandle() instanceof Tameable) || !(arrow.getShooter() instanceof Player))
            return;

        Tameable tameableEntity = (Tameable) ((CraftEntity) entity).getHandle();
        Player player = (Player) arrow.getShooter();


        if (!isTranquilizerArrow(arrow))
            return;

        double torpidity = e.getDamage();
        e.setDamage(EntityDamageEvent.DamageModifier.BASE, torpidity / 10);

        if (!tameableEntity.isTameable() || tameableEntity.isTamed())
            return;

        tameableEntity.increaseTorpidityBy((int) torpidity, player.getUniqueId());

    }

    private boolean isTranquilizerArrow(Arrow arrow) {

        List<MetadataValue> titleData = arrow.getMetadata("Tranquilizer Arrow");

        for (MetadataValue metaValue : titleData)
            return metaValue.asBoolean();
        return false;

    }

}
