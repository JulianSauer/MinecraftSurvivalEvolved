package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.messages.ChatMessages;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Rank;
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

        switch (args.length) {

            case 1:

                processCommand(sender);
                break;

            case 2:

                if (args[1].equalsIgnoreCase("leave")) {
                    processCommandLeave(sender);

                } else if (args[1].equalsIgnoreCase("confirm")) {
                    processCommandConfirm(sender);

                } else if (!args[1].equals("")) {
                    processCommandInfo(sender, args);
                }
                break;

            case 3:

                if (sender instanceof Player) {
                    if (args[1].equals("invite")) {
                        processCommandInvite((Player) sender, args);

                    } else if (args[1].equalsIgnoreCase("create") && !args[2].equals("")) {
                        processCommandCreate(sender, args);

                    } else if (args[1].equalsIgnoreCase("promote") && !args[2].equals("")) {
                        processCommandPromoteOrDemote(sender, args, true);

                    } else if (args[1].equalsIgnoreCase("demote") && !args[2].equals("")) {
                        processCommandPromoteOrDemote(sender, args, false);

                    } else
                        sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());

                } else if (args[1].equalsIgnoreCase("leave") && args[2].equalsIgnoreCase("help")) {
                    processCommandLeaveHelp(sender);

                } else if (args[1].equalsIgnoreCase("confirm") && args[2].equalsIgnoreCase("help")) {
                    processCommandConfirmHelp(sender);

                } else {
                    sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.setParams());
                    sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
                    sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
                }
                break;

            default:
                sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.setParams());
                sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
                sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
                break;

        }
    }

    /**
     * Command: /mse tribe
     *
     * @param sender
     */
    private void processCommand(CommandSender sender) {
        if (sender instanceof Player) {

            TribeMember member = tribeMemberRegistry.getTribeMember((Player) sender);
            if (member == null || !member.hasTribe()) {
                sendNoTribeMembershipErrorTo(sender);

            } else
                printTribeInfo(sender, member.getTribe().getName());
        } else
            sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
    }

    /**
     * Command: /mse tribe leave
     * <p>
     * Needs additional confirmation to fulfill this request.
     * See {@link #confirmAction(Player)}
     *
     * @param sender
     */
    private void processCommandLeave(CommandSender sender) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
            return;
        }

        Player player = (Player) sender;
        Tribe tribe = tribeMemberRegistry.getTribeOf(player);

        if (tribe == null) {
            sendNoTribeMembershipErrorTo(player);
            return;
        }

        if (tribe.getMembers().size() == 1) {
            player.sendMessage(ChatMessages.WARNING_DELETE_TRIBE.setParams(tribe.getName()));
        } else {
            player.sendMessage(ChatMessages.WARNING_LEAVE_TRIBE.setParams(tribe.getName()));
            if (tribe.isFounder(player))
                player.sendMessage(ChatMessages.WARNING_TRANSFER_OWNERSHIP.setParams());
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
     * Command: /mse tribe confirm
     *
     * @param sender
     */
    private void processCommandConfirm(CommandSender sender) {
        if (sender instanceof Player)
            confirmAction((Player) sender);
        else
            sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
    }

    /**
     * Command: /mse tribe help
     * or
     * Command: /mse tribe <tribe>
     *
     * @param sender
     */
    private void processCommandInfo(CommandSender sender, String args[]) {
        if (args[1].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
            sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
        } else
            printTribeInfo(sender, args[1]);
    }

    /**
     * Command: /mse tribe invite <player>
     *
     * @param invitingPlayer
     * @param args
     */
    private void processCommandInvite(Player invitingPlayer, String args[]) {

        TribeMember invitingTribeMember = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(invitingPlayer);

        if (!invitingTribeMember.hasTribe()) {
            sendNoTribeMembershipErrorTo(invitingPlayer);
            return;
        }

        String playerName = args[2];
        Player invitedPlayer = Bukkit.getPlayer(playerName);

        if (invitedPlayer == null) {
            invitingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(playerName));
            return;
        }

        if (TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(invitedPlayer).hasTribe()) {
            invitingPlayer.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE2.setParams(playerName));

        } else if (invitingTribeMember.canRecruit()) {
            sendInvite(invitedPlayer, invitingTribeMember.getTribe());
            invitingPlayer.sendMessage(ChatMessages.TRIBE_INVITED_PLAYER.setParams(playerName));
            invitedPlayer.sendMessage(ChatMessages.TRIBE_INVITE_RECEIVED
                    .setParams(invitingPlayer.getDisplayName(), invitingTribeMember.getTribe().getName()));
        } else
            invitingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Sends an invitation to a player to join a tribe.
     *
     * @param player
     */
    private void sendInvite(Player player, Tribe tribe) {

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
     * Command: /mse tribe create help
     * or
     * Command: /mse tribe create <tribe>
     *
     * @param sender
     * @param args
     */
    private void processCommandCreate(CommandSender sender, String args[]) {

        if (args[2].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE1.setParams());
            sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE2.setParams());
        } else
            createTribe((Player) sender, args[2]);

    }

    /**
     * Command: /mse tribe promote <player>
     *
     * @param sender
     * @param args
     * @param promote True if the target should be promoted, false if demoted
     */
    private void processCommandPromoteOrDemote(CommandSender sender, String args[], boolean promote) {

        if (args[2].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatMessages.HELP_TRIBE_PROMOTE1.setParams());
            sender.sendMessage(ChatMessages.HELP_TRIBE_PROMOTE2.setParams());
            return;

        }

        Player executingPlayer = (Player) sender;
        TribeMemberRegistry registry = TribeMemberRegistry.getTribeMemberRegistry();
        TribeMember executingMember = registry.getTribeMember(executingPlayer);

        if (!executingMember.hasTribe()) {
            sendNoTribeMembershipErrorTo(executingPlayer);
            return;
        }

        String playerName = args[2];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        TribeMember targetMember = registry.getTribeMember(targetPlayer);

        if (targetPlayer == null) {
            executingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(playerName));
            return;
        }

        if (!targetMember.hasTribe() || !executingMember.getTribe().getUniqueID().equals(targetMember.getTribe().getUniqueID())) {
            sender.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(playerName));

        } else if (executingMember.canPromote() && Rank.rankIsHigher(executingMember, targetMember)) {
            if (promote)
                targetMember.setRank(Rank.getNextHigher(targetMember.getRank()));
            else
                targetMember.setRank(Rank.getNextLower(targetMember.getRank()));
            sender.sendMessage(ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(playerName, targetMember.getRank().toString()));
            targetPlayer.sendMessage(ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(playerName, targetMember.getRank().toString()));

        } else
            executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Command: /mse tribe leave help
     *
     * @param sender
     */
    private void processCommandLeaveHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE2.setParams());
    }

    /**
     * Command: /mse tribe confirm help
     *
     * @param sender
     */
    private void processCommandConfirmHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_CONFIRM1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_CONFIRM2.setParams());
    }

    /**
     * Can be used to confirm a tribe action if called within 20 seconds after the initial request.
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
            UUID newFounder = tribe.remove(player);
            pendingTribeLeaves.remove(playerUUID);
            player.sendMessage(ChatMessages.TRIBE_YOU_LEFT.setParams(tribe.getName()));
            tribe.sendMessageToMembers(ChatMessages.TRIBE_MEMBER_LEFT.setParams(player.getName()));
            if (newFounder != null) {
                String founderName = Bukkit.getOfflinePlayer(newFounder).getName();
                tribe.sendMessageToMembers(ChatMessages.TRIBE_NEW_FOUNDER.setParams(founderName));
            }

        } else if (pendingTribeInvitations.get(playerUUID) != null) {

            Tribe tribe = pendingTribeInvitations.get(playerUUID);
            tribe.sendMessageToMembers(ChatMessages.TRIBE_NEW_MEMBER_RECRUITED.setParams(player.getName()));
            tribe.addNewMember(playerUUID);
            pendingTribeInvitations.remove(playerUUID);
            player.sendMessage(ChatMessages.TRIBE_WELCOME_MESSAGE.setParams(tribe.getName()));

        } else {
            player.sendMessage(ChatMessages.ERROR_NOTHING_TO_CONFIRM.setParams());
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
    private void createTribe(Player player, String tribeName) {

        TribeMember tribeMember = tribeMemberRegistry.getTribeMember(player);

        if (tribeMember.hasTribe())
            player.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE1.setParams(tribeMember.getTribe().getName()));
        else if (tribeRegistry.tribeExists(tribeName))
            player.sendMessage(ChatMessages.ERROR_TRIBE_EXISTS_ALREADY.setParams(tribeName));
        else {
            new Tribe(player, tribeName);
            player.sendMessage(ChatMessages.TRIBE_CREATED.setParams(tribeName));
        }

    }

    private void sendNoTribeMembershipErrorTo(CommandSender sender) {
        sender.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
    }

}
