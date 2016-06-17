package de.julianSauer.minecraftSurvivalEvolved.main;

import de.julianSauer.minecraftSurvivalEvolved.config.ConfigHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.EntityRegistry;
import de.julianSauer.minecraftSurvivalEvolved.items.CustomRecipes;
import de.julianSauer.minecraftSurvivalEvolved.listeners.*;
import de.julianSauer.minecraftSurvivalEvolved.listeners.packets.PacketInjector;
import de.julianSauer.minecraftSurvivalEvolved.visuals.HologramHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ThisPlugin extends JavaPlugin {

    private static ThisPlugin instance;

    private static ConfigHandler configHandler;

    private static PacketInjector packetInjector;

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
        pluginManager.registerEvents(new EntityDeathListener(), this);
        pluginManager.registerEvents(new EntitySpawnListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);
        pluginManager.registerEvents(new InventoryDragListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PrepareItemCraftListener(), this);

        instance = this;

        packetInjector = new PacketInjector();

        getLogger().info("Enabled");

    }

    @Override
    public void onDisable() {
        HologramHandler.despawnAllHolograms();
        getLogger().info("Disabled");
    }

    public static ThisPlugin getInstance() {
        return instance;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public static PacketInjector getPacketInjector() {
        return packetInjector;
    }

}
