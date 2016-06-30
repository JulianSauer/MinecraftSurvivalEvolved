package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeRegistry;
import org.bukkit.command.CommandSender;

/**
 * Represents an object that can process a command.
 */
public abstract class CommandHandler {

    TribeRegistry tribeRegistry;

    CommandHandler() {
        tribeRegistry = TribeRegistry.getTribeRegistry();
    }

    abstract void process(CommandSender sender, String... args);

}
