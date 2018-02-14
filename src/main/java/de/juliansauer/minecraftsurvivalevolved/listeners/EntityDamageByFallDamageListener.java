package de.juliansauer.minecraftsurvivalevolved.listeners;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByFallDamageListener implements BasicEventListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;
        Entity chicken = e.getEntity().getPassenger();
        if (chicken == null || !(chicken instanceof Chicken))
            return;
        e.setCancelled(true);
        e.getEntity().setFallDistance(0);
    }

}
