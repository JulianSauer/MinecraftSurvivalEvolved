package de.juliansauer.minecraftsurvivalevolved.commands;

import de.juliansauer.minecraftsurvivalevolved.messages.ChatMessages;
import de.juliansauer.minecraftsurvivalevolved.tribes.Tribe;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Handles all sub commands of /mse tribes.
 */
class CommandTribes extends BasicCommand implements MSECommand {

    private String[] commandAliases;

    public CommandTribes() {
        super();
        commandAliases = new String[]{"tribes"};
    }

    @Override
    public String[] getCommandAliases() {
        return commandAliases;
    }

    @Override
    public void process(CommandSender sender, String... args) {

        if (!sender.hasPermission("MinecraftSurvivalEvolved.tribes")) {
            sender.sendMessage(ChatMessages.ERROR_NO_PERMISSION.setParams());
            return;
        }

        switch (args.length) {

            case 1:

                processCommand(sender);
                break;

            case 2:

                if (args[1].equalsIgnoreCase("help"))
                    processCommandHelp(sender);
                else
                    processCommand(sender, args);
                break;

            default:

                sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.setParams());
                break;
        }

    }

    /**
     * Command: /mse tribes
     * Prints the first 9 tribes of this server.
     */
    private void processCommand(CommandSender sender) {
        printTribes(sender, 1);
    }

    /**
     * Command: /mse tribes help
     */
    private void processCommandHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBES1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBES2.setParams());
    }

    /**
     * Command: /mse tribes <page>
     * Parses the user input to print a specific page of tribes.
     */
    private void processCommand(CommandSender sender, String args[]) {

        int page;
        try {
            page = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatMessages.ERROR_NOT_A_NUMBER.setParams("second"));
            return;
        }
        printTribes(sender, page);

    }

    /**
     * Prints 9 tribes of the server.
     */
    private void printTribes(CommandSender sender, int page) {

        Collection<Tribe> tribes = tribeRegistry.getTribes();
        if (tribes.size() == 0) {
            sender.sendMessage(ChatMessages.ERROR_NO_TRIBES_EXIST.setParams());
            return;
        }

        Collection<String> tribeNames = tribes.stream().map(Tribe::getName).collect(Collectors.toList());
        printPage(sender, tribeNames, page, 9, true, null);

    }

}
