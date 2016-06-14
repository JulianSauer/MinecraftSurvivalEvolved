package de.julianSauer.minecraftSurvivalEvolved.config;

import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages config files.
 */
public abstract class ConfigHandlerBase {

    private Logger logger;

    private Map<String, CustomConfig> configFiles;
    private Map<String, FileConfiguration> configs;

    public ConfigHandlerBase(String[] configNames) {
        configFiles = new HashMap<>();
        configs = new HashMap<>();
        for (String configName : configNames) {
            CustomConfig configFile = new CustomConfig(configName);
            configFiles.put(configName, configFile);
            configs.put(configName, configFile.getConfig());
        }

        this.logger = ThisPlugin.getInstance().getLogger();
        createDefaultConfigs();
    }

    /**
     * Tries to convert the value found under the given path to a Boolean.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected Boolean getBooleanFromConfig(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isBoolean(path))
            return customConfig.getBoolean(path);
        return null;
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
    protected boolean getBooleanFromConfig(String configName, String parentConfig, String path) {
        Boolean ret = getBooleanFromConfig(configName, path);
        if (ret != null)
            return ret;
        ret = getBooleanFromConfig(parentConfig, parentConfig);
        if (ret != null)
            return ret;
        return false;
    }

    /**
     * Tries to convert the value found under the given path to an Integer.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected Integer getIntFromConfig(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isInt(path))
            return customConfig.getInt(path);
        return null;
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
    protected int getIntFromConfig(String configName, String parentConfig, String path) {
        Integer ret = getIntFromConfig(configName, path);
        if (ret != null)
            return ret;
        ret = getIntFromConfig(parentConfig, path);
        if (ret != null)
            return ret;
        return 0;
    }

    /**
     * Tries to convert the value found under the given path to a Double.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected Double getDoubleFromConfig(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isDouble(path))
            return customConfig.getDouble(path);
        return null;
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
    protected double getDoubleFromConfig(String configName, String parentConfig, String path) {
        Double ret = getDoubleFromConfig(configName, path);
        if (ret != null)
            return ret;
        ret = getDoubleFromConfig(parentConfig, path);
        if (ret != null)
            return ret;
        return 0;
    }

    /**
     * Tries to convert the value found under the given path to a String.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected String getStringFromConfig(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isString(path))
            return customConfig.getString(path);
        return null;
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
        String ret = getStringFromConfig(configName, path);
        if (ret != null)
            return ret;
        ret = getStringFromConfig(parentConfig, path);
        if (ret != null)
            return ret;
        return "";
    }

    /**
     * Tries to convert the values found under the given path to a list of Strings.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected List<String> getStringListFromConfig(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isList(path)) {
            List list = customConfig.getStringList(path);
            if (!list.isEmpty())
                return list;
        }
        return new ArrayList<>();
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
        List<String> ret = getStringListFromConfig(configName, path);
        if (!ret.isEmpty())
            return ret;
        ret = getStringListFromConfig(parentConfig, path);
        return ret;
    }

    /**
     * Tries to convert the values found under the given path to a ConfigurationSection.
     *
     * @param configName Name of the .yml file
     * @param path       Path to the variable
     * @return Found value
     */
    protected ConfigurationSection getConfigurationSectionFromConfig(String configName, String path) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null && customConfig.isSet(path) && customConfig.isConfigurationSection(path))
            return customConfig.getConfigurationSection(path);
        return null;
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
    protected ConfigurationSection getConfigurationSectionFromConfig(String configName, String parentConfig, String path) {
        ConfigurationSection ret = getConfigurationSectionFromConfig(configName, path);
        if (ret != null)
            return ret;
        ret = getConfigurationSectionFromConfig(parentConfig, path);
        if (ret != null)
            return ret;
        return null;
    }

    /**
     * Tries to convert all values found under the Configuration to a map.
     *
     * @param configName Name of the .yml file
     * @return Map of all values found in the configuration
     */
    protected Map getAllValuesFromConfig(String configName) {
        FileConfiguration customConfig = configs.get(configName);
        if (customConfig != null)
            return customConfig.getValues(true);
        return new HashMap<>();
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
        File[] files = ThisPlugin.getInstance().getDataFolder().listFiles();
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