package de.gmx.endermansend.tameableCreatures.main;

import org.bukkit.plugin.java.JavaPlugin;

public class TameableCreatures extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

}
