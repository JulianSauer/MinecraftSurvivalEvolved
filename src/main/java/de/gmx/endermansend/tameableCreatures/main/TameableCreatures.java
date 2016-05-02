package de.gmx.endermansend.tameableCreatures.main;

import de.gmx.endermansend.tameableCreatures.entities.CustomEntities;
import de.gmx.endermansend.tameableCreatures.items.CustomRecipes;
import de.gmx.endermansend.tameableCreatures.listeners.BowShootListener;
import de.gmx.endermansend.tameableCreatures.listeners.EntityDamageByEntityListener;
import de.gmx.endermansend.tameableCreatures.listeners.PlayerInteractListener;
import de.gmx.endermansend.tameableCreatures.listeners.PrepareItemCraftListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TameableCreatures extends JavaPlugin {

    static TameableCreatures instance;

    @Override
    public void onEnable() {

        CustomEntities.TameableEntityType.registerCustomEntities();

        CustomRecipes customRecipes = new CustomRecipes();
        customRecipes.setUpNarcotics();
        customRecipes.setUpTranquilizerArrow();

        PluginManager pluginManager = getServer().getPluginManager();
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
