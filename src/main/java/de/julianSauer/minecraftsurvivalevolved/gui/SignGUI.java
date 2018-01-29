package de.juliansauer.minecraftsurvivalevolved.gui;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenSignEditor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SignGUI {

    public static void sendSignToPlayer(Player player) {
        Location location = player.getLocation();
        double x, y, z;
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(
                BlockPosition.PooledBlockPosition.c(x, y, z));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}