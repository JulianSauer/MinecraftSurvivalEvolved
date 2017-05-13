package de.julianSauer.minecraftSurvivalEvolved.tribes;

import org.bukkit.entity.Player;

import java.util.UUID;

public class TribeMember {

    private Player player;

    private Tribe tribe;

    public TribeMember(Player player, Tribe tribe) {
        this.player = player;
        this.tribe = tribe;
    }

    public boolean hasTribe() {
        return tribe != null;
    }

    public boolean canRecruit() {
        return Rank.rankIsEqualOrHigher(tribe.getRankOfMember(player), tribe.getRankForRecruitment());
    }

    public boolean canDischarge() {
        return Rank.rankIsEqualOrHigher(tribe.getRankOfMember(player), tribe.getRankForDischarge());
    }

    public boolean canPromote() {
        return Rank.rankIsEqualOrHigher(tribe.getRankOfMember(player), tribe.getRankForPromoting());
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public Rank getRank() {
        return tribe.getRankOfMember(player);
    }

    public void setRank(Rank newRank) {
        tribe.setRankOf(player, newRank);
    }

}
