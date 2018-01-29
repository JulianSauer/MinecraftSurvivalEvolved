package de.juliansauer.minecraftsurvivalevolved.tribes;

public enum RankPermission {

    CHANGING_RANKS,
    RECRUITING,
    DISCHARGING,
    PROMOTING;

    @Override
    public String toString() {
        return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }

}
