package de.juliansauer.minecraftsurvivalevolved.commands;

import de.juliansauer.minecraftsurvivalevolved.gui.inventories.InventoryGUI;
import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import de.juliansauer.minecraftsurvivalevolved.messages.ChatMessages;
import de.juliansauer.minecraftsurvivalevolved.tribes.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all sub commands of /mse tribe.
 */
class CommandTribe extends BasicCommand implements MSECommand {

    private TribeMemberRegistry tribeMemberRegistry;
    private Set<UUID> pendingTribeLeaves;
    private Map<UUID, Tribe> pendingTribeInvitations;
    private Map<UUID, AbstractMap.SimpleEntry<UUID, Tribe>> pendingTribeTransfers; // Current founder, <new founder, tribe>

    private Set<String> tribeNameExceptions = new HashSet<>();

    private InventoryGUI gui;

    private String[] commandAliases;

    public CommandTribe() {
        super();
        commandAliases = new String[]{"tribe"};
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
        tribeNameExceptions.add("transfer");
        tribeNameExceptions.add("ranks");
        tribeNameExceptions.add("help");

        gui = new InventoryGUI();
    }

    @Override
    public String[] getCommandAliases() {
        return commandAliases;
    }

    @Override
    public void process(CommandSender sender, String... args) {

        if (!sender.hasPermission("MinecraftSurvivalEvolved.tribe")) {
            sender.sendMessage(ChatMessages.ERROR_NO_PERMISSION.setParams());
            return;
        }

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

                else if (args[1].equalsIgnoreCase("ranks"))
                    processCommandRanksHelp(sender);

                else
                    processCommandHelp(sender);

                break;

        }
    }

    /**
     * Command: /mse tribe
     * <p>
     * Shows info about tribe of player or help for creating a new tribe.
     */
    private void processCommand(Player player) {

        TribeMember member = tribeMemberRegistry.getTribeMember(player);
        if (member == null || !member.hasTribe()) {
            sendNoTribeMembershipErrorTo(player);

        } else
            processCommand(player, member.getTribe().getName());
    }

    /**
     * Command: /mse tribe <tribe>
     * <p>
     * Shows info about a specific tribe.
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
     */
    private void processCommandHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE3.setParams());
    }

    /**
     * Command: /mse tribe leave
     * <p>
     * Allows a player to leave their tribe. Needs additional confirmation to fulfill this request.
     * See {@link #processCommandConfirm(Player)}
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
     */
    private void processCommandLeaveHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_LEAVE2.setParams());
    }

    /**
     * Command: /mse tribe confirm
     * <p>
     * Can be used to confirm a tribe action if called within 20 seconds after the initial request.
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
            tribe.sendMessageToMembers(ChatMessages.TRIBE_NEW_FOUNDER.setParams(Bukkit.getOfflinePlayer(newFounder).getName()));

        } else {
            player.sendMessage(ChatMessages.ERROR_NOTHING_TO_CONFIRM.setParams());
        }

    }

    /**
     * Command: /mse tribe confirm help
     */
    private void processCommandConfirmHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_CONFIRM1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_CONFIRM2.setParams());
    }

    /**
     * Command: /mse tribe log
     * <p>
     * Displays the tribe's log.
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
     * <p>
     * Opens a UI for viewing or editing the permissions of ranks. UI depends on {@link RankPermission#CHANGING_RANKS}
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
     * Command: /mse tribe ranks help
     */
    private void processCommandRanksHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_RANKS1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_RANKS2.setParams());
    }

    /**
     * Command: /mse tribe log [help | <number of entries>]
     * <p>
     * Displays certain number of log entries or help for the command.
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
     */
    private void processCommandLogHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_LOG1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_LOG2.setParams());
    }

    /**
     * Command: /mse tribe invite <player>
     * <p>
     * Invites a new player to the tribe who needs to confirm the invitation.
     * See {@link #processCommandConfirm(Player)}
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
     */
    private void processCommandInviteHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_INVITE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_INVITE2.setParams());
    }

    /**
     * Sends an invitation to a player to join a tribe.
     * See {@link #processCommandInvite(Player, String)}
     * See {@link #processCommandConfirm(Player)}
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
     * <p>
     * Creates a new tribe or displays help.
     */
    private void processCommandCreate(Player player, String args[]) {

        if (args[2].equalsIgnoreCase("help")) {
            processCommandCreateHelp(player);
        } else if (isTribeNameException(args[2])) {
            player.sendMessage(ChatMessages.ERROR_INVALID_TRIBE_NAME.setParams(args[1]));
        } else {

            String tribeName = args[2];

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

    }

    /**
     * Command: /mse tribe create help
     */
    private void processCommandCreateHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_CREATE2.setParams());
    }

    /**
     * Command: /mse tribe [promote | demote] <player>
     * <p>
     * Promotes or demotes another member to the next higher/lower rank.
     *
     * @param promote True if the target should be promoted, false if demoted
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

        Tribe tribe = executingMember.getTribe();
        UUID targetPlayer = getPlayerFromTribe(executingPlayer, tribe, targetPlayerName);
        if (targetPlayer == null)
            return;

        if (!tribe.isMember(targetPlayer)) {
            executingPlayer.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(targetPlayerName));

        } else if (executingMember.isAllowedTo(RankPermission.PROMOTING) && Rank.rankIsHigher(executingMember.getRank(), tribe.getRankOfMember(targetPlayer))) {
            if (promote) {
                Rank currentRank = tribe.getRankOfMember(targetPlayer);
                Rank newRank = Rank.getNextHigher(currentRank);
                if (newRank == Rank.FOUNDER)
                    executingPlayer.sendMessage(ChatMessages.ERROR_ONLY_ONE_FOUNDER.setParams());
                else if (newRank == executingMember.getRank())
                    executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());
                else {
                    tribe.setRankOf(targetPlayer, newRank);
                    String message = ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(targetPlayerName, newRank.toString());
                    executingPlayer.sendMessage(message);
                    OfflinePlayer targetPlayerOffline = Bukkit.getOfflinePlayer(targetPlayer);
                    if (targetPlayerOffline.isOnline())
                        ((Player) targetPlayerOffline).sendMessage(message);
                }
            } else {
                Rank newRank = Rank.getNextLower(tribe.getRankOfMember(targetPlayer));
                tribe.setRankOf(targetPlayer, newRank);
                String message = ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(targetPlayerName, newRank.toString());
                executingPlayer.sendMessage(message);
                OfflinePlayer targetPlayerOffline = Bukkit.getOfflinePlayer(targetPlayer);
                if (targetPlayerOffline.isOnline())
                    ((Player) targetPlayerOffline).sendMessage(message);
            }

        } else
            executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Command: /mse tribe promote help
     */
    private void processCommandPromoteHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_PROMOTE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_PROMOTE2.setParams());
    }

    /**
     * Command: /mse tribe demote help
     */
    private void processCommandDemoteHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_DEMOTE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_DEMOTE2.setParams());
    }

    /**
     * Command: /mse [discharge | kick] <player>
     * <p>
     * Removes a player from the tribe.
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

        Tribe tribe = executingMember.getTribe();
        UUID targetPlayer = getPlayerFromTribe(executingPlayer, tribe, targetPlayerName);
        if (targetPlayer == null)
            return;

        if (!tribe.isMember(targetPlayer)) {
            executingPlayer.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(targetPlayerName));

        } else if (executingMember.isAllowedTo(RankPermission.DISCHARGING) && Rank.rankIsHigher(executingMember.getRank(), tribe.getRankOfMember(targetPlayer))) {
            tribe.remove(targetPlayer);
            String message = ChatMessages.TRIBE_DISCHARGED_MEMBER.setParams(targetPlayerName, executingPlayer.getName());
            tribe.sendMessageToMembers(message);
            OfflinePlayer targetPlayerOffline = Bukkit.getOfflinePlayer(targetPlayer);
            if (targetPlayerOffline.isOnline())
                ((Player) targetPlayerOffline).sendMessage(message);

        } else
            executingPlayer.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.setParams());

    }

    /**
     * Command: /mse [discharge | kick] help
     */
    private void processCommandDischargeHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_DISCHARGE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_DISCHARGE2.setParams());
    }

    /**
     * Command: /mse tribe transfer <player>
     * <p>
     * Transfers the ownership of a tribe to another member. Needs additional confirmation to fulfill this request.
     * See {@link #processCommandConfirm(Player)}
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

        UUID targetPlayer = getPlayerFromTribe(executingPlayer, tribe, targetPlayerName);

        if (targetPlayer == null)
            return;

        if (!tribe.isMember(targetPlayer)) {
            executingPlayer.sendMessage(ChatMessages.ERROR_DIFFERENT_TRIBE.setParams(targetPlayerName));

        } else {
            executingPlayer.sendMessage(ChatMessages.WARNING_TRANSFER_OWNERSHIP.setParams(tribe.getName(), targetPlayerName));
            pendingTribeTransfers.put(executingMember.getUniqueId(), new AbstractMap.SimpleEntry<>(targetPlayer, tribe));
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
     */
    private void processCommandTransferHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBE_TRANSFER1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE_TRANSFER2.setParams());
    }

    private void sendNoTribeMembershipErrorTo(CommandSender sender) {
        sender.sendMessage(ChatMessages.ERROR_NO_TRIBE_MEMBERSHIP.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE2.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBE3.setParams());
    }


    /**
     * Searches for a tribe member regardless if he is online or not.
     *
     * @param executor   Player executing the command
     * @param tribe      Tribe in which will be searched
     * @param targetName Name of the target member
     * @return UUID of member matching the name or null
     */
    private UUID getPlayerFromTribe(Player executor, Tribe tribe, String targetName) {
        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            OfflinePlayer targetPlayerOffline = tribe.getMember(targetName);
            if (targetPlayerOffline == null) {
                executor.sendMessage(ChatMessages.ERROR_NO_PLAYER_FOUND.setParams(targetName));
                return null;
            } else
                return targetPlayerOffline.getUniqueId();
        } else
            return targetPlayer.getUniqueId();
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
