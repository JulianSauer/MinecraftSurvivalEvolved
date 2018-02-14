package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.logging.Logger;

public class EntitySpawnListener implements BasicEventListener {

    private Logger logger;

    public EntitySpawnListener() {
        logger = MSEMain.getInstance().getLogger();
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (!(e.getEntity() instanceof CraftLivingEntity))
            return;

        CraftLivingEntity entity = (CraftLivingEntity) e.getEntity();

        if (entity instanceof Witch
                || entity instanceof Villager
                || entity instanceof Horse
                || entity instanceof EnderDragon
                || entity instanceof Bat
                || entity instanceof Guardian
                || entity instanceof Wither
                || entity instanceof Ocelot)
            return;

        if (!(entity.getHandle() instanceof MSEEntity)) {
            logger.warning("A " + entity.getName() + " spawned in " + entity.getWorld().getBiome(entity.getLocation().getBlockX(), entity.getLocation().getBlockZ()).name() + " and was not a MSEEntity");
            entity.setHealth(0);
            return;
        }
        MSEEntity mseEntity = (MSEEntity) entity.getHandle();
        mseEntity.initWithDefaults();
    }

}
