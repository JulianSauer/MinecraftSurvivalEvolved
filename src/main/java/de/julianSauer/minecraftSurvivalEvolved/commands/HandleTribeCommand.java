package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.gui.inventories.InventoryGUI;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.messages.ChatMessages;
import de.julianSauer.minecraftSurvivalEvolved.tribes.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all sub commands of /mse tribe.
 */
public class HandleTribeCommand extends CommandHandler {

    private TribeMemberRegistry tribeMemberRegistry;
    private Set<UUID> pendingTribeLeaves;
    private Map<UUID, Tribe> pendingTribeInvitations;
    private Map<UUID, AbstractMap.SimpleEntry<UUID, Tribe>> pendingTribeTransfers; // Current founder, <new founder, tribe>

    Set<String> tribeNameExceptions = new HashSet<>();

    private InventoryGUI gui;

    HandleTribeCommand() {
        super();
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
        pendingTribeLeaves = new HashSet<>();
        pendingTribeInvitations = new HashMap<>();
        pendingTribeTransfers = new HashMap<>();

        tribeNameExceptions.add("");
        tribeNameExceptions.add("leave");
        tribeNameExceptions.add("confirm");
        tribeNameExceptions.add("log");
        tribeNameExceptions.add("invite");
        tribeNameExceptions.add("create");
        tribeNameExceptions.add("promote");
        tribeNameExceptions.add("demote");
        tribeNameExceptions.add("discharge");
        tribeNameExceptions.add("kick");
        tribeNameExceptions.add("ranks");
        tribeNameExceptions.add("help");

        gui = new InventoryGUI();
    }

