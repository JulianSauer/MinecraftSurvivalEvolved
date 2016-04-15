package de.gmx.endermansend.tameableCreatures.entities;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntitySpider;
import net.minecraft.server.EntityTypes;
import org.bukkit.block.Biome;
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

        private TameableEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
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

            for (Biome biome : Biome.values()) {

                if (biome == null)
                    break;

                for (String field : new String[]{"K", "J", "L", "M"}) {
                    try {

                        Field list = Biome.class.getDeclaredField(field);
                        list.setAccessible(true);
                        List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) list.get(biome);

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
