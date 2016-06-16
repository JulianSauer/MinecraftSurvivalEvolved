package de.julianSauer.minecraftSurvivalEvolved.listeners.packets;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_9_R1.Packet;

public interface PacketListener<T extends Packet> {

    void onPacketEvent(ChannelHandlerContext context, T packet);

}
