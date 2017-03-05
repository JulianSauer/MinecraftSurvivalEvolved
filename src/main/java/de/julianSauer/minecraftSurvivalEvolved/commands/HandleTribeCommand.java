package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMember;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Handles all sub commands of /mse tribe.
 */
public class HandleTribeCommand extends CommandHandler {

    private TribeMemberRegistry tribeMemberRegistry;
    private Set<UUID> pendingTribeLeaves;
    private Map<UUID, Tribe> pendingTribeInvitations;

    HandleTribeCommand() {
        super();
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
        pendingTribeLeaves = new HashSet<>();
        pendingTribeInvitations = new HashMap<>();
    }

    @Override
    public void process(CommandSender sender, String... args) {

        if (args.length == 1) {
            // Command: /mse tribe
            if (sender instanceof Player) {
                TribeMember member = tribeMemberRegistry.getTribeMember((Player) sender);
                if (member == null || !member.hasTribe()) {
                    sender.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.toString());
                    sender.sendMessage(ChatMessages.HELP_TRIBE1.toString());
                    sender.sendMessage(ChatMessages.HELP_TRIBE2.toString());
                } else
                    printTribeInfo(sender, member.getTribe().getName());
            } else
                sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.toString());

        } else if (args.length == 2 && args[1].equalsIgnoreCase("leave")) {
            // Command: /mse tribe leave
            if (sender instanceof Player)
                leave((Player) sender);
            else
                sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.toString());

        } else if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
            // Command: /mse tribe confirm
            if (sender instanceof Player)
                confirmAction((Player) sender);
            else
                sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.toString());

        } else if (args.length == 2 && !args[1].equals("")) {

            // Command: /mse tribe help
            if (args[1].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP_TRIBE1.toString());
                sender.sendMessage(ChatMessages.HELP_TRIBE2.toString());
            } else
                // Command: /mse tribe <tribe>
                printTribeInfo(sender, args[1]);

        } else if (args.length == 3 && sender instanceof Player) {

            if (sender instanceof Player) {

                if (args[1].equals("invite")) {

                    // Command: /mse tribe invite <player>
                    Player invitingPlayer = (Player) sender;
                    TribeMember invitingTribeMember = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(invitingPlayer);

                    if (invitingTribeMember.hasTribe()) {
                        String playerName = args[2];
                        Player invitedPlayer = Bukkit.getPlayer(playerName);

                        if (invitedPlayer != null) {

                            if (TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(invitedPlayer).hasTribe()) {
                                invitingPlayer.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE2.setParams(playerName));

                            } else if (invitingTribeMember.canRecruit()) {
                                invite(invitedPlayer, invitingTribeMember.getTribe());
                                invitingPlayer.sendMessage(ChatMessages.TRIBE_INVITED_PLAYER.setParams(playerName));
                                invitedPlayer.sendMessage(ChatMessages.TRIBE_INVITE_RECEIVED
                                        .setParams(invitingPlayer.getDisplayName(), invitingTribeMember.getTribe().getName()));
                            } else {
                                invitingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
                            }

                        } else
                            invitingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(playerName));

                    } else {
                        invitingPlayer.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.toString());
                    }

                } else if (args[1].equalsIgnoreCase("create") && !args[2].equals("")) {
                    if (args[2].equalsIgnoreCase("help")) {

                        // Command: /mse tribe create help
                        sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE1.toString());
                        sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE2.toString());
                    } else {
                        // Command: /mse tribe create <tribe>
                        create((Player) sender, args[2]);
                    }
                }

            } else
                sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.toString());

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
     * See {@link #confirmAction(Player)}
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
     * Allows a player to join a tribe. Needs additional confirmation to fulfill this request.
     * See {@link #confirmAction(Player)}
     *
     * @param player
     */
    private void invite(Player player, Tribe tribe) {

        UUID playerUUID = player.getUniqueId();
        pendingTribeInvitations.put(playerUUID, tribe);

        // Player has 20 seconds to confirm the action.
        new BukkitRunnable() {
            @Override
            public void run() {
                pendingTribeInvitations.remove(playerUUID);
                this.cancel();
            }
        }.runTaskTimerAsynchronously(MSEMain.getInstance(), 400, 0);
    }

    /**
     * Can be used to confirm leaving or joining a tribe if called within 20 seconds after the initial request.
     * Therefore {@link #leave(Player)} or {@link #invite(Player, Tribe)} has to be called first.
     *
     * @param player The player who wants to leave his tribe
     */
    private void confirmAction(Player player) {

        UUID playerUUID = player.getUniqueId();

        if (pendingTribeLeaves.contains(playerUUID) && pendingTribeInvitations.get(playerUUID) != null) {
            new IllegalStateException("A player has invitation and leave requests at the same time. That shouldn't have happened :(").printStackTrace();
            return;
        }

        if (pendingTribeLeaves.contains(playerUUID)) {

            Tribe tribe = tribeMemberRegistry.getTribeOf(player);
            tribe.remove(player, player);
            if (tribe.getMembers().isEmpty())
                tribe.deleteTribe(null);
            pendingTribeLeaves.remove(playerUUID);
            player.sendMessage(ChatMessages.TRIBE_LEFT.setParams(tribe.getName()));

        } else if (pendingTribeInvitations.get(playerUUID) != null) {

            Tribe tribe = pendingTribeInvitations.get(playerUUID);
            tribe.addNewMember(playerUUID);

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
            Tribe tribe = tribeRegistry.getTribe(tribeName);
            for (OfflinePlayer offlinePlayer : tribe.getMembers())
                sender.sendMessage(ChatMessages.TRIBE_PRINT_MEMBER.setParams(
                        offlinePlayer.getName(),
                        tribe.getRankOfMember(offlinePlayer.getUniqueId()).toString()));
        } else
            sender.sendMessage(ChatMessages.ERROR_TRIBE_DOESNT_EXIST.setParams(tribeName));
    }

    /**
     * Creates a new tribe if the name is available and gives the player a leader rank within it.
     */
    private void create(Player player, String tribeName) {

        TribeMember tribeMember = tribeMemberRegistry.getTribeMember(player);

        if (tribeMember.hasTribe())
            player.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE1.setParams(tribeMember.getTribe().getName()));
        else if (tribeRegistry.tribeExists(tribeName))
            player.sendMessage(ChatMessages.ERROR_TRIBE_EXISTS_ALREADY.setParams(tribeName));
        else {
            new Tribe(player, tribeName);
            tribeMemberRegistry.registerPlayer(player);
            player.sendMessage(ChatMessages.TRIBE_CREATED.setParams(tribeName));
        }

    }

}
