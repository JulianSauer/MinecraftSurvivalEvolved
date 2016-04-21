package de.gmx.endermansend.tameableCreatures.main;

import de.gmx.endermansend.tameableCreatures.entities.CustomEntities;
import de.gmx.endermansend.tameableCreatures.items.CustomRecipes;
import de.gmx.endermansend.tameableCreatures.listeners.CraftItemListener;
import de.gmx.endermansend.tameableCreatures.listeners.PlayerInteractListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TameableCreatures extends JavaPlugin {

    @Override
    public void onEnable() {

        CustomEntities.TameableEntityType.registerCustomEntities();

        CustomRecipes customRecipes = new CustomRecipes();
        customRecipes.setUpNarcotics();
        customRecipes.setUpTranquilizerArrow();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new CraftItemListener(), this);

        getLogger().info("Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

}
