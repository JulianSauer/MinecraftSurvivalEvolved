package de.juliansauer.minecraftsurvivalevolved.listeners.packets;

import de.juliansauer.minecraftsurvivalevolved.listeners.BasicListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_9_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

interface PacketListener<T extends Packet> extends BasicListener {

    void onPacketEvent(ChannelHandlerContext context, T packet);

    default UUID getUUIDFrom(ChannelHandlerContext context) {
        return UUID.fromString(context.name());
    }

    default Player getPlayerFrom(ChannelHandlerContext context) {
        return Bukkit.getPlayer(UUID.fromString(context.name()));
    }

}
