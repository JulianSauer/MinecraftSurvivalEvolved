package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
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
            if (!tribeRegistry.tribeExists(args[2])) {
                new Tribe(player, args[2]);
            } else
                player.sendMessage(ChatMessages.TRIBE_EXISTS_ALREADY.setParams(args[2]));
        } else
            sender.sendMessage(ChatMessages.WRONG_NUMBER_OF_ARGS.toString()); // TODO: Print help

    }

}
