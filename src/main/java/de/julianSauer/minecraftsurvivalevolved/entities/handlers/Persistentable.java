package de.juliansauer.minecraftsurvivalevolved.entities.handlers;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

/**
 * Defines methods for loading and saving NBTTags.
 */
public interface Persistentable {

    /**
     * Checks if the NBTTags were already loaded.
     *
     * @return True if the values are loaded
     */
    boolean isInitialized();

    void initWithDefaults();

    void initWith(NBTTagCompound data);

    void saveData(NBTTagCompound data);

}
