package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMember;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles all sub commands of /mse tribe.
 */
public class HandleTribeCommand extends CommandHandler {

    private TribeMemberRegistry tribeMemberRegistry;
    private List<UUID> pendingTribeLeaves;

    HandleTribeCommand() {
        super();
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
        pendingTribeLeaves = new ArrayList<>();
    }

    @Override
    public void process(CommandSender sender, String... args) {

        if (args.length == 2 && args[1].equalsIgnoreCase("leave")) {
            // Command: /mse tribe leave
            leave((Player) sender);

        } else if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
            // Command: /mse tribe confirm
            confirmLeave((Player) sender);

        } else if (args.length == 2 && !args[1].equals("")) {

            // Command: /mse tribe help
            if (args[1].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP_TRIBE1.toString());
                sender.sendMessage(ChatMessages.HELP_TRIBE2.toString());
                return;
            }

            // Command: /mse tribe <tribe>
            printTribeInfo(sender, args[1]);

        } else if (args.length == 3 && args[1].equalsIgnoreCase("create") && !args[2].equals("") && sender instanceof Player) {

            // Command: /mse tribe create help
            if (args[2].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE1.toString());
                sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE2.toString());
                return;
            }

            // Command: /mse tribe create <tribe>
            create((Player) sender, args[2]);

        } else if (args.length == 3 && args[1].equalsIgnoreCase("leave") && args[2].equalsIgnoreCase("help")) {
            // Command: /mse tribe leave help
            sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE1.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE2.toString());

        } else if (args.length == 3 && args[1].equalsIgnoreCase("confirm") && args[2].equalsIgnoreCase("help")) {
            // Command: /mse tribe confirm help
            sender.sendMessage(ChatMessages.HELP_TRIBE_CONFIRM1.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE_CONFIRM2.toString());

        } else {
            sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE1.toString());
            sender.sendMessage(ChatMessages.HELP_TRIBE2.toString());
        }

    }

    /**
     * Allows a player to leave his tribe. Needs additional confirmation to fulfill this request.
     * See {@link #confirmLeave(Player)}
     *
     * @param player
     */
    private void leave(Player player) {

        Tribe tribe = tribeMemberRegistry.getTribeOf(player);
        if (tribe == null) {
            player.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.toString());
            return;
        }

        if (tribe.getMembers().size() == 1) {
            player.sendMessage(ChatMessages.WARNING_DELETE_TRIBE.setParams(tribe.getName()));
        } else {
            player.sendMessage(ChatMessages.WARNING_LEAVE_TRIBE.setParams(tribe.getName()));
        }
        pendingTribeLeaves.add(player.getUniqueId());

        // Player has 20 seconds to confirm the action.
        new BukkitRunnable() {
            @Override
            public void run() {
                pendingTribeLeaves.remove(player.getUniqueId());
                this.cancel();
            }
        }.runTaskTimerAsynchronously(MSEMain.getInstance(), 400, 0);

    }

    /**
     * If called within 20 seconds after the initial leave request, the player is removed from the tribe. Therefore
     * {@link #leave(Player)} has to be called first.
     *
     * @param player The player who wants to leave his tribe
     */
    private void confirmLeave(Player player) {

        if (pendingTribeLeaves.contains(player.getUniqueId())) {

            Tribe tribe = tribeMemberRegistry.getTribeOf(player);
            tribe.remove(player, player);
            if (tribe.getMembers().isEmpty())
                tribe.deleteTribe(null);
            pendingTribeLeaves.remove(player.getUniqueId());
            player.sendMessage(ChatMessages.TRIBE_LEFT.setParams(tribe.getName()));

        } else {
            player.sendMessage(ChatMessages.ERROR_NOTHING_TO_CONFIRM.toString());
        }

    }

    /**
     * Shows the members of a tribe.
     */
    private void printTribeInfo(CommandSender sender, String tribeName) {
        if (tribeRegistry.tribeExists(tribeName)) {
            sender.sendMessage(ChatMessages.TRIBE_PRINT_MEMBERS.setParams(tribeName));
            for (OfflinePlayer offlinePlayer : tribeRegistry.getTribe(tribeName).getMembers())
                sender.sendMessage("- " + offlinePlayer.getName());
        } else
            sender.sendMessage(ChatMessages.ERROR_TRIBE_DOESNT_EXIST.setParams(tribeName));
    }

    /**
     * Creates a new tribe if the name is available and gives the player a leader rank within it.
     */
    private void create(Player player, String tribeName) {

        TribeMember tribeMember = tribeMemberRegistry.getTribeMember(player);

        if (tribeMember.hasTribe())
            player.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE.setParams(tribeMember.getTribe().getName()));
        else if (tribeRegistry.tribeExists(tribeName))
            player.sendMessage(ChatMessages.ERROR_TRIBE_EXISTS_ALREADY.setParams(tribeName));
        else {
            new Tribe(player, tribeName);
            tribeMemberRegistry.registerPlayer(player);
            player.sendMessage(ChatMessages.TRIBE_CREATED.setParams(tribeName));
        }

    }

}
