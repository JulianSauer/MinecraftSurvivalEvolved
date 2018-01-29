package de.juliansauer.minecraftsurvivalevolved.listeners.packets;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_9_R1.Packet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PacketEventManager extends ChannelDuplexHandler {

    private static Map<Class, PacketListener> packetListeners = new HashMap<>();

    @Override
    public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
        super.write(context, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
        PacketListener packetListener = packetListeners.get(packet.getClass());
        if (packetListener != null && packet instanceof Packet)
            packetListener.onPacketEvent(context, (Packet) packet);
        super.channelRead(context, packet);
    }

    /**
     * Registers a new packet listener.
     *
     * @param packetListener The packet listener
     */
    public static void registerPacketListener(PacketListener packetListener) {

        Method[] methods = packetListener.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("onPacketEvent"))
                packetListeners.put(method.getParameterTypes()[1], packetListener);
        }

    }

}