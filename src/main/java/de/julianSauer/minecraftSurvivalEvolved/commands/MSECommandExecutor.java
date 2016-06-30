package de.julianSauer.minecraftSurvivalEvolved.commands;

import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MSECommandExecutor implements CommandExecutor {

    private TribeRegistry tribeRegistry;
    private TribeMemberRegistry tribeMemberRegistry;

    private CommandHandler handleForceTame;
    private CommandHandler handleTribeCommand;
    private CommandHandler handleTribesCommand;

    public MSECommandExecutor() {
        tribeRegistry = TribeRegistry.getTribeRegistry();
        tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
        handleForceTame = new HandleForceTame();
        handleTribeCommand = new HandleTribeCommand();
        handleTribesCommand = new HandleTribesCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("mse"))
            return false;

        // TODO: Print help

        if (args.length >= 1) {

            if ((args[0].equalsIgnoreCase("forcetame") || args[0].equalsIgnoreCase("ft"))) {
                handleForceTame.process(sender, args);

            } else if (args[0].equalsIgnoreCase("tribe")) {
                handleTribeCommand.process(sender, args);

            } else if (args[0].equalsIgnoreCase("tribes")) {
                handleTribesCommand.process(sender, args);
            }
        }

        return true;

    }

}
