package de.julianSauer.minecraftSurvivalEvolved.config;

import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigHandler {

    public GetValuesFromConfig get;

    // File name, config file
    private static Map<String, FileConfiguration> cache;

    private CustomConfig defaultEntityFile;
    private CustomConfig foodFile;

    private Logger logger;
    private FileConfiguration defaultEntity;

    public ConfigHandler() {

        cache = new HashMap<String, FileConfiguration>();
        defaultEntityFile = new CustomConfig("DefaultEntity.yml");
        foodFile = new CustomConfig("Food.yml");
        cache.put("Food.yml", foodFile.getConfig());

        get = new GetValuesFromConfig(this);

        this.logger = ThisPlugin.getInstance().getLogger();

        if (!loadConfig())
            createDefaultConfig();

    }

    /**
     * Tries to convert the value found under the given path to a boolean. If it cannot be found in EntityName.yml or
     * DefaultEntity.yml, an error message will be printed and a default one will be used.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Value found in EntityName.yml or DefaultEntity.yml (default value if none is found)
     */
    protected boolean getBooleanFromConfig(String configName, String path) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isBoolean(path))
            return customConfig.getBoolean(path);

        if (!defaultEntity.isSet(path) || !defaultEntity.isBoolean(path))
            noValueFound(path);
        return defaultEntity.getBoolean(path);
    }

    /**
     * Tries to convert the value found under the given path to an int. If it cannot be found in EntityName.yml or
     * DefaultEntity.yml, an error message will be printed and a default one will be used.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Value found in EntityName.yml or DefaultEntity.yml (default value if none is found)
     */
    protected int getIntFromConfig(String configName, String path) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isInt(path))
            return customConfig.getInt(path);

        if (!defaultEntity.isSet(path) || !defaultEntity.isInt(path))
            noValueFound(path);
        return defaultEntity.getInt(path);
    }

    /**
     * Tries to convert the value found under the given path to a double. If it cannot be found in EntityName.yml or
     * DefaultEntity.yml, an error message will be printed and a default one will be used.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Value found in EntityName.yml or DefaultEntity.yml (default value if none is found)
     */
    protected double getDoubleFromConfig(String configName, String path) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isDouble(path))
            return customConfig.getDouble(path);

        if (!defaultEntity.isSet(path) || !defaultEntity.isDouble(path))
            noValueFound(path);
        return defaultEntity.getDouble(path);
    }

    /**
     * Tries to convert the value found under the given path to a String. If it cannot be found in EntityName.yml or
     * DefaultEntity.yml, an error message will be printed and a default one will be used.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Value found in EntityName.yml or DefaultEntity.yml (default value if none is found)
     */
    protected String getStringFromConfig(String configName, String path) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isString(path))
            return customConfig.getString(path);

        if (!defaultEntity.isSet(path) || !defaultEntity.isString(path))
            noValueFound(path);
        return defaultEntity.getString(path);
    }

    /**
     * Tries to convert the values found under the given path to a list of Strings. If it cannot be found in
     * EntityName.yml or DefaultEntity.yml, an error message will be printed and a default one is be used.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Value found in EntityName.yml or DefaultEntity.yml (default value if none is found)
     */
    protected List<String> getStringListFromConfig(String configName, String path) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isList(path)) {
            List list = customConfig.getStringList(path);
            if (!list.isEmpty())
                return list;
        }

        List list = defaultEntity.getStringList(path);
        if (!defaultEntity.isSet(path) || !defaultEntity.isList(path))
            noListFound(path);
        return list;
    }

    /**
     * Tries to convert the values found under the given path to a ConfigurationSection. If it cannot be found in
     * EntityName.yml or DefaultEntity.yml, an error message will be printed and a default one is used.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return ConfigurationSection found in EntityName.yml or DefaultEntity.yml (default value if none is found)
     */
    protected ConfigurationSection getConfigurationSectionFromConfig(String configName, String path) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isConfigurationSection(path))
            return customConfig.getConfigurationSection(path);

        if (!defaultEntity.isSet(path) || !defaultEntity.isConfigurationSection(path))
            noValueFound(path);
        return defaultEntity.getConfigurationSection(path);
    }

    /**
     * Tries to convert all values found under the Configuration to a map. If it cannot be found in the .yml file, an
     * error message will be printed and a default one is used.
     *
     * @param configName Name of the .yml file
     * @return Map of all values found in the configuration
     */
    protected Map getAllValuesFromConfig(String configName) {
        FileConfiguration customConfig = getConfigFor(configName);
        if (customConfig != null)
            return customConfig.getValues(true);
        noValueFound(configName + ".yml");
        return new HashMap<>();
    }

    /**
     * Returns the specified .yml file from cache or disk.
     *
     * @param configName Name of the .yml file
     * @return Config file or null if it doesn't exist
     */
    private FileConfiguration getConfigFor(String configName) {
        if (cache.containsKey(configName)) {
            return cache.get(configName);
        } else {
            FileConfiguration customConfig = new CustomConfig(configName + ".yml").getConfig();
            cache.put(configName, customConfig);
            return customConfig;
        }
    }

    /**
     * Saves the default configuration file and stores it's content to config.
     */
    private void createDefaultConfig() {
        logger.info("Initializing config files");
        defaultEntityFile.saveDefaultConfig();
        foodFile.saveDefaultConfig();
        defaultEntity = defaultEntityFile.getConfig();

        String[] defaultMobs = {"CaveSpider", "Giant", "Spider", "Squid", "Wolf"};
        CustomConfig tempConfig;
        for (String entity : defaultMobs) {
            if (this.configExistsFor(entity))
                continue;

            tempConfig = new CustomConfig(entity + ".yml");
            tempConfig.saveDefaultConfig();
            cache.put(entity, tempConfig.getConfig());
        }

        logger.info("Config loaded");
    }

    /**
     * Tries to load the DefaultEntity.yml from disk. This method returns false if no matching file was found. In this
     * case a default config should be loaded.
     *
     * @return True if the config could be loaded
     */
    private boolean loadConfig() {

        logger.info("Loading config");

        if (this.configExistsFor("DefaultEntity")) {
            defaultEntity = defaultEntityFile.getConfig();
            logger.info("Config loaded");
            return true;
        }

        return false;

    }

    /**
     * Prints a warning to the log if a value was not found under the specified path.
     *
     * @param path Path to the missing value
     */
    private void noValueFound(String path) {
        logger.warning("Value is missing or of wrong type: " + path);
        logger.warning("Using default value");
        logger.warning("Delete DefaultEntity.yml to get a default one");
    }

    /**
     * Prints a warning to the log if a list was not found under the specified path.
     *
     * @param path Path to the missing value
     */
    private void noListFound(String path) {
        logger.warning("Could not find list in DefaultEntity.yml: " + path);
        logger.warning("Delete DefaultEntity.yml to get a default one");
    }

    /**
     * Checks if a .yml file exists in the plugin folder.
     *
     * @param entity Name of the .yml file
     * @return True if a DefaultEntity.yml was found
     */
    private boolean configExistsFor(String entity) {

        File[] files = ThisPlugin.getInstance().getDataFolder().listFiles();
        if (files == null)
            return false;

        for (File file : files) {
            if (file.getName().equals(entity + ".yml")) {
                return true;
            }
        }

        return false;

    }

}