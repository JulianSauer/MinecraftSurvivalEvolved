package de.juliansauer.minecraftsurvivalevolved.config;

import de.juliansauer.minecraftsurvivalevolved.main.MSEMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages config files.
 */
abstract class ConfigHandlerBase {

    private final Logger logger;

    private Map<String, CustomConfig> configFiles;
    private Map<String, FileConfiguration> configs;

    ConfigHandlerBase(String[] configNames) {
        configFiles = new HashMap<>();
        configs = new HashMap<>();
        for (String configName : configNames) {
            CustomConfig configFile = new CustomConfig("", configName);
            configFiles.put(configName, configFile);
            configs.put(configName, configFile.getConfig());
        }

        this.logger = MSEMain.getInstance().getLogger();
        createDefaultConfigs();
    }

    /**
     * Tries to convert the value found under the given path to a boolean.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected boolean getBooleanFromConfig(String configName, String path) {
        Boolean ret = getBoolean(configName, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, path);
        return configs.get(configName).getBoolean(path);
    }

    /**
     * Tries to convert the value found under the given path to a boolean. The declared parent file will be used if the
     * value was not found in the normal config.
     *
     * @param configName   Name of the .yml file
     * @param parentConfig Name of the parent.yml file
     * @param path         Path to the variable
     * @return Found value
     */
    boolean getBooleanFromConfig(String configName, String parentConfig, String path) {
        Boolean ret = getBoolean(configName, path);
        if (ret != null)
            return ret;
        ret = getBoolean(parentConfig, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, parentConfig, path);
        return configs.get(parentConfig).getBoolean(path);
    }

    /**
     * Tries to convert the value found under the given path to an int.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected int getIntFromConfig(String configName, String path) {
        Integer ret = getInt(configName, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, path);
        return configs.get(configName).getInt(path);
    }

    /**
     * Tries to convert the value found under the given path to an int. The declared parent file will be used if the
     * value was not found in the normal config.
     *
     * @param configName   Name of the .yml file
     * @param parentConfig Name of the parent.yml file
     * @param path         Path to the variable
     * @return Found value
     */
    int getIntFromConfig(String configName, String parentConfig, String path) {
        Integer ret = getInt(configName, path);
        if (ret != null)
            return ret;
        ret = getInt(parentConfig, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, parentConfig, path);
        return configs.get(parentConfig).getInt(path);
    }

    /**
     * Tries to convert the value found under the given path to a double.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected double getDoubleFromConfig(String configName, String path) {
        Double ret = getDouble(configName, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, path);
        return configs.get(configName).getDouble(path);
    }

    /**
     * Tries to convert the value found under the given path to a double. The declared parent file will be used if the
     * value was not found in the normal config.
     *
     * @param configName   Name of the .yml file
     * @param parentConfig Name of the parent.yml file
     * @param path         Path to the variable
     * @return Found value
     */
    double getDoubleFromConfig(String configName, String parentConfig, String path) {
        Double ret = getDouble(configName, path);
        if (ret != null)
            return ret;
        ret = getDouble(parentConfig, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, parentConfig, path);
        return configs.get(parentConfig).getDouble(path);
    }

    /**
     * Tries to convert the value found under the given path to a String.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    String getStringFromConfig(String configName, String path) {
        String ret = getString(configName, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, path);
        return configs.get(configName).getString(path);
    }

    /**
     * Tries to convert the value found under the given path to a String. The declared parent file will be used if the
     * value was not found in the normal config.
     *
     * @param configName   Name of the .yml file
     * @param parentConfig Name of the parent.yml file
     * @param path         Path to the variable
     * @return Found value
     */
    protected String getStringFromConfig(String configName, String parentConfig, String path) {
        String ret = getString(configName, path);
        if (ret != null)
            return ret;
        ret = getString(parentConfig, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, parentConfig, path);
        return configs.get(parentConfig).getString(path);
    }

    /**
     * Tries to convert the value found under the given path to a list of Strings.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    List<String> getStringListFromConfig(String configName, String path) {
        List<String> ret = getStringList(configName, path);
        if (ret != null && !ret.isEmpty())
            return ret;
        noValueFoundFor(configName, path);
        return configs.get(configName).getStringList(path);
    }

    /**
     * Tries to convert the value found under the given path to a list of Strings. The declared parent file will be
     * used if the value was not found in the normal config.
     *
     * @param configName   Name of the .yml file
     * @param parentConfig Name of the parent.yml file
     * @param path         Path to the variable
     * @return Found value
     */
    protected List<String> getStringListFromConfig(String configName, String parentConfig, String path) {
        List<String> ret = getStringList(configName, path);
        if (ret != null && !ret.isEmpty())
            return ret;
        ret = getStringList(parentConfig, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, parentConfig, path);
        return configs.get(parentConfig).getStringList(path);
    }

    /**
     * Tries to convert the value found under the given path to a ConfigurationSection.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    ConfigurationSection getConfigurationSectionFromConfig(String configName, String path) {
        ConfigurationSection ret = getConfigurationSection(configName, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, path);
        return configs.get(configName).getConfigurationSection(path);
    }

    /**
     * Tries to convert the value found under the given path to a ConfigurationSection. The declared parent file will
     * be used if the value was not found in the normal config.
     *
     * @param configName   Name of the .yml file
     * @param parentConfig Name of the parent.yml file
     * @param path         Path to the variable
     * @return Found value
     */
    ConfigurationSection getConfigurationSectionFromConfig(String configName, String parentConfig, String path) {
        ConfigurationSection ret = getConfigurationSection(configName, path);
        if (ret != null)
            return ret;
        ret = getConfigurationSection(parentConfig, path);
        if (ret != null)
            return ret;
        noValueFoundFor(configName, parentConfig, path);
        return configs.get(parentConfig).getConfigurationSection(path);
    }

