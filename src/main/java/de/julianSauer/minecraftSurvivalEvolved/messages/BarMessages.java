package de.julianSauer.minecraftSurvivalEvolved.messages;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public enum BarMessages implements Messages {

    TAME_DIED("Your %ARGS0% died!", BarColor.RED, BarStyle.SOLID),
    TAMED_SUCCESSFULLY("You have tamed a %ARGS0%!", BarColor.GREEN, BarStyle.SOLID),
    TRIBE_MEMBER_TAMED_SUCCESSFULLY("%ARGS0% has tamed a %ARGS1%!", BarColor.GREEN, BarStyle.SOLID);

    private final String message;
    private final BarColor color;
    private final BarStyle style;

    private String[] params;

    BarMessages(String message, BarColor color, BarStyle style) {
        this.message = message;
        this.color = color;
        this.style = style;
    }

    public String getMessage() {
        return message;
    }

    public BarColor getColor() {
        return color;
    }

    /**
     * Translates BarColors into ChatColors.
     *
     * @return Closest ChatColor I could find
     */
    public ChatColor getChatColor() {
        switch (color) {
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            case BLUE:
                return ChatColor.BLUE;
            case RED:
                return ChatColor.RED;
            case GREEN:
                return ChatColor.GREEN;
            case YELLOW:
                return ChatColor.YELLOW;
            case PURPLE:
                return ChatColor.DARK_PURPLE;
            case WHITE:
                return ChatColor.WHITE;
            default:
                return ChatColor.WHITE;
        }
    }

    public BarStyle getStyle() {
        return style;
    }

    @Override
    public String toString() {
        if (params == null)
            params = new String[]{""};
        return Messages.super.setParams(params);
    }

    public BarMessages presetParams(String... args) {
        params = args;
        return this;
    }

}
