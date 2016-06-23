package de.julianSauer.minecraftSurvivalEvolved.listeners.packets;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.PacketPlayInBlockDig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Registers players that are riding an entity and mining for custom duration and drops of blocks.
 */
public class InBlockDigListener implements PacketListener<PacketPlayInBlockDig> {

    private Map<UUID, MiningTimer> currentMiningTasks = new HashMap<>();

    @Override
    public void onPacketEvent(ChannelHandlerContext context, PacketPlayInBlockDig packet) {

        PacketPlayInBlockDig.EnumPlayerDigType digType = packet.c();
        if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {

            Player player = getPlayerFrom(context);
            MSEEntity mseEntity = getMSEEntityFromVehicle(player);
            if (mseEntity == null)
                return;

            // Get the block
            BlockPosition blockPosition = packet.a();
            Block block = player.getWorld().getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
            long breakSpeed = mseEntity.getMiningHandler().calculateMiningTimeFor(block.getType());
            if (breakSpeed <= 0)
                return;
            MiningTimer miningTimer = new MiningTimer(player.getUniqueId(), block, mseEntity);
            currentMiningTasks.put(player.getUniqueId(), miningTimer);
            miningTimer.runTaskTimer(MSEMain.getInstance(), breakSpeed, 20L);

        } else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {

            UUID player = getUUIDFrom(context);
            if (currentMiningTasks.containsKey(player)) {
                currentMiningTasks.get(player).cancel();
                currentMiningTasks.remove(player);
            }

        }

    }

    /**
     * Hands out the block to an entity's MiningHandler after a certain amount of time.
     */
    public class MiningTimer extends BukkitRunnable {

        private final Block block;
        private final UUID player;
        private final MSEEntity mseEntity;

        MiningTimer(UUID player, Block block, MSEEntity mseEntity) {
            this.player = player;
            this.block = block;
            this.mseEntity = mseEntity;
        }

        @Override
        public void run() {
            currentMiningTasks.remove(player);
            mseEntity.getMiningHandler().mineBlocks(block);
            this.cancel();
        }

    }

}