package de.julianSauer.minecraftSurvivalEvolved.visuals;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class HologramHandler {

    private static Map<UUID, List<ArmorStand>> activeHolograms;

    // Positioning of text
    private static float height = 0.65F;
    private static final float spacing = 0.25F;

    /**
     * Spawns a hologram at the given location.
     *
     * @param location The hologram will float slightly above
     * @param text     Lines of text
     * @return UUID to update the hologram later
     */
    public static UUID spawnHologramAt(Location location, String... text) {

        if (activeHolograms == null)
            activeHolograms = new HashMap<>();

        location.add(0, height, 0);
        int textLength = text.length;
        List<ArmorStand> armorStands = new ArrayList<>(textLength);
        for (int i = textLength - 1; i >= 0; i--) {
            armorStands.add(spawnArmorStandAt(location, text[i]));
            location.add(0, spacing, 0);
        }

        UUID hologram = armorStands.get(0).getUniqueId();
        activeHolograms.put(hologram, armorStands);
        return hologram;

    }

    /**
     * Updates the text of a hologram.
     *
     * @param hologram Used to access the hologram
     * @param text     New lines of text
     * @return True if the hologram could've been updated
     */
    public static boolean updateHologram(UUID hologram, String... text) {

        if (activeHolograms == null || !activeHolograms.containsKey(hologram))
            return false;
        List<ArmorStand> armorStands = activeHolograms.get(hologram);
        ArrayUtils.reverse(text);

        int armorStandsLength = armorStands.size();
        int textLength = text.length;
        // Checks if there is one armor stand per text line
        int difference = armorStandsLength - textLength;
        if (difference > 0) {

            // Too many armor stands
            int i = 0;
            for (; i < textLength; i++)
                armorStands.get(i).setCustomName(text[i]);
            for (; i < armorStandsLength; i++) {
                armorStands.get(i).remove();
                armorStands.remove(i);
            }

        } else if (difference < 0) {

            // Not enough armor stands
            int i = 0;
            for (; i < armorStandsLength; i++)
                armorStands.get(i).setCustomName(text[i]);
            Location location = armorStands.get(i - 1).getLocation();
            for (; i < textLength; i++) {
                try {
                    // Calling spawn methods thread safe
                    armorStands.add((ArmorStand)
                            Bukkit.getScheduler().callSyncMethod(MSEMain.getInstance(),
                                    new CallableSpawner(location.add(0, spacing, 0), text[i]))
                                    .get()
                    );
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

        } else {
            for (int i = 0; i < textLength; i++)
                armorStands.get(i).setCustomName(text[i]);
        }

        return true;

    }

    /**
     * Removes a hologram from the world and deletes it's content.
     *
     * @param hologram Used to access the hologram
     */
    public static void despawnHologram(UUID hologram) {
        if (activeHolograms == null || !activeHolograms.containsKey(hologram))
            return;

        List<ArmorStand> armorStands = activeHolograms.get(hologram);
        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
            activeHolograms.remove(hologram);
        }
    }

    /**
     * Removes all visuals from all worlds.
     */
    public static void despawnAllHolograms() {
        if (activeHolograms == null)
            return;

        activeHolograms.values().stream()
                .forEach(armorStands -> armorStands.stream()
                        .forEach(armorStand -> armorStand.remove()));
        activeHolograms.clear();
    }

    /**
     * Spawns a single invisible armor stand with a custom name.
     *
     * @param location Position of the armor stand
     * @param text     Name of the armor stand
     * @return Reference to the spawned armor stand
     */
    private static ArmorStand spawnArmorStandAt(Location location, String text) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setSmall(true);
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        return armorStand;
    }

    /**
     * Used for async {@link #spawnArmorStandAt(Location, String)} calls.
     */
    private static class CallableSpawner implements Callable {

        final Location location;
        final String text;

        CallableSpawner(Location location, String text) {
            this.location = location;
            this.text = text;
        }

        @Override
        public ArmorStand call() throws Exception {
            return spawnArmorStandAt(location, text);
        }
    }

}
