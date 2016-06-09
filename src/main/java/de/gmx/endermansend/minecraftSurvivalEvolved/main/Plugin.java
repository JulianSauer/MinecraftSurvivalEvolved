package de.gmx.endermansend.minecraftSurvivalEvolved.main;

import de.gmx.endermansend.minecraftSurvivalEvolved.config.ConfigHandler;
import de.gmx.endermansend.minecraftSurvivalEvolved.entities.EntityRegistry;
import de.gmx.endermansend.minecraftSurvivalEvolved.holograms.HologramHandler;
import de.gmx.endermansend.minecraftSurvivalEvolved.items.CustomRecipes;
import de.gmx.endermansend.minecraftSurvivalEvolved.listeners.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Plugin extends JavaPlugin {

    private static Plugin instance;

    private static ConfigHandler configHandler;

    private static Random random;

    @Override
    public void onEnable() {

        instance = this;

        configHandler = new ConfigHandler();

        EntityRegistry.registerCustomEntities();

        CustomRecipes customRecipes = new CustomRecipes();
        customRecipes.setUpNarcotics();
        customRecipes.setUpTranquilizerArrow();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockDamageListener(), this);
        pluginManager.registerEvents(new BowShootListener(), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);
        pluginManager.registerEvents(new EntitySpawnListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PrepareItemCraftListener(), this);

        instance = this;
        random = new Random();
        getLogger().info("Enabled");

    }

    @Override
    public void onDisable() {
        HologramHandler.despawnAllHolograms();
        getLogger().info("Disabled");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public static int getRandomInt(int upperLimit) {
        return random.nextInt(upperLimit);
    }

}
