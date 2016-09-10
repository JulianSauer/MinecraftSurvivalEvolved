package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMember;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles all sub commands of /mse tribe.
 */
public class HandleTribeCommand extends CommandHandler {

    @Override
    public void process(CommandSender sender, String... args) {

        // mse tribe <tribe>
        if (args.length == 2 && args[1] != "") {
            // Print tribe members
            if (tribeRegistry.tribeExists(args[1])) {
                sender.sendMessage(ChatMessages.TRIBE_PRINT_MEMBERS.setParams(args[1]));
                for (OfflinePlayer offlinePlayer : tribeRegistry.getTribe(args[1]).getMembers())
                    sender.sendMessage("- " + offlinePlayer.getName());
            } else
                sender.sendMessage(ChatMessages.TRIBE_DOESNT_EXIST.setParams(args[1]));

            // mse tribe create <tribe>
        } else if (args.length == 3 && args[1].equalsIgnoreCase("create") && args[2] != "" && sender instanceof Player) {
            Player player = (Player) sender;
            TribeMemberRegistry tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
            TribeMember tribeMember = tribeMemberRegistry.getTribeMember(player);

            if (tribeMember.hasTribe())
                player.sendMessage(ChatMessages.TRIBE_ALREADY_JOINED_A_TRIBE.setParams(tribeMember.getTribe().getName()));
            else if (tribeRegistry.tribeExists(args[2]))
                player.sendMessage(ChatMessages.TRIBE_EXISTS_ALREADY.setParams(args[2]));
            else {
                new Tribe(player, args[2]);
                tribeMemberRegistry.registerPlayer(player);
                player.sendMessage(ChatMessages.TRIBE_CREATED.setParams(args[2]));
            }
        } else
            sender.sendMessage(ChatMessages.WRONG_NUMBER_OF_ARGS.toString()); // TODO: Print help

    }

}
