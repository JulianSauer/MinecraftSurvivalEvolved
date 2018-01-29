package de.juliansauer.minecraftsurvivalevolved.tribes;

/**
 * Represents the ranks a player can have in a tribe.
 */
public enum Rank {

    FOUNDER,
    LEADER,
    GENERAL,
    OFFICER,
    MEMBER,
    RECRUIT;

    /**
     * Compares two ranks with each other.
     *
     * @param providedRank Rank that is being checked for it's rights in the tribe
     * @param neededRank   Rank that is at least needed for something
     * @return True if the providedRank is higher or equal to the neededRank
     */
    public static boolean rankIsEqualOrHigher(Rank providedRank, Rank neededRank) {
        if (providedRank == neededRank)
            return true;
        return rankIsHigher(providedRank, neededRank);
    }

    /**
     * Checks if the first rank is higher than the second.
     *
     * @param higher Rank that should be higher
     * @param lower  Rank that should be lower
     * @return False if the first rank is not higher
     */
    public static boolean rankIsHigher(TribeMember higher, TribeMember lower) {
        return rankIsHigher(higher.getRank(), lower.getRank());
    }

    /**
     * Checks if the first rank is higher than the second.
     *
     * @param higher Rank that should be higher
     * @param lower  Rank that should be lower
     * @return False if the first rank is not higher
     */
    public static boolean rankIsHigher(Rank higher, Rank lower) {
        if (higher == lower)
            return false;
        for (Rank rank : Rank.values()) {
            if (rank == higher)
                return true;
            if (rank == lower)
                return false;
        }
        return false;
    }

    public static Rank getNextHigher(Rank currentRank) {
        Rank nextHigher = FOUNDER;
        for (Rank rank : Rank.values()) {
            if (rank == currentRank)
                break;
            nextHigher = rank;
        }
        return nextHigher;
    }

    public static Rank getNextLower(Rank currentRank) {
        Rank[] ranks = Rank.values();
        for (int i = 0; i < ranks.length - 1; i++) {
            if (ranks[i] == currentRank)
                return ranks[i + 1];
        }
        return Rank.RECRUIT;
    }

    /**
     * Compares two ranks.
     *
     * @param rank1 First rank
     * @param rank2 Second rank
     * @return The higher rank
     */
    public static Rank getHigher(Rank rank1, Rank rank2) {
        if (rankIsHigher(rank1, rank2))
            return rank1;
        return rank2;
    }

    /**
     * Compares two ranks and returns the lower rank.
     *
     * @param rank1 First rank
     * @param rank2 Second rank
     * @return The lower rank
     */
    public static Rank getLower(Rank rank1, Rank rank2) {
        if (rankIsHigher(rank1, rank2))
            return rank2;
        return rank1;
    }

    @Override
    public String toString() {
        return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }

}
