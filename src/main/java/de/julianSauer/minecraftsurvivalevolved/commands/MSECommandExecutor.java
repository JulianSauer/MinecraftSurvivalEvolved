package de.juliansauer.minecraftsurvivalevolved.commands;

import de.juliansauer.minecraftsurvivalevolved.messages.ChatMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MSECommandExecutor implements CommandExecutor {

    private CommandHandler handleForceTame;
    private CommandHandler handleTribeCommand;
    private CommandHandler handleTribesCommand;

    public MSECommandExecutor() {
        handleForceTame = new HandleForceTame();
        handleTribeCommand = new HandleTribeCommand();
        handleTribesCommand = new HandleTribesCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("mse"))
            return false;

        if (args.length >= 1) {

            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatMessages.HELP1.setParams());
                sender.sendMessage(ChatMessages.HELP2.setParams());

            } else if ((args[0].equalsIgnoreCase("forcetame") || args[0].equalsIgnoreCase("ft"))) {
                handleForceTame.process(sender, args);

            } else if (args[0].equalsIgnoreCase("tribe")) {
                handleTribeCommand.process(sender, args);

            } else if (args[0].equalsIgnoreCase("tribes")) {
                handleTribesCommand.process(sender, args);
            }
        } else {
            sender.sendMessage(ChatMessages.HELP1.setParams());
            sender.sendMessage(ChatMessages.HELP2.setParams());
        }

        return true;

    }

}
