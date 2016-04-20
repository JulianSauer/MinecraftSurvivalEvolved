package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.v1_9_R1.BiomeBase;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntitySpider;
import net.minecraft.server.v1_9_R1.EntityTypes;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CustomEntities {

    public enum TameableEntityType {

        SPIDER("Spider", 52, EntityType.SPIDER, EntitySpider.class, TameableSpider.class);

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

        public static void registerCustomEntities() {

            for (TameableEntityType entity : values()) {
                try {
                    Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[]{Class.class, String.class, int.class});
                    a.setAccessible(true);
                    a.invoke(null, entity.getCustomClass(), entity.getName(), entity.getId());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            for (BiomeBase biomeBase : BiomeBase.i) {

                if (biomeBase == null)
                    break;

                for (String field : new String[]{"u", "v", "w", "x"}) {
                    try {

                        Field list = BiomeBase.class.getDeclaredField(field);
                        list.setAccessible(true);
                        List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) list.get(biomeBase);

                        for (BiomeBase.BiomeMeta meta : mobList) {
                            for (TameableEntityType entity : values()) {
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

    }

}
