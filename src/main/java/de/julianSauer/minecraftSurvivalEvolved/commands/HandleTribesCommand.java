package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all sub commands of /mse tribes.
 */
public class HandleTribesCommand extends CommandHandler {

    @Override
    public void process(CommandSender sender, String... args) {

        switch (args.length) {

            case 1:

                processCommand(sender);
                break;

            case 2:

                if (args[1].equalsIgnoreCase("help"))
                    processCommandHelp(sender);
                else
                    processCommandPage(sender, args);
                break;

            default:

                sender.sendMessage(ChatMessages.ERROR_WRONG_NUMBER_OF_ARGS.setParams());
                break;
        }

    }

    /**
     * Command: /mse tribes
     * Prints the first 10 tribes of this server.
     *
     * @param sender
     */
    private void processCommand(CommandSender sender) {
        printTribes(sender, 1);
    }

    /**
     * Command: /mse tribes help
     *
     * @param sender
     */
    private void processCommandHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.HELP_TRIBES1.setParams());
        sender.sendMessage(ChatMessages.HELP_TRIBES2.setParams());
    }

    /**
     * Command: /mse tribes <page>
     * Parses the user input to print a specific page of tribes
     *
     * @param sender
     * @param args
     */
    private void processCommandPage(CommandSender sender, String args[]) {

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
     * Prints 10 tribes of the server.
     *
     * @param sender
     * @param page
     */
    private void printTribes(CommandSender sender, int page) {

        Collection<Tribe> tribes = tribeRegistry.getTribes();
        if (tribes.size() == 0) {
            sender.sendMessage(ChatMessages.ERROR_NO_TRIBES_EXIST.setParams());
            return;
        }

        int pageCount = (int) Math.ceil((double) tribes.size() / 10.0);
        if (page > pageCount) {
            sender.sendMessage(ChatMessages.ERROR_PAGE_DOESNT_EXIST.setParams("" + page));
            return;
        }

        sender.sendMessage(ChatMessages.PAGE_COUNT.setParams(
                "" + (page),
                "" + pageCount)
        );

        List<String> tribeNames = splitIntoPages(
                tribes.stream().map(Tribe::getName).collect(Collectors.toList()), 10)
                .get(page - 1);

        for (String tribeName : tribeNames)
            sender.sendMessage("- " + tribeName);

    }

    /**
     * Joins list entries to pages.
     *
     * @param entryList      Text that should be processed
     * @param entriesPerPage Defines the number of entries from the list on each page
     * @return Collection that maps page numbers to text
     */
    private Map<Integer, List<String>> splitIntoPages(List<String> entryList, int entriesPerPage) {

        entryList = entryList.stream().sorted().collect(Collectors.toList());
        Map<Integer, List<String>> pagedMap = new HashMap<>();
        int size = entryList.size();
        int pages = (int) Math.ceil((double) size / (double) entriesPerPage);

        if (size <= entriesPerPage) {
            pagedMap.put(0, entryList);
            return pagedMap;
        }

        for (int i = 1; i <= pages; i++) {
            List<String> pageList = new ArrayList<>();
            for (int j = 0; j < entriesPerPage && j * i < size; j++)
                pageList.add(entryList.get(i * j));
            pagedMap.put(i - 1, pageList);
        }
        return pagedMap;

    }

}
