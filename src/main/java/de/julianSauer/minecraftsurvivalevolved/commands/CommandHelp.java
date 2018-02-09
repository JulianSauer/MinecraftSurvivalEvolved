package de.juliansauer.minecraftsurvivalevolved.commands;

import de.juliansauer.minecraftsurvivalevolved.messages.ChatMessages;
import org.bukkit.command.CommandSender;

public class CommandHelp extends BasicCommand implements MSECommand {

    private String[] commandAliases;

    public CommandHelp() {
        commandAliases = new String[]{"help"};
    }

    @Override
    public String[] getCommandAliases() {
        return commandAliases;
    }

    @Override
    public void process(CommandSender sender, String... args) {
        sender.sendMessage(ChatMessages.HELP1.setParams());
        sender.sendMessage(ChatMessages.HELP2.setParams());
    }
}