    @Override
    public void process(CommandSender sender, String... args) {

        switch (args.length) {

            case 1:

                if (sender instanceof Player)
                    processCommand((Player) sender);
                else
                    sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
                break;

            case 2:

                if (args[1].equalsIgnoreCase("leave")) {
                    if (sender instanceof Player)
                        processCommandLeave((Player) sender);
                    else
                        sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());

                } else if (args[1].equalsIgnoreCase("confirm")) {
                    if (sender instanceof Player)
                        processCommandConfirm((Player) sender);
                    else
                        sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());

                } else if (args[1].equalsIgnoreCase("log")) {
                    if (sender instanceof Player)
                        processCommandLog((Player) sender, 9);
                    else
                        sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());

                } else if (args[1].equalsIgnoreCase("ranks")) {
                    if (sender instanceof Player)
                        processCommandRanks((Player) sender);
                    else
                        sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
                } else if (args[1].equalsIgnoreCase("help"))
                    processCommandHelp(sender);

                else if (args[1].equalsIgnoreCase("invite"))
                    processCommandInviteHelp(sender);

                else if (args[1].equalsIgnoreCase("create"))
                    processCommandCreateHelp(sender);

                else if (args[1].equalsIgnoreCase("promote"))
                    processCommandPromoteHelp(sender);

                else if (args[1].equalsIgnoreCase("demote"))
                    processCommandDemoteHelp(sender);

                else if (args[1].equalsIgnoreCase("discharge") || args[1].equalsIgnoreCase("kick"))
                    processCommandDischargeHelp(sender);

                else if (args[1].equalsIgnoreCase("transfer"))
                    processCommandTransferHelp(sender);

                else if (isTribeNameException(args[1]))
                    sender.sendMessage(ChatMessages.ERROR_INVALID_TRIBE_NAME.setParams(args[1]));

                else
                    processCommand(sender, args[1]);

                break;

            case 3:

                if (sender instanceof Player) {
                    if (args[1].equals("invite")) {
                        processCommandInvite((Player) sender, args[2]);
                        break;

                    } else if (args[1].equalsIgnoreCase("create")) {
                        processCommandCreate((Player) sender, args);
                        break;

                    } else if (args[1].equalsIgnoreCase("promote") && !args[2].equals("")) {
                        processCommandPromoteOrDemote((Player) sender, args[2], true);
                        break;

                    } else if (args[1].equalsIgnoreCase("demote") && !args[2].equals("")) {
                        processCommandPromoteOrDemote((Player) sender, args[2], false);
                        break;

                    } else if (args[1].equalsIgnoreCase("discharge") || args[1].equalsIgnoreCase("kick")) {
                        processCommandDischarge((Player) sender, args[2]);
                        break;

                    } else if (args[1].equalsIgnoreCase("log")) {
                        processCommandLog((Player) sender, args[2]);
                        break;

                    } else if (args[1].equalsIgnoreCase("transfer")) {
                        processCommandTransfer((Player) sender, args[2]);
                        break;

                    }

                } else {
                    sender.sendMessage(ChatMessages.ERROR_SENDER_NO_PLAYER.setParams());
                    break;
                }

            default:

                if (!args[2].equalsIgnoreCase("help"))
                    sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.setParams());

                if (args[1].equalsIgnoreCase("leave"))
                    processCommandLeaveHelp(sender);

                else if (args[1].equalsIgnoreCase("confirm"))
                    processCommandConfirmHelp(sender);

                else if (args[1].equalsIgnoreCase("log"))
                    processCommandLogHelp(sender);

                else if (args[1].equalsIgnoreCase("invite"))
                    processCommandInviteHelp(sender);

                else if (args[1].equalsIgnoreCase("create"))
                    processCommandCreateHelp(sender);

                else if (args[1].equalsIgnoreCase("promote"))
                    processCommandPromoteHelp(sender);

                else if (args[1].equalsIgnoreCase("demote"))
                    processCommandDemoteHelp(sender);

                else if (args[1].equalsIgnoreCase("discharge") || args[1].equalsIgnoreCase("kick"))
                    processCommandDischargeHelp(sender);

                else if (args[1].equalsIgnoreCase("transfer"))
                    processCommandTransferHelp(sender);

                else
                    processCommandHelp(sender);

                break;

        }
    }

    /**
     * Command: /mse tribe
     *
     * @param player
     */
    private void processCommand(Player player) {

        TribeMember member = tribeMemberRegistry.getTribeMember((Player) player);
        if (member == null || !member.hasTribe()) {
            sendNoTribeMembershipErrorTo(player);

        } else
            processCommand(player, member.getTribe().getName());
    }

    /**
     * Command: /mse tribe <tribe>
     *
     * @param sender
     */
    private void processCommand(CommandSender sender, String tribeName) {

        if (tribeRegistry.tribeExists(tribeName)) {
            Tribe tribe = tribeRegistry.getTribe(tribeName);

            Collection<String> content = tribe.getMembers().stream()
                    .map(offlinePlayer -> ChatMessages.TRIBE_PRINT_MEMBER.setParams(
                            offlinePlayer.getName(),
                            tribe.getRankOfMember(offlinePlayer.getUniqueId()).toString()))
                    .collect(Collectors.toList());

            String headline = ChatMessages.TRIBE_PRINT_MEMBERS.setParams(tribeName);
            printPage(sender, content, 1, content.size(), true, headline);

        } else
            sender.sendMessage(ChatMessages.ERROR_TRIBE_DOESNT_EXIST.setParams(tribeName));

    }

    /**
     * Command: /mse tribe help
     *
     * @param sender
     */
    private void processCommandHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
    }

    /**
     * Command: /mse tribe leave
     * <p>
     * Needs additional confirmation to fulfill this request.
     * See {@link #processCommandConfirm(Player)}
     *
     * @param player
     */
    private void processCommandLeave(Player player) {

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
                player.sendMessage(ChatMessages.WARNING_LEAVE_TRIBE_OWNERSHIP.setParams());
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
     * Command: /mse tribe leave help
     *
     * @param sender
     */
    private void processCommandLeaveHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE2.setParams());
    }

    /**
     * Command: /mse tribe confirm
     * Can be used to confirm a tribe action if called within 20 seconds after the initial request.
     *
     * @param player
     */
    private void processCommandConfirm(Player player) {

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

        } else if (pendingTribeTransfers.containsKey(playerUUID)) {

            Tribe tribe = pendingTribeTransfers.get(playerUUID).getValue();
            tribe.setRankOf(player, Rank.LEADER);
            UUID newFounder = pendingTribeTransfers.get(playerUUID).getKey();
            tribe.setFounder(newFounder);
            tribe.sendMessageToMembers(ChatMessages.TRIBE_NEW_FOUNDER.setParams(Bukkit.getPlayer(newFounder).getName()));

        } else {
            player.sendMessage(ChatMessages.ERROR_NOTHING_TO_CONFIRM.setParams());
        }

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
     * Command: /mse tribe log
     *
     * @param player
     * @param entries
     */
    private void processCommandLog(Player player, int entries) {

        TribeMember member = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(player);
        if (member == null || !member.hasTribe()) {
            player.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.setParams());
            return;
        }

        TribeLogger logger = member.getTribe().getLogger();
        String headline = ChatMessages.TRIBE_LOG.setParams("" + entries, "" + logger.getLog().size());
        printPage(player, member.getTribe().getLogger().getLatestEntries(entries), 1, entries, false, headline);

    }

    /**
     * Command: /mse tribe ranks
     *
     * @param player
     */
    private void processCommandRanks(Player player) {

        TribeMember member = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(player);
        if (member == null || !member.hasTribe()) {
            player.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.setParams());
            return;
        }

        Tribe tribe = member.getTribe();
        if (Rank.rankIsEqualOrHigher(member.getRank(), tribe.getRankFor(RankPermission.CHANGING_RANKS))) {
            gui.openRankEditGUI(player);
        } else {
            gui.openRankViewGUI(player);
        }

    }

    /**
     * Command: /mse tribe log [help | <number of entries>]
     *
     * @param player
     * @param entriesString
     */
    private void processCommandLog(Player player, String entriesString) {

        if (entriesString.equalsIgnoreCase("help")) {
            processCommandLogHelp(player);
            return;
        }

        int entries;
        try {
            entries = Integer.parseInt(entriesString);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatMessages.ERROR_NOT_A_NUMBER.setParams("second"));
            return;
        }

        processCommandLog(player, entries);

    }

    /**
     * Command: /mse tribe log help
     *
     * @param sender
     */
    private void processCommandLogHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_LOG1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_LOG2.setParams());
    }

    /**
     * Command: /mse tribe invite <player>
     *
     * @param invitingPlayer
     * @param invitedPlayerName
     */
    private void processCommandInvite(Player invitingPlayer, String invitedPlayerName) {

        if (invitedPlayerName.equalsIgnoreCase("help")) {
            invitingPlayer.sendMessage(ChatMessages.HELP_TRIBE_INVITE1.setParams());
            invitingPlayer.sendMessage(ChatMessages.HELP_TRIBE_INVITE2.setParams());
            return;
        }

        TribeMember invitingTribeMember = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(invitingPlayer);

        if (invitingTribeMember == null || !invitingTribeMember.hasTribe()) {
            sendNoTribeMembershipErrorTo(invitingPlayer);
            return;
        }

        Player invitedPlayer = Bukkit.getPlayer(invitedPlayerName);

        if (invitedPlayer == null) {
            invitingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(invitedPlayerName));
            return;
        }

        TribeMember invitedTribeMember = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(invitedPlayer);
        if (invitedTribeMember == null || invitedTribeMember.hasTribe()) {
            invitingPlayer.sendMessage(ChatMessages.ERROR_ALREADY_JOINED_A_TRIBE2.setParams(invitedPlayerName));

        } else if (invitingTribeMember.isAllowedTo(RankPermission.RECRUITING)) {
            sendInvite(invitedPlayer, invitingTribeMember.getTribe());
            invitingPlayer.sendMessage(ChatMessages.TRIBE_INVITED_PLAYER.setParams(invitedPlayerName));
            invitedPlayer.sendMessage(ChatMessages.TRIBE_INVITE_RECEIVED
                    .setParams(invitingPlayer.getDisplayName(), invitingTribeMember.getTribe().getName()));
        } else
            invitingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Command: /mse tribe invite help
     *
     * @param sender
     */
    private void processCommandInviteHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_INVITE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_INVITE2.setParams());
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
     * Command: /mse tribe create [help | <tribe>]
     *
     * @param player
     * @param args
     */
    private void processCommandCreate(Player player, String args[]) {

        if (args[2].equalsIgnoreCase("help")) {
            processCommandCreateHelp(player);
        } else if (isTribeNameException(args[2])) {
            player.sendMessage(ChatMessages.ERROR_INVALID_TRIBE_NAME.setParams(args[1]));
        } else
            createTribe(player, args[2]);

    }

    /**
     * Command: /mse tribe create help
     *
     * @param sender
     */
    private void processCommandCreateHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE2.setParams());
    }

    /**
     * Command: /mse tribe promote <player>
     *
     * @param executingPlayer
     * @param targetPlayerName
     * @param promote          True if the target should be promoted, false if demoted
     */
    private void processCommandPromoteOrDemote(Player executingPlayer, String targetPlayerName, boolean promote) {

        if (targetPlayerName.equalsIgnoreCase("help")) {
            if (promote)
                processCommandPromoteHelp(executingPlayer);
            else
                processCommandDemoteHelp(executingPlayer);
            return;

        }

        TribeMemberRegistry registry = TribeMemberRegistry.getTribeMemberRegistry();
        TribeMember executingMember = registry.getTribeMember(executingPlayer);

        if (executingMember == null || !executingMember.hasTribe()) {
            sendNoTribeMembershipErrorTo(executingPlayer);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        TribeMember targetMember = registry.getTribeMember(targetPlayer);

        if (targetPlayer == null || targetMember == null) {
            executingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(targetPlayerName));
            return;
        }

        if (!targetMember.hasTribe() || !executingMember.getTribe().getUniqueID().equals(targetMember.getTribe().getUniqueID())) {
            executingPlayer.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(targetPlayerName));

        } else if (executingMember.isAllowedTo(RankPermission.PROMOTING) && Rank.rankIsHigher(executingMember, targetMember)) {
            if (promote) {
                Rank newRank = Rank.getNextHigher(targetMember.getRank());
                if (newRank == Rank.FOUNDER)
                    executingPlayer.sendMessage(ChatMessages.ERROR_ONLY_ONE_FOUNDER.setParams());
                else if (newRank == executingMember.getRank())
                    executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());
                else {
                    targetMember.setRank(newRank);
                    executingPlayer.sendMessage(ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(targetPlayerName, targetMember.getRank().toString()));
                    targetPlayer.sendMessage(ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(targetPlayerName, targetMember.getRank().toString()));
                }
            } else
                targetMember.setRank(Rank.getNextLower(targetMember.getRank()));

        } else
            executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Command: /mse tribe promote help
     *
     * @param sender
     */
    private void processCommandPromoteHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_PROMOTE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_PROMOTE2.setParams());
    }

    /**
     * Command: /mse tribe demote help
     *
     * @param sender
     */
    private void processCommandDemoteHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_DEMOTE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_DEMOTE2.setParams());
    }

    /**
     * Command: /mse [discharge | kick] <player>
     *
     * @param executingPlayer
     * @param targetPlayerName
     */
    private void processCommandDischarge(Player executingPlayer, String targetPlayerName) {

        if (targetPlayerName.equalsIgnoreCase("help")) {
            processCommandDischargeHelp(executingPlayer);
            return;
        }

        TribeMemberRegistry registry = TribeMemberRegistry.getTribeMemberRegistry();
        TribeMember executingMember = registry.getTribeMember(executingPlayer);

        if (executingMember == null || !executingMember.hasTribe()) {
            sendNoTribeMembershipErrorTo(executingPlayer);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        TribeMember targetMember = registry.getTribeMember(targetPlayer);

        if (targetPlayer == null || targetMember == null) {
            executingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(targetPlayerName));
            return;
        }

        if (!targetMember.hasTribe() || !executingMember.getTribe().getUniqueID().equals(targetMember.getTribe().getUniqueID())) {
            executingPlayer.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(targetPlayerName));

        } else if (executingMember.isAllowedTo(RankPermission.DISCHARGING) && Rank.rankIsHigher(executingMember, targetMember)) {
            Tribe tribe = targetMember.getTribe();
            tribe.remove(targetPlayer);
            String message = ChatMessages.TRIBE_DISCHARGED_MEMBER.setParams(targetPlayer.getName(), executingPlayer.getName());
            tribe.sendMessageToMembers(message);
            targetPlayer.sendMessage(message);

        } else
            executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Command: /mse [discharge | kick] help
     *
     * @param sender
     */
    private void processCommandDischargeHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_DISCHARGE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_DISCHARGE2.setParams());
    }

    /**
     * Command: /mse tribe transfer <player>
     *
     * @param executingPlayer
     * @param targetPlayerName
     */
    private void processCommandTransfer(Player executingPlayer, String targetPlayerName) {

        if (targetPlayerName.equalsIgnoreCase("help")) {
            processCommandTransferHelp(executingPlayer);
            return;
        }

        TribeMemberRegistry registry = TribeMemberRegistry.getTribeMemberRegistry();
        TribeMember executingMember = registry.getTribeMember(executingPlayer);

        if (executingMember == null || !executingMember.hasTribe()) {
            sendNoTribeMembershipErrorTo(executingPlayer);
            return;
        }

        Tribe tribe = executingMember.getTribe();
        if (!tribe.isFounder(executingPlayer)) {
            executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        TribeMember targetMember = registry.getTribeMember(targetPlayer);

        if (targetPlayer == null || targetMember == null) {
            executingPlayer.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(targetPlayerName));
            return;
        }

        if (!targetMember.hasTribe() || !tribe.getUniqueID().equals(targetMember.getTribe().getUniqueID())) {
            executingPlayer.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(targetPlayerName));

        } else {
            executingPlayer.sendMessage(ChatMessages.WARNING_TRANSFER_OWNERSHIP.setParams(tribe.getName(), targetPlayer.getName()));
            pendingTribeTransfers.put(executingMember.getUniqueId(), new AbstractMap.SimpleEntry<>(targetMember.getUniqueId(), tribe));
            // Player has 20 seconds to confirm the action.
            new BukkitRunnable() {
                @Override
                public void run() {
                    pendingTribeTransfers.remove(executingMember.getUniqueId());
                    this.cancel();
                }
            }.runTaskTimerAsynchronously(MSEMain.getInstance(), 400, 0);

        }

    }

    /**
     * Command: /mse tribe transfer help
     *
     * @param sender
     */
    private void processCommandTransferHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_TRANSFER1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_TRANSFER2.setParams());
    }

    /**
     * Creates a new tribe if the name is available and gives the player a leader rank within it.
     */
    private void createTribe(Player player, String tribeName) {

        TribeMember tribeMember = tribeMemberRegistry.getTribeMember(player);

        if (tribeMember == null)
            MSEMain.getInstance().getLogger().warning("Could not create tribe " + tribeName + ". Founder " + player.getName() + " is offline."); // Throws NPE afterwards

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

    /**
     * Checks if a tribe name is an alias for a command or invalid.
     *
     * @param name Possible tribe name
     * @return True if the name is valid
     */
    private boolean isTribeNameException(String name) {
        for (String exception : tribeNameExceptions)
            if (name.equalsIgnoreCase(exception))
                return true;
        return false;
    }

}
