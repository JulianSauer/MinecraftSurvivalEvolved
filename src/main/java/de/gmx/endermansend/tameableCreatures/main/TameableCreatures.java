package de.gmx.endermansend.tameableCreatures.main;

import de.gmx.endermansend.tameableCreatures.entities.CustomEntities;
import de.gmx.endermansend.tameableCreatures.listeners.PlayerInteractListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TameableCreatures extends JavaPlugin {

    @Override
    public void onEnable() {

        CustomEntities.TameableEntityType.registerCustomEntities();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(), this);

        getLogger().info("Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

}
