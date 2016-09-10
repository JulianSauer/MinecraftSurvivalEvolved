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

        if(args.length > 2)
            sender.sendMessage(ChatMessages.WRONG_NUMBER_OF_ARGS.toString());

        Collection<Tribe> tribes = tribeRegistry.getTribes();
        if(tribes.size() == 0) {
            sender.sendMessage(ChatMessages.TRIBE_NONE_EXIST.toString());
            return;
        }

        int page = 1;

        if (args.length == 2)
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatMessages.NOT_A_NUMBER.setParams("second"));
                return;
            }

        int pageCount = (int) Math.ceil((double) tribes.size() / 10.0);
        if (page > pageCount) {
            sender.sendMessage(ChatMessages.PAGE_DOESNT_EXIST.setParams("" + page));
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
     * @param entryList
     * @param entriesPerPage
     * @return
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
