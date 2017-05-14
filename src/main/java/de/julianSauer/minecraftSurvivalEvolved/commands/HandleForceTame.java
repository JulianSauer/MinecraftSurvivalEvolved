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

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
            return;
        }

        if (sender.hasPermission("MinecraftSurvivalEvolved.ForceTame")) {

            Player player = (Player) sender;

            switch (args.length) {
                case 1:
                    processCommand(player, args);
                    break;

                case 2:
                    if (args[1].equalsIgnoreCase("help"))
                        processCommandHelp(player);
                    else
                        processCommand(player, args);
                    break;

                default:
                    sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.setParams());
                    processCommandHelp(sender);
                    break;
            }

        } else
            sender.sendMessage(ChatMessages.ERROR_NO_PERMISSION.setParams());

    }

    /**
     * Command: /mse forcetame <player>
     *
     * @param player
     * @param args
     */
    private void processCommand(Player player, String[] args) {
        MSEEntity mseEntity = getEntityInLineOfSightFor(player);
        if (mseEntity != null) {

            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(args[1]));
                    return;
                } else
                    player = target;

            }
            if (mseEntity.getEntityAttributes().isTameable())
                mseEntity.forceTame(player);
            else
                player.sendMessage(ChatMessages.ERROR_NOT_TAMEABLE.setParams());
        } else
            player.sendMessage(ChatMessages.ERROR_NO_ENTITY_FOUND.setParams());
    }

    /**
     * Command: /mse forcetame help
     *
     * @param sender
     */
    private void processCommandHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_FORCETAME1.setParams());
        sender.sendMessage(ChatMessages.HELP_FORCETAME2.setParams());
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
