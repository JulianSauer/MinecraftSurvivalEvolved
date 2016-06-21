package de.julianSauer.minecraftSurvivalEvolved.main;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.List;
import java.util.stream.Collectors;

public class MSECommandExecutor implements CommandExecutor {

    public enum ChatMessages {

        NO_PERMISSION(ChatColor.RED + "Sorry but you don't have permission to do that."),
        NO_ENTITY_FOUND(ChatColor.RED + "No entity was found at your viewing direction."),
        PRINT_HELP_FORCETAME("Usage: /mse forcetame [player]");

        ChatMessages(String message) {
            this.message = message;
        }

        private final String message;

        @Override
        public String toString() {
            return message;
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("mse"))
            return false;

        if (args.length == 1 || args.length == 2) {
            if ((args[0].equalsIgnoreCase("forcetame") || args[0].equalsIgnoreCase("ft")) && sender instanceof Player) {
                if (sender.hasPermission("MinecraftSurvivalEvolved.ForceTame")) {

                    Player player = (Player) sender;
                    MSEEntity mseEntity = getEntityInLineOfSightFor(player);
                    if (mseEntity != null) {

                        // Force tame
                        if (args.length == 2) {
                            player = Bukkit.getPlayer(args[1]);
                            if (player == null) {
                                sender.sendMessage(ChatMessages.PRINT_HELP_FORCETAME.toString());
                                return true;
                            }
                        }
                        mseEntity.forceTame(player);

                    } else
                        sender.sendMessage(ChatMessages.NO_ENTITY_FOUND.toString());
                } else
                    sender.sendMessage(ChatMessages.NO_PERMISSION.toString());
                return true;
            }
        }
        return false;
    }

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
