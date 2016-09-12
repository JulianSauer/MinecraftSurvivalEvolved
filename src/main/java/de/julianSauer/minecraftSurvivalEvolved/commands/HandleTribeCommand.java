package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMember;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles all sub commands of /mse tribe.
 */
public class HandleTribeCommand extends CommandHandler {

    @Override
    public void process(CommandSender sender, String... args) {

        if (args.length == 2 && args[1].equalsIgnoreCase("leave")) {
            // Command: /mse tribe leave
            // TODO

        } else if (args.length == 2 && !args[1].equals("")) {

            // Command: /mse tribe help
            if (args[1].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP_TRIBE1.toString());
                sender.sendMessage(ChatMessages.HELP_TRIBE2.toString());
                return;
            }

            // Command: /mse tribe <tribe>
            if (tribeRegistry.tribeExists(args[1])) {
                sender.sendMessage(ChatMessages.TRIBE_PRINT_MEMBERS.setParams(args[1]));
                for (OfflinePlayer offlinePlayer : tribeRegistry.getTribe(args[1]).getMembers())
                    sender.sendMessage("- " + offlinePlayer.getName());
            } else
                sender.sendMessage(ChatMessages.ERROR_TRIBE_DOESNT_EXIST.setParams(args[1]));

        } else if (args.length == 3 && args[1].equalsIgnoreCase("create") && !args[2].equals("") && sender instanceof Player) {

            // Command: /mse tribe create help
            if (args[2].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE1.toString());
                sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE2.toString());
                return;
            }

            // Command: /mse tribe create <tribe>
            Player player = (Player) sender;
            TribeMemberRegistry tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
            TribeMember tribeMember = tribeMemberRegistry.getTribeMember(player);

            if (tribeMember.hasTribe())
                player.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE.setParams(tribeMember.getTribe().getName()));
            else if (tribeRegistry.tribeExists(args[2]))
                player.sendMessage(ChatMessages.ERROR_TRIBE_EXISTS_ALREADY.setParams(args[2]));
            else {
                new Tribe(player, args[2]);
                tribeMemberRegistry.registerPlayer(player);
                player.sendMessage(ChatMessages.TRIBE_CREATED.setParams(args[2]));
            }

        } else if (args.length == 3 && args[1].equalsIgnoreCase("leave") && args[2].equalsIgnoreCase("help")) {
            // Command: /mse tribe leave help
            sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE1.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE2.toString());

        } else {
            sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE1.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE2.toString());
        }

    }

}
