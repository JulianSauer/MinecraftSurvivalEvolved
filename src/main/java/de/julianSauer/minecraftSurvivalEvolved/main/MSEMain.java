package de.julianSauer.minecraftSurvivalEvolved.main;

import de.julianSauer.minecraftSurvivalEvolved.config.ConfigHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.EntityRegistry;
import de.julianSauer.minecraftSurvivalEvolved.items.CustomRecipes;
import de.julianSauer.minecraftSurvivalEvolved.listeners.*;
import de.julianSauer.minecraftSurvivalEvolved.listeners.packets.InBlockDigListener;
import de.julianSauer.minecraftSurvivalEvolved.listeners.packets.InUpdateSignListener;
import de.julianSauer.minecraftSurvivalEvolved.listeners.packets.PacketEventManager;
import de.julianSauer.minecraftSurvivalEvolved.listeners.packets.PacketInjector;
import de.julianSauer.minecraftSurvivalEvolved.visuals.HologramHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MSEMain extends JavaPlugin {

    private static MSEMain instance;

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

        getCommand("mse").setExecutor(new MSECommandExecutor());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockDamageListener(), this);
        pluginManager.registerEvents(new BowShootListener(), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);
        pluginManager.registerEvents(new EntityDeathListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);
        pluginManager.registerEvents(new InventoryDragListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PrepareItemCraftListener(), this);

        packetInjector = new PacketInjector();
        PacketEventManager.registerPacketListener(new InBlockDigListener());
        PacketEventManager.registerPacketListener(new InUpdateSignListener());

        instance = this;

        getLogger().info("Enabled");

    }

    @Override
    public void onDisable() {
        HologramHandler.despawnAllHolograms();
        getLogger().info("Disabled");
    }

    public static MSEMain getInstance() {
        return instance;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public static PacketInjector getPacketInjector() {
        return packetInjector;
    }

}
