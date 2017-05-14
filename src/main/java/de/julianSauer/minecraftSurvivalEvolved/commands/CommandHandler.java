package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.messages.ChatMessages;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeRegistry;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents an object that can process a command.
 */
public abstract class CommandHandler {

    TribeRegistry tribeRegistry;

    CommandHandler() {
        tribeRegistry = TribeRegistry.getTribeRegistry();
    }

    abstract void process(CommandSender sender, String... args);

    /**
     * Prints only a certain range of entries within a Collection.
     *
     * @param sender         Receives the message
     * @param content        Contains all entries
     * @param page           Defines the range depending on entriesPerPage
     * @param entriesPerPage Defines how many entries will be printed
     */
    void printPage(CommandSender sender, Collection<String> content, int page, int entriesPerPage) {

        int pageCount = (int) Math.ceil((double) content.size() / (double) entriesPerPage);
        if (page > pageCount) {
            sender.sendMessage(ChatMessages.ERROR_PAGE_DOESNT_EXIST.setParams("" + page));
            return;
        }

        sender.sendMessage(ChatMessages.PAGE_COUNT.setParams(
                "" + (page),
                "" + pageCount)
        );

        String[] entries = content.toArray(new String[0]);
        Arrays.sort(entries);

        int start = (page - 1) * entriesPerPage;
        int end = start + entriesPerPage;
        if (end > entries.length)
            end = entries.length;

        for (int i = start; i < end; i++)
            sender.sendMessage("- " + entries[i]);

    }

}
