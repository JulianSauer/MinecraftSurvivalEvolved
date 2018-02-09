package de.juliansauer.minecraftsurvivalevolved.commands;

import org.bukkit.command.CommandSender;

public interface MSECommand {

    /**
     * List of aliases for this command.
     *
     * @return Aliases
     */
    String[] getCommandAliases();

    /**
     * Compares aliases of this command with a given command name.
     *
     * @param command Name to check
     * @return True if the given command is an alias
     */
    boolean equalsAnyAlias(String command);

    /**
     * Processes this command.
     *
     * @param sender Player or server who issued this command
     * @param args   Parameters for the call
     */
    void process(CommandSender sender, String... args);

}
