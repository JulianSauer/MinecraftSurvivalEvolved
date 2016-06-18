package de.julianSauer.minecraftSurvivalEvolved.listeners.packets;

import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.NameChangeHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_9_R1.PacketPlayInUpdateSign;

import java.util.UUID;

/**
 * Listens for incoming sign updates to change the name of an MSEEntity.
 */
public class InUpdateSignListener implements PacketListener<PacketPlayInUpdateSign> {

    @Override
    public void onPacketEvent(ChannelHandlerContext context, PacketPlayInUpdateSign packet) {
        String newName = "";
        for (String s : packet.b())
            newName += s;
        UUID player = getUUIDFrom(context);
        NameChangeHandler.changeNameOfEntity(player, newName);
    }

}
