package de.julianSauer.minecraftSurvivalEvolved.commands;

import org.bukkit.ChatColor;

/**
 * Currently all messages for players are stored here.
 */
public enum ChatMessages {

    NO_PERMISSION(ChatColor.RED + "Sorry but you don't have permission to do that"),
    SENDER_NO_PLAYER(ChatColor.RED + "You have to be a player to be able to perform this command"),
    WRONG_NUMBER_OF_ARGS(ChatColor.RED + "Wrong number of arguments"),
    NOT_A_NUMBER(ChatColor.RED + "The %ARGS0% parameter is not a valid number"),
    VALUE_TOO_BIG(ChatColor.RED + "The value %ARGS0% is too big"),
    PAGE_COUNT("Viewing page %ARGS0% of %ARGS1%:"),
    NO_ENTITY_FOUND(ChatColor.RED + "No entity was found at your viewing direction"),
    PRINT_HELP_FORCETAME("Usage: /mse forcetame [player]"),
    TRIBE_RANK_TOO_LOW(ChatColor.RED + "Your tribe rank is too low"),
    TRIBE_MEMBER_DOESNT_EXIST(ChatColor.RED + "The player %ARGS0% is not part of tribe %ARGS1%"),
    TRIBE_EXISTS_ALREADY(ChatColor.RED + "The tribe %ARGS0% already exists"),
    TRIBE_DOESNT_EXIST(ChatColor.RED + "The tribe %ARGS0% does not exist"),
    TRIBE_PRINT_MEMBERS("Tribe members:");

    ChatMessages(String message) {
        this.message = message;
    }

    private final String message;

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