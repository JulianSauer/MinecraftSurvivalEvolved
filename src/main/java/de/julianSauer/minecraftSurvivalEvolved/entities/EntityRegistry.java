package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.*;
import de.julianSauer.minecraftSurvivalEvolved.utils.ReflectionHelper;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

public class EntityRegistry {

    public enum TameableEntityType {

        SPIDER("Spider", 52, EntityType.SPIDER, EntitySpider.class, MCESpider.class),
        GIANT("Giant", 53, EntityType.GIANT, EntityGiantZombie.class, MCEGiant.class),
        CAVE_SPIDER("CaveSpider", 59, EntityType.CAVE_SPIDER, EntityCaveSpider.class, MCECaveSpider.class),
        SQUID("Squid", 94, EntityType.SQUID, EntitySquid.class, MCESquid.class),
        WOLF("Wolf", 95, EntityType.WOLF, EntityWolf.class, MCEWolf.class);

        private String name;
        private int id;
        private EntityType entityType;
        private Class<? extends EntityInsentient> nmsClass;
        private Class<? extends EntityInsentient> customClass;

        TameableEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
            this.name = name;
            this.id = id;
            this.entityType = entityType;
            this.nmsClass = nmsClass;
            this.customClass = customClass;
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        public EntityType getEntityType() {
            return this.entityType;
        }

        public Class<? extends EntityInsentient> getNMSClass() {
            return this.nmsClass;
        }

        public Class<? extends EntityInsentient> getCustomClass() {
            return this.customClass;
        }

    }

    /**
     * Registers all entities found in TameableEntityType
     */
    public static void registerCustomEntities() {

        for (TameableEntityType entity : TameableEntityType.values())
            writeEntityToMaps(entity.getCustomClass(), entity.getName(), entity.getId());

        for (BiomeBase biomeBase : BiomeBase.i) {

            if (biomeBase == null)
                break;

            for (String field : new String[]{"u", "v", "w", "x"}) {
                List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) ReflectionHelper.getPrivateVariableValue(BiomeBase.class, biomeBase, field);

                for (BiomeBase.BiomeMeta meta : mobList) {
                    for (TameableEntityType entity : TameableEntityType.values()) {
                        if (entity.getNMSClass().equals(meta.b))
                            meta.b = entity.getCustomClass();
                    }
                }
            }
        }

    }

    /**
     * Writes to the maps in EntityTypes to register an entity.
     */
    private static void writeEntityToMaps(Class paramClass, String paramString, int paramInt) {
        ((Map) ReflectionHelper.getPrivateStaticVariable(EntityTypes.class, "c")).put(paramString, paramClass);
        ((Map) ReflectionHelper.getPrivateStaticVariable(EntityTypes.class, "d")).put(paramClass, paramString);
        ((Map) ReflectionHelper.getPrivateStaticVariable(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
        ((Map) ReflectionHelper.getPrivateStaticVariable(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
        ((Map) ReflectionHelper.getPrivateStaticVariable(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
    }

}
