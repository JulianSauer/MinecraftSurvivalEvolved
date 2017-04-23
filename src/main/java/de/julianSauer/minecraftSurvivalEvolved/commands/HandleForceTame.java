package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.messages.ChatMessages;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles /mse forcetame.
 */
public class HandleForceTame extends CommandHandler {

    @Override
    public void process(CommandSender sender, String... args) {

        if (!(sender instanceof Player))
            sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());

        if (sender.hasPermission("MinecraftSurvivalEvolved.ForceTame")) {

            Player player = (Player) sender;

            // Error or command: /mse forcetame help
            if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP_FORCETAME1.setParams());
                sender.sendMessage(ChatMessages.HELP_FORCETAME2.setParams());
                return;
            }

            // Command: /mse forcetame
            MSEEntity mseEntity = getEntityInLineOfSightFor(player);
            if (mseEntity != null) {

                // Command: /mse forcetame <player>
                if (args.length == 2) {
                    player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage(ChatMessages.HELP_FORCETAME1.setParams());
                        sender.sendMessage(ChatMessages.HELP_FORCETAME2.setParams());
                        return;
                    }

                }
                if (!mseEntity.getEntityAttributes().isAlpha())
                    mseEntity.forceTame(player);
                else
                    sender.sendMessage(ChatMessages.ERROR_ALPHA_TAME.setParams());

            } else
                sender.sendMessage(ChatMessages.ERROR_NO_ENTITY_FOUND.setParams());
        } else
            sender.sendMessage(ChatMessages.ERROR_NO_PERMISSION.setParams());

    }

    /**
     * Gets the closest entity a player is looking at.
     *
     * @param player Player that is looking at the entity
     * @return Entity that the player is looking at
     */
    private MSEEntity getEntityInLineOfSightFor(Player player) {
        List<Entity> nearbyEntities = player.getNearbyEntities(10, 10, 10);
        List<MSEEntity> livingEntities = nearbyEntities.stream().filter(entity -> entity instanceof CraftEntity && ((CraftEntity) entity).getHandle() instanceof MSEEntity).map(entity -> (MSEEntity) ((CraftEntity) entity).getHandle()).collect(Collectors.toList());

        BlockIterator blockIterator = new BlockIterator(player, 10);
        Block block;
        int blockX, blockY, blockZ;
        double entityX, entityY, entityZ;
        while (blockIterator.hasNext()) {
            block = blockIterator.next();
            blockX = block.getX();
            blockY = block.getY();
            blockZ = block.getZ();
            for (MSEEntity entity : livingEntities) {
                entityX = entity.getLocation().getX();
                entityY = entity.getLocation().getY();
                entityZ = entity.getLocation().getZ();
                if ((blockX - 0.75 <= entityX && entityX <= blockX + 1.75)
                        && (blockZ - 0.75 <= entityZ && entityZ <= blockZ + 1.75)
                        && (blockY - 1 <= entityY && entityY <= blockY + 2.5)) {
                    return entity;
                }
            }
        }
        return null;
    }

}
