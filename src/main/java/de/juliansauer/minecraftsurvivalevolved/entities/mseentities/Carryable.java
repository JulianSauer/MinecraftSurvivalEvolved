package de.juliansauer.minecraftsurvivalevolved.entities.mseentities;

import de.juliansauer.minecraftsurvivalevolved.gui.inventories.InventoryGUI;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutMount;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Carryable extends Interactable {

    @Override
    default boolean interact(Entity target, MSEEntity mseTarget, Player interactingPlayer, InventoryGUI gui) {
        if (!Interactable.super.interact(target, mseTarget, interactingPlayer, gui)
                && interactingPlayer.isEmpty()
                && mseTarget.isTamed()
                && mseTarget.isOwner(interactingPlayer.getUniqueId())) {
            interactingPlayer.setPassenger(target);

            // TODO: Due to a bug in Minecraft the carrying player never receives the correct packet. Might be fixed in newer Minecraft versions
            if (interactingPlayer instanceof CraftPlayer) {
                EntityPlayer player = ((CraftPlayer) interactingPlayer).getHandle();
                PacketPlayOutMount packet = new PacketPlayOutMount(player);
                player.playerConnection.sendPacket(packet);
            }
            return true;
        }
        return false;
    }

}
