package de.julianSauer.minecraftSurvivalEvolved.listeners.packets;

import io.netty.channel.Channel;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketInjector {

    /**
     * Adds a player to the packet listener.
     * @param player
     */
    public void addPlayer(Player player) {
        if(!(player instanceof CraftPlayer))
            return;
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Channel channel = entityPlayer.playerConnection.networkManager.channel;
        if (channel.pipeline().get("PacketInjector") == null) {
            PacketEventManager customPacketListener = new PacketEventManager();
            channel.pipeline().addBefore("packet_handler", "PacketInjector", customPacketListener);
        }
    }

    /**
     * Removes a player from the packet listener.
     * @param player
     */
    public void removePlayer(Player player) {
        if(!(player instanceof CraftPlayer))
            return;
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Channel channel = entityPlayer.playerConnection.networkManager.channel;
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }

}