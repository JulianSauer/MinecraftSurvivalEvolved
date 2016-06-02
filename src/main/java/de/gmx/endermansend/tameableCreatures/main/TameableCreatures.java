package de.gmx.endermansend.tameableCreatures.main;

import de.gmx.endermansend.tameableCreatures.entities.EntityRegistry;
import de.gmx.endermansend.tameableCreatures.items.CustomRecipes;
import de.gmx.endermansend.tameableCreatures.listeners.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TameableCreatures extends JavaPlugin {

    static TameableCreatures instance;

    @Override
    public void onEnable() {

        EntityRegistry.TameableEntityType.registerCustomEntities();

        CustomRecipes customRecipes = new CustomRecipes();
        customRecipes.setUpNarcotics();
        customRecipes.setUpTranquilizerArrow();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockDamageListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PrepareItemCraftListener(), this);
        pluginManager.registerEvents(new BowShootListener(), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);


        instance = this;
        getLogger().info("Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

    public static TameableCreatures getInstance() {
        return instance;
    }

}
