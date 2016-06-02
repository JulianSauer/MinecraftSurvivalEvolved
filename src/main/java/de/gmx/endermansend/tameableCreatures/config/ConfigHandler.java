package de.gmx.endermansend.tameableCreatures.config;

import de.gmx.endermansend.tameableCreatures.main.TameableCreatures;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class ConfigHandler {

    public GetValuesFromConfig get;

    private TameableCreatures plugin;
    private Logger logger;
    private FileConfiguration config;

    public ConfigHandler() {

        get = new GetValuesFromConfig(this);

        plugin = TameableCreatures.getInstance();
        this.logger = this.plugin.getLogger();

        if (!loadConfig())
            createDefaultConfig();

    }

    /**
     * Saves the config after values have been change. This method is not used and hasn't been tested yet.
     */
    public void saveConfig() {
        logger.info("Saving config");
        plugin.saveConfig();
        logger.info("Config saved");
    }

    /**
     * Tries to convert the value found under the given path to a boolean. If it cannot be found in config.yml, an
     * error message will be printed and a default one will be used.
     *
     * @param path Path to the variable
     * @return Value found in config.yml (default value if none is found)
     */
    protected boolean getBooleanFromConfig(String path) {
        if (!config.isSet(path) || !config.isBoolean(path))
            noValueFound(path);
        return config.getBoolean(path);
    }

    /**
     * Tries to convert the value found under the given path to an int. If it cannot be found in config.yml, an error
     * message will be printed and a default one will be used.
     *
     * @param path Path to the variable
     * @return Value found in config.yml (default value if none is found)
     */
    protected int getIntFromConfig(String path) {
        if (!config.isSet(path) || !config.isInt(path))
            noValueFound(path);
        return config.getInt(path);
    }

    /**
     * Tries to convert the value found under the given path to a double. If it cannot be found in config.yml, an error
     * message will be printed and a default one will be used.
     *
     * @param path Path to the variable
     * @return Value found in config.yml (default value if none is found)
     */
    protected double getDoubleFromConfig(String path) {
        if (!config.isSet(path) || !config.isDouble(path))
            noValueFound(path);
        return config.getDouble(path);
    }

    /**
     * Tries to convert the value found under the given path to a String. If it cannot be found in config.yml, an error
     * message will be printed and a default one will be used.
     *
     * @param path Path to the variable
     * @return Value found in config.yml (default value if none is found)
     */
    protected String getStringFromConfig(String path) {
        if (!config.isSet(path) || !config.isString(path))
            noValueFound(path);
        return config.getString(path);
    }

    /**
     * Tries to convert the values found under the given path to a list of Strings. If it cannot be found in
     * config.yml, an error message will be printed and a default one is be used.
     *
     * @param path Path to the variable
     * @return Value found in config.yml (default value if none is found)
     */
    protected List<String> getStringListFromConfig(String path) {
        List list = config.getStringList(path);
        if (!config.isSet(path) || !config.isList(path))
            noListFound(path);
        else if (list.isEmpty())
            noListFound(path);
        return list;
    }

    /**
     * Tries to convert the values found under the given path to a ConfigurationSection. If it cannot be found in
     * config.yml, an error message will be printed and a default one is used.
     *
     * @param path Path to the variable
     * @return ConfigurationSection found in config.yml (default value if none is found)
     */
    protected ConfigurationSection getConfigurationSectionFromConfig(String path) {
        if (!config.isSet(path) || !config.isConfigurationSection(path))
            noValueFound(path);
        return config.getConfigurationSection(path);
    }

    /**
     * Checks if the given section exists.
     *
     * @param section Name of the section in the config.yml file
     * @return True if the section exists
     */
    protected boolean SectionExists(String section) {
        return config.isSet(section);
    }

    /**
     * Saves the default configuration file and stores it's content to config.
     */
    private void createDefaultConfig() {

        logger.info("Creating default config");
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
        logger.info("Config loaded");

    }

    /**
     * Tries to load the config.yml from disk. This method returns false if no matching file was found. In this case a
     * default config should be loaded.
     *
     * @return True if the config could be loaded
     */
    private boolean loadConfig() {

        logger.info("Loading config");

        if (this.configExists()) {
            config = plugin.getConfig();
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
        logger.warning("Delete config.yml to get a default one");
    }

    /**
     * Prints a warning to the log if a list was not found under the specified path.
     *
     * @param path Path to the missing value
     */
    private void noListFound(String path) {
        logger.warning("Could not find list in config.yml: " + path);
        logger.warning("Delete config.yml to get a default one");
    }

    /**
     * Checks if a config.yml exists in the plugin folder.
     *
     * @return True if a config.yml was found
     */
    private boolean configExists() {

        File[] files = plugin.getDataFolder().listFiles();
        if (files == null)
            return false;

        for (File file : files) {
            if (file.getName().equals("config.yml")) {
                return true;
            }
        }

        return false;

    }

}