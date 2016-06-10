package de.julianSauer.minecraftSurvivalEvolved.config;

import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

/**
 * Used for custom config files.
 */
public class CustomConfig {

    private String fileName;

    private File file;

    private FileConfiguration fileConfiguration;

    public CustomConfig(String fileName) {
        this.fileName = fileName;
        File dataFolder = ThisPlugin.getInstance().getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();
        file = new File(dataFolder, fileName);
    }

    public void reloadConfig() {

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        InputStream defaultsStream = ThisPlugin.getInstance().getResource(fileName);
        if (defaultsStream != null) {
            Reader defaultsStreamReader = new InputStreamReader(defaultsStream);
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultsStreamReader);
            fileConfiguration.setDefaults(defaultConfig);
            try {
                defaultsStreamReader.close();
            } catch (IOException e) {
                ThisPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
            }
        }

    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || file == null)
            return;
        try {
            getConfig().save(file);
        } catch (IOException e) {
            ThisPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
        }
    }

    public void saveDefaultConfig() {
        if (!file.exists())
            ThisPlugin.getInstance().saveResource(fileName, false);
    }

}
