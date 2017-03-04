package de.julianSauer.minecraftSurvivalEvolved.tribes;

import de.julianSauer.minecraftSurvivalEvolved.commands.ChatMessages;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

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

    /**
     * @param name     Name of the tribe
     * @param register Defines if the tribe should be cached in the TribeRegistry; set to false for a dummy object
     */
    public Tribe(String name, boolean register) {
        uniqueID = MathHelper.a(new Random());
        this.name = name;
        members = new HashMap<>();

        rankForRecruitment = Rank.LEADER;
        rankForDischarge = Rank.LEADER;
        rankForPromoting = Rank.LEADER;

        tribeRegistry = TribeRegistry.getTribeRegistry();
        if (register)
            tribeRegistry.registerTribe(this);
    }

    public Tribe(Player founder, String name) {
        this(name, true);
        members.put(founder.getUniqueId(), Rank.LEADER);
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public Set<UUID> getMemberUUIDs() {
        return members.keySet();
    }

    public Set<OfflinePlayer> getMembers() {
        return getMemberUUIDs().stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toSet());
    }

    public boolean isMember(Player player) {
        return members.containsKey(player.getUniqueId());
    }

    public Rank getRankOfMember(UUID playerUUID) {
        return members.get(playerUUID);
    }

    public Rank getRankOfMember(Player player) {
        return members.get(player.getUniqueId());
    }

    public Rank getRankForRecruitment() {
        return rankForRecruitment;
    }

    public Rank getRankForDischarge() {
        return rankForDischarge;
    }

    public Rank getRankForPromoting() {
        return rankForPromoting;
    }

    private boolean checkTribeMembership(Player player) {
        if (members.containsKey(player.getUniqueId()))
            return true;
        MSEMain.getInstance().getLogger().log(Level.INFO, "Player " + player.getName() + " tried accessing the tribe " + this.name);
        return false;
    }

    public void add(UUID playerUUID, Rank rank) {
        if (members.containsKey(playerUUID))
            return;
        members.put(playerUUID, rank);
        TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(playerUUID).setTribe(this);
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
                executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
        }

    }

    /**
     * Removes a player from the tribe. The executing player needs the permission to remove players and also a higher
     * rank than the member that is being removed. If a member is leaving the tribe, both parameters should be the same
     * player.
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
            if ((Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), rankForDischarge)
                    && Rank.rankIsHigher(members.get(executingMemberUUID), members.get(dischargedMemberUUID)))
                    || executingMemberUUID.equals(dischargedMemberUUID)) {
                TribeMemberRegistry tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
                tribeMemberRegistry.getTribeMember(dischargedMemberUUID).setTribe(null);
                members.remove(dischargedMemberUUID);
            } else
                executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
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
            executingMember.sendMessage(ChatMessages.ERROR_TRIBE_MEMBER_DOESNT_EXIST.setParams(
                    Bukkit.getOfflinePlayer(promotedMemberUUID).getName(),
                    name
            ));

        if (Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), rankForPromoting)
                && Rank.rankIsEqualOrHigher(members.get(executingMemberUUID), newRank)) {
            members.remove(promotedMemberUUID);
            members.put(promotedMemberUUID, newRank);
        } else
            executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());

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
            executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
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
            executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
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
            executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
    }

    /**
     * Deletes a tribe.
     *
     * @param executingMember Member that is trying to perform this action or null to skip a rank check
     */
    public void deleteTribe(Player executingMember) {
        if (executingMember != null && !checkTribeMembership(executingMember))
            return;

        if (executingMember == null || Rank.rankIsEqualOrHigher(members.get(executingMember.getUniqueId()), Rank.LEADER)) {
            tribeRegistry.unregisterTribe(this);
        } else
            executingMember.sendMessage(ChatMessages.ERROR_TRIBE_RANK_TOO_LOW.toString());
    }

}
