package de.juliansauer.minecraftsurvivalevolved.messages;

public interface Messages {

    /**
     * Replaces arguments in the message with provided parameters.
     *
     * @param args Parameters for the message
     */
    default String setParams(String... args) {
        String ret = getMessage();
        for (int i = 0; i < args.length; i++)
            ret = ret.replace("%ARGS" + i + "%", args[i]);
        return ret;
    }

    /**
     * Saves parameters to use them later.
     *
     * @param args Parameters for the message
     */
    Messages presetParams(String... args);

    String getMessage();

}
