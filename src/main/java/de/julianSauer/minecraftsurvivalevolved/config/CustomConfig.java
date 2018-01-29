package de.juliansauer.minecraftsurvivalevolved.config;

import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

/**
 * Used for custom config files.
 */
class CustomConfig {

    private final String fileName;

    private File file;

    private FileConfiguration fileConfiguration;

    public CustomConfig(String folderName, String fileName) {
        this.fileName = fileName;
        File dataFolder = MSEMain.getInstance().getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();
        file = new File(dataFolder + folderName, fileName);
    }

    public void reloadConfig() {

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        InputStream defaultsStream = MSEMain.getInstance().getResource(fileName);
        if (defaultsStream != null) {
            Reader defaultsStreamReader = new InputStreamReader(defaultsStream);
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultsStreamReader);
            fileConfiguration.setDefaults(defaultConfig);
            try {
                defaultsStreamReader.close();
            } catch (IOException e) {
                MSEMain.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
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
            MSEMain.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
        }
    }

    public void saveDefaultConfig() {
        if (!file.exists())
            MSEMain.getInstance().saveResource(fileName, false);
    }

    void deleteFile() {
        if (!file.delete())
            MSEMain.getInstance().getLogger().warning("Could not delete config: " + fileName);
    }

}
