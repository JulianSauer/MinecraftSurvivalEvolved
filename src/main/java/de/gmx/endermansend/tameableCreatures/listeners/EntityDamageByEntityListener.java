package de.gmx.endermansend.tameableCreatures.listeners;

import de.gmx.endermansend.tameableCreatures.entities.Tameable;
import org.bukkit.entity.Arrow;
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

        if (!(e.getEntity() instanceof Tameable) || !(e.getDamager() instanceof Player) || !(e.getDamager() instanceof Arrow))
            return;

        Tameable tameableEntity = (Tameable) e.getEntity();
        Player player = (Player) e.getDamager();
        Arrow arrow = (Arrow) e.getDamager();

        if (!isTranquilizerArrow(arrow))
            return;

        double torpidity = e.getDamage();
        e.setDamage(EntityDamageEvent.DamageModifier.BASE, torpidity / 10);

        if (!tameableEntity.isTameable() || tameableEntity.isTamed())
            return;

        tameableEntity.increaseTorpidityBy((int) (torpidity * 5));

    }

    private boolean isTranquilizerArrow(Arrow arrow) {

        List<MetadataValue> titleData = arrow.getMetadata("Tranquilizer Arrow");

        for (MetadataValue metaValue : titleData)
            return metaValue.asBoolean();
        return false;

    }

}
