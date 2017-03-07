package de.julianSauer.minecraftSurvivalEvolved.commands;

import org.bukkit.ChatColor;

/**
 * Currently all messages for players are stored here.
 */
public enum ChatMessages {

    ERROR_NO_PERMISSION(Format.ERROR + "Sorry but you don't have permission to do that."),
    ERROR_SENDER_NO_PLAYER(Format.ERROR + "You have to be a player to be able to perform this command."),
    ERROR_WRONG_NUMBER_OF_ARGS(Format.ERROR + "Wrong number of arguments."),
    ERROR_NOT_A_NUMBER(Format.ERROR + "The %ARGS0% parameter is not a valid number."),
    ERROR_PAGE_DOESNT_EXIST(Format.ERROR + "Page #%ARGS0% does not exist."),
    ERROR_NO_PLAYER_FOUND(Format.ERROR + "Could not find player %ARGS0%."),
    ERROR_NO_ENTITY_FOUND(Format.ERROR + "No entity was found at your viewing direction."),
    ERROR_ALPHA_TAME(Format.ERROR + "You cannot tame alphas."),
    ERROR_TRIBE_EXISTS_ALREADY(Format.ERROR + "The tribe %ARGS0% already exists."),
    ERROR_NO_TRIBES_EXIST(Format.ERROR + "No tribes exist on this server."),
    ERROR_ALREADY_JOINED_A_TRIBE1(Format.ERROR + "You are already a member of the tribe %ARGS0%. Leave it to create your own one."),
    ERROR_ALREADY_JOINED_A_TRIBE2(Format.ERROR + "The player %ARGS0% is already a member of a tribe."),
    ERROR_TRIBE_RANK_TOO_LOW(Format.ERROR + "Your tribe rank is too low."),
    ERROR_TRIBE_MEMBER_DOESNT_EXIST(Format.ERROR + "The player %ARGS0% is not part of tribe %ARGS1%."),
    ERROR_TRIBE_DOESNT_EXIST(Format.ERROR + "The tribe %ARGS0% does not exist."),
    ERROR_NO_TRIBE_MEMBERSHIP(Format.ERROR + "You are not part of any tribe currently."),
    ERROR_NOTHING_TO_CONFIRM(Format.ERROR + "There's currently nothing to confirm."),
    WARNING_LEAVE_TRIBE(Format.WARNING + "Are you sure you want to leave your tribe %ARGS0%? Confirm your actions within 20 seconds using " + ChatColor.ITALIC + "/mse tribe confirm."),
    WARNING_DELETE_TRIBE(Format.WARNING + "Are you sure you want to leave your tribe %ARGS0%? Since you are currently the only member it will be deleted afterwards. Confirm your actions within 20 seconds using " + ChatColor.ITALIC + "/mse tribe confirm."),
    WARNING_TRANSFER_OWNERSHIP(Format.WARNING + "You are the founder of this tribe. Leaving it will transfer the ownership to another member with th highest rank. Use " + ChatColor.ITALIC + "/mse tribe transfer <player>" + Format.WARNING + " first to determine a member yourself."),
    HELP1(Format.HELP_TITLE + "Usage:" + Format.HELP_TEXT + " /mse <forcetame | tribe | tribes>."),
    HELP2(Format.HELP_TITLE + "Description:" + Format.HELP_TEXT + " Access to all Minecraft: Survival Evolved commands."),
    HELP_FORCETAME1(Format.HELP_TITLE + "Usage:" + Format.HELP_TEXT + " /mse forcetame [<player>]."),
    HELP_FORCETAME2(Format.HELP_TITLE + "Description:" + Format.HELP_TEXT + " Can be used to tame an entity. Providing a player name sets the owner of the entity to that player."),
    HELP_TRIBE1(Format.HELP_TITLE + "Usage:" + Format.HELP_TEXT + " /mse tribe <create | leave | <tribe name>>."),
    HELP_TRIBE2(Format.HELP_TITLE + "Description:" + Format.HELP_TEXT + " Can be used to create, join and leave a tribe or list it's members."),
    HELP_TRIBE_CREATE1(Format.HELP_TITLE + "Usage:" + Format.HELP_TEXT + " /mse tribe create <tribe name>."),
    HELP_TRIBE_CREATE2(Format.HELP_TITLE + "Description:" + Format.HELP_TEXT + " Can be used to create a new tribe."),
    HELP_TRIBE_LEAVE1(Format.HELP_TITLE + "Usage:" + Format.HELP_TEXT + " /mse tribe leave."),
    HELP_TRIBE_LEAVE2(Format.HELP_TITLE + "Description:" + Format.HELP_TEXT + " Can be used to leave your current tribe."),
    HELP_TRIBE_CONFIRM1(Format.HELP_TITLE + "Usage: " + Format.HELP_TEXT + "/mse tribe confirm."),
    HELP_TRIBE_CONFIRM2(Format.HELP_TITLE + "Description: " + Format.HELP_TEXT + "Confirms the current tribe action. Has to be used within 20 seconds or it will expire."),
    HELP_TRIBES1(Format.HELP_TITLE + "Usage:" + Format.HELP_TEXT + " /mse tribes."),
    HELP_TRIBES2(Format.HELP_TITLE + "Description:" + Format.HELP_TEXT + " Can be used to list all tribes on this server."),
    PAGE_COUNT(Format.INFO + "Viewing page %ARGS0% of %ARGS1%:"),
    TRIBE_CREATED(Format.SUCCESS + "You successfully created the tribe %ARGS0%!"),
    TRIBE_YOU_LEFT(Format.SUCCESS + "You successfully left the tribe %ARGS0%!"),
    TRIBE_MEMBER_LEFT(Format.INFO + "The player %ARGS0% left your tribe."),
    TRIBE_NEW_FOUNDER(Format.INFO + "%ARGS0% has become the new founder of this tribe!"),
    TRIBE_INVITED_PLAYER(Format.INFO + "You invited %ARGS0%."),
    TRIBE_INVITE_RECEIVED(Format.INFO + "You received an invitation from %ARGS0% to join his tribe %ARGS1%. Use " + ChatColor.ITALIC + "/mse tribe confirm" + Format.INFO + " within 20 seconds to join the tribe."),
    TRIBE_NEW_MEMBER_RECRUITED(Format.INFO + "The player %ARGS0% has joined your tribe."),
    TRIBE_WELCOME_MESSAGE(Format.SUCCESS + "You joined the tribe " + ChatColor.ITALIC + "%ARGS0%!"),
    TRIBE_PRINT_MEMBERS(Format.INFO + "Members of tribe " + ChatColor.ITALIC + "%ARGS0%:"),
    TRIBE_PRINT_MEMBER(Format.INFO + "- %ARGS0% " + ChatColor.ITALIC + "(Rank: %ARGS1%)");

    private static class Format {
        private static String HELP_TITLE = ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.UNDERLINE;
        private static String HELP_TEXT = ChatColor.RESET + "" + ChatColor.WHITE;
        private static String ERROR = ChatColor.RESET + "" + ChatColor.RED;
        private static String WARNING = ChatColor.RESET + "" + ChatColor.GOLD;
        private static String INFO = ChatColor.RESET + "" + ChatColor.YELLOW;
        private static String SUCCESS = ChatColor.RESET + "" + ChatColor.GREEN;
    }

    private final String message;

    ChatMessages(String message) {
        this.message = message;
    }

    public String setParams(String... args) {
        String ret = this.toString();
        for (int i = 0; i < args.length; i++)
            ret = ret.replace("%ARGS" + i + "%", args[i]);
        return ret;
    }

    @Override
    public String toString() {
        return message;
    }

}