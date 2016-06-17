package de.julianSauer.minecraftSurvivalEvolved.listeners.packets;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_9_R1.PacketPlayInUpdateSign;

public class InUpdateSignListener implements PacketListener<PacketPlayInUpdateSign> {

    @Override
    public void onPacketEvent(ChannelHandlerContext context, PacketPlayInUpdateSign packet) {
        String message = "";
        for (String s : packet.b())
            message += s;
        System.out.println(message);
    }

}
