package de.julianSauer.minecraftSurvivalEvolved.tribes;

import de.julianSauer.minecraftSurvivalEvolved.main.MSECommandExecutor;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.MathHelper;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

/**
 * Represents a tribe of players with different ranks.
 */
public class Tribe {

    private final TribeRegistry tribeRegistry;

    private final UUID uniqueID;

    private Map<UUID, Rank> members;

    private String name;

    private Rank rankForRecruitment;
    private Rank rankForDischarge;
    private Rank rankForPromoting;

    public Tribe(Player founder, String name) {
        uniqueID = MathHelper.a(new Random());
        this.name = name;
        members = new HashMap<>();
        members.put(founder.getUniqueId(), Rank.LEADER);

        rankForRecruitment = Rank.LEADER;
        rankForDischarge = Rank.LEADER;
        rankForPromoting = Rank.LEADER;

        tribeRegistry = TribeRegistry.getTribeRegistry();
        tribeRegistry.registerTribe(this);
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public Set<UUID> getMembers() {
        return members.keySet();
    }

    public boolean isMember(Player player) {
        return members.containsKey(player.getUniqueId());
    }

    private boolean checkTribeMembership(Player player) {
        if (members.containsKey(player.getUniqueId()))
            return true;
        MSEMain.getInstance().getLogger().log(Level.INFO, "Player " + player.getName() + " tried accessing the tribe " + this.name);
        return false;
    }

    /**
     * Recruites a new member to the tribe. The recruiter needs a rank that is high enough to recruit.
     *
     * @param recruit         New tribe member
     * @param executingMember Member that is trying to perform this action
     */
    public void add(Player recruit, Player executingMember) {

        if (!checkTribeMembership(executingMember))
            return;

        UUID recruitUUID = recruit.getUniqueId();
        UUID executingMemberUUID = executingMember.getUniqueId();

        if (!members.containsKey(recruitUUID)) {
            if (Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), rankForRecruitment))
                members.put(recruitUUID, Rank.RECRUIT);
            else
                executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());
        }

    }

    /**
     * Removes a player from the tribe. The executing player needs the permission to remove players and also a higher
     * rank than the member that is being removed.
     *
     * @param dischargedMember Removed tribe member
     * @param executingMember  Member that is trying to perform this action
     */
    public void remove(Player dischargedMember, Player executingMember) {

        if (!checkTribeMembership(executingMember))
            return;

        UUID dischargedMemberUUID = dischargedMember.getUniqueId();
        UUID executingMemberUUID = executingMember.getUniqueId();

        if (members.containsKey(dischargedMemberUUID)) {
            if (Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), rankForDischarge)
                    && Rank.rankIsHigher(members.get(executingMemberUUID), members.get(dischargedMemberUUID)))
                members.remove(dischargedMemberUUID);
            else
                executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());
        }

    }

    /**
     * Changes the rank for a tribe member. The executing member has to be allowed to promote/demote other members.
     * Also the new rank can't be higher than the rank of the executing member
     *
     * @param promotedMember  Member who's rank is being changed
     * @param newRank         New rank of the member
     * @param executingMember Member that is trying to perform this action
     */
    public void promote(Player promotedMember, Rank newRank, Player executingMember) {

        if (!checkTribeMembership(executingMember))
            return;

        UUID promotedMemberUUID = promotedMember.getUniqueId();
        UUID executingMemberUUID = executingMember.getUniqueId();

        if (!members.containsKey(promotedMemberUUID))
            executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_MEMBER_DOESNT_EXIST.toString());

        if (Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), rankForPromoting)
                && Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), newRank)) {
            members.remove(promotedMemberUUID);
            members.put(promotedMemberUUID, newRank);
        } else
            executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());

    }

    /**
     * Increases or decreases the rank needed to recruit new members.
     *
     * @param newRank         New rank needed for recruitment
     * @param executingMember Member that is trying to perform this action
     */
    public void setRankForRecruitment(Rank newRank, Player executingMember) {
        if (!checkTribeMembership(executingMember))
            return;

        if (Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), rankForPromoting)
                && Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), newRank))
            rankForRecruitment = newRank;
        else
            executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());
    }

    /**
     * Increases or decreases the rank needed to remove members.
     *
     * @param newRank         New rank needed for removing members
     * @param executingMember Member that is trying to perform this action
     */
    public void setRankForDischarge(Rank newRank, Player executingMember) {
        if (!checkTribeMembership(executingMember))
            return;

        if (Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), rankForPromoting)
                && Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), newRank))
            rankForDischarge = newRank;
        else
            executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());
    }

    /**
     * Increases or decreases the rank needed to promote/demote players.
     *
     * @param newRank         New rank needed for promoting/demoting
     * @param executingMember Member that is trying to perform this action
     */
    public void setRankForPromoting(Rank newRank, Player executingMember) {
        if (!checkTribeMembership(executingMember))
            return;

        if (Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), rankForPromoting)
                && Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), newRank))
            rankForPromoting = newRank;
        else
            executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());
    }

    public void deleteTribe(Player executingMember) {
        if (!checkTribeMembership(executingMember))
            return;

        if (Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), Rank.LEADER)) {
            tribeRegistry.unregisterTribe(this);
        } else
            executingMember.sendMessage(MSECommandExecutor.ChatMessages.TRIBE_RANK_TOO_LOW.toString());
    }

}
