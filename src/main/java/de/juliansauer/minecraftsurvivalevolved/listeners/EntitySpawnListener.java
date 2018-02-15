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

        if (entity instanceof Blaze
                || entity instanceof CaveSpider
                || entity instanceof Chicken
                || entity instanceof Cow
                || entity instanceof Creeper
                || entity instanceof Enderman
                || entity instanceof Endermite
                || entity instanceof Ghast
                || entity instanceof Giant
                || entity instanceof Guardian
                || entity instanceof MagmaCube
                || entity instanceof MushroomCow
                || entity instanceof Pig
                || entity instanceof PigZombie
                || entity instanceof Rabbit
                || entity instanceof Sheep
                || entity instanceof Silverfish
                || entity instanceof Skeleton
                || entity instanceof Slime
                || entity instanceof Spider
                || entity instanceof Squid
                || entity instanceof Wolf
                || entity instanceof Zombie) {

            if (entity.getHandle() instanceof MSEEntity) {
                MSEEntity mseEntity = (MSEEntity) entity.getHandle();
                mseEntity.initWithDefaults();
            } else {
                int x = entity.getLocation().getBlockX();
                int z = entity.getLocation().getBlockZ();
                logger.warning("A " + entity.getName() + " spawned in " + entity.getWorld().getBiome(x, z).name() + " and was not a MSEEntity");
                entity.remove();
            }
        }

    }

}