    /**
     * Tries to convert all values found under the Configuration to a map.
     *
     * @param configName Name of the .yml file
     * @return Map of all values found in the configuration
     */
    Map getAllValuesFromConfig(String configName) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null)
            return customConfig.getValues(true);
        return new HashMap<>();
    }

    /**
     * Saves a value to a config. The config has to be added first.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @param value      Value that will be set
     */
    void setValueInConfig(String configName, String path, Object value) {
        FileConfiguration customConfig = configs.get(configName);
        customConfig.set(path, value);
        configFiles.get(configName).saveConfig();
    }

    /**
     * Loads a config to the cache.
     *
     * @param folderName Name of the subdirectory; can be empty
     * @param configName Name of the .yml file
     */
    void addConfigToCache(String folderName, String configName) {
        if (configFiles.containsKey(configName) && configs.containsKey(configName))
            return;
        CustomConfig configFile = new CustomConfig(folderName, configName);
        configFiles.put(configName, configFile);
        configs.put(configName, configFile.getConfig());
        if (!configExistsFor(configName))
            configFile.saveConfig();
    }

    /**
     * Unloads a config from the cache.
     *
     * @param configName Name of the .yml file
     */
    void removeConfigFromCache(String configName) {
        if (configFiles.containsKey(configName))
            configFiles.remove(configName);
        if (configs.containsKey(configName))
            configs.remove(configName);
    }

    /**
     * Finds a file within the plugin folder by name and deletes it. Cannot be undone.
     *
     * @param folderName Path to the file within the plugin folder
     * @param fileName   Name of the file
     */
    void deleteFile(String folderName, String fileName) {
        addConfigToCache(folderName, fileName);
        if (configFiles.containsKey(fileName)) {
            configFiles.get(fileName).deleteFile();
        }
        removeConfigFromCache(fileName);
    }

    /**
     * Prints a warning to the log if a value was not found under the specified path.
     *
     * @param configName Name of the config
     */
    void noValueFoundFor(String configName) {
        logger.warning("Value is missing or of wrong type in " + configName);
        logger.warning("Using default value");
        logger.warning("Delete " + configName + " to get a default one");
    }

    /**
     * Prints a warning to the log if a value was not found under the specified path.
     *
     * @param configName Name of the config
     * @param path       Path to the missing value
     */
    private void noValueFoundFor(String configName, String path) {
        logger.warning("Value is missing or of wrong type " + path + " in " + configName);
        logger.warning("Attempting to use a default value...");
    }

    /**
     * Prints a warning to the log if a value was not found under the specified path.
     *
     * @param configName   Name of the config
     * @param parentConfig Name of the parent config
     * @param path         Path to the missing value
     */
    void noValueFoundFor(String configName, String parentConfig, String path) {
        logger.warning("Value is missing or of wrong type: " + path + " in " + configName + " or " + parentConfig);
        logger.warning("Using default value");
        logger.warning("Delete " + configName + " and " + parentConfig + " to get default ones");
    }

    /**
     * Tries to convert the value found under the given path to a Boolean.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    private Boolean getBoolean(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isBoolean(path))
            return customConfig.getBoolean(path);
        return null;
    }

    /**
     * Tries to convert the value found under the given path to an Integer.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    private Integer getInt(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isInt(path))
            return customConfig.getInt(path);
        return null;
    }

    /**
     * Tries to convert the value found under the given path to a Double.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    private Double getDouble(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isDouble(path))
            return customConfig.getDouble(path);
        return null;
    }

    /**
     * Tries to convert the value found under the given path to a String.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    private String getString(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isString(path))
            return customConfig.getString(path);
        return null;
    }

    /**
     * Tries to convert the values found under the given path to a list of Strings.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    private List<String> getStringList(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isList(path)) {
            List list = customConfig.getStringList(path);
            if (!list.isEmpty())
                return list;
        }
        return null;
    }

    /**
     * Tries to convert the values found under the given path to a ConfigurationSection.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    private ConfigurationSection getConfigurationSection(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isConfigurationSection(path))
            return customConfig.getConfigurationSection(path);
        return null;
    }

    /**
     * Creates config files from all values found in configFiles.
     */
    private void createDefaultConfigs() {
        logger.info("Initializing config files");
        configFiles.entrySet().stream().filter(entry -> !configExistsFor(entry.getKey())).forEach(entry -> {
            entry.getValue().saveDefaultConfig();
            entry.getValue().reloadConfig();
            configs.put(entry.getKey(), entry.getValue().getConfig());
        });
        logger.info("Configs loaded");
    }

    /**
     * Checks if a .yml file exists in the plugin folder.
     *
     * @param configName Name of the .yml file
     * @return True if a .yml file was found
     */
    private boolean configExistsFor(String configName) {
        File[] files = MSEMain.getInstance().getDataFolder().listFiles();
        if (files == null)
            return false;

        for (File file : files) {
            if (file.getName().equals(configName)) {
                return true;
            }
        }
        return false;
    }

}