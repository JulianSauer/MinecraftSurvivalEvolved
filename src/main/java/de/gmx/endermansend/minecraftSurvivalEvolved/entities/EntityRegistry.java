package de.gmx.endermansend.minecraftSurvivalEvolved.entities;

import de.gmx.endermansend.minecraftSurvivalEvolved.entities.customEntities.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class EntityRegistry {

    public enum TameableEntityType {

        SPIDER("Spider", 52, EntityType.SPIDER, EntitySpider.class, TameableSpider.class),
        GIANT("Giant", 53, EntityType.GIANT, EntityGiantZombie.class, TameableGiant.class),
        CAVE_SPIDER("CaveSpider", 59, EntityType.CAVE_SPIDER, EntityCaveSpider.class, TameableCaveSpider.class),
        SQUID("Squid", 94, EntityType.SQUID, EntitySquid.class, TameableSquid.class),
        WOLF("Wolf", 95, EntityType.WOLF, EntityWolf.class, TameableWolf.class);

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
            a(entity.getCustomClass(), entity.getName(), entity.getId());

        for (BiomeBase biomeBase : BiomeBase.i) {

            if (biomeBase == null)
                break;

            for (String field : new String[]{"u", "v", "w", "x"}) {
                try {

                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) list.get(biomeBase);

                    for (BiomeBase.BiomeMeta meta : mobList) {
                        for (TameableEntityType entity : TameableEntityType.values()) {
                            if (entity.getNMSClass().equals(meta.b))
                                meta.b = entity.getCustomClass();
                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Source: https://github.com/Europia79/Extraction/blob/master/modules/v1_8_R3/src/mc/euro/extraction/nms/v1_8_R3/CustomEntityType.java
     * A convenience method.
     *
     * @param clazz The class
     * @param f     The string representation of the private static field
     * @return The object found
     * @throws NoSuchFieldException   If unable to get the object
     * @throws IllegalAccessException If unable to get the object
     */
    private static Object getPrivateStatic(Class clazz, String f) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    /**
     * Source: https://github.com/Europia79/Extraction/blob/master/modules/v1_8_R3/src/mc/euro/extraction/nms/v1_8_R3/CustomEntityType.java
     * Since 1.7.2 added a check in their entity registration, simply bypass it and write to the maps ourself.
     */
    private static void a(Class paramClass, String paramString, int paramInt) {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
            ((Map) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
