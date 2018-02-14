package de.juliansauer.minecraftsurvivalevolved.listeners;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.Carryable;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutMount;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EntityDismountListener implements BasicEventListener {

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        if (!(e.getDismounted() instanceof CraftPlayer)
                || !(e.getEntity() instanceof CraftEntity)
                || !(((CraftEntity) e.getEntity()).getHandle() instanceof Carryable))
            return;

        // TODO: Due to a bug in Minecraft the carrying player never receives the correct packet. Might be fixed in newer Minecraft versions
        (new BukkitRunnable() {
            @Override
            public void run() {
                EntityPlayer player = ((CraftPlayer) e.getDismounted()).getHandle();
                PacketPlayOutMount packet = new PacketPlayOutMount(player);
                player.playerConnection.sendPacket(packet);
            }
        }).runTaskLater(MSEMain.getInstance(), 1L);

    }

}
