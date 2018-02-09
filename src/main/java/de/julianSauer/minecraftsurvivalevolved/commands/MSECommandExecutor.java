package de.juliansauer.minecraftsurvivalevolved.commands;

import de.juliansauer.minecraftsurvivalevolved.messages.ChatMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MSECommandExecutor implements CommandExecutor {

    private List<MSECommand> commands;

    public MSECommandExecutor() {
        commands = new ArrayList<>();
        commands.add(new CommandHelp());
        commands.add(new CommandTribe());
        commands.add(new CommandTribes());
        commands.add(new CommandForceTame());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("mse"))
            return false;

        if (args.length >= 1) {

            for (MSECommand mseCommand : commands) {
                if (mseCommand.equalsAnyAlias(args[0]))
                    mseCommand.process(sender, args);
            }

        } else {
            sender.sendMessage(ChatMessages.HELP1.setParams());
            sender.sendMessage(ChatMessages.HELP2.setParams());
        }

        return true;

    }

}
