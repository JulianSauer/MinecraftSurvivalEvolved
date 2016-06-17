package de.julianSauer.minecraftSurvivalEvolved.listeners.packets;

import de.julianSauer.minecraftSurvivalEvolved.listeners.BasicListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_9_R1.Packet;

public interface PacketListener<T extends Packet> extends BasicListener{

    void onPacketEvent(ChannelHandlerContext context, T packet);

}
