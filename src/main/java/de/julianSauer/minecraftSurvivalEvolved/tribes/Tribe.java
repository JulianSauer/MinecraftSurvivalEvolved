package de.julianSauer.minecraftSurvivalEvolved.tribes;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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

    // Has all rights
    private UUID founder;

    private String name;

    private Rank rankForRecruitment;
    private Rank rankForDischarge;
    private Rank rankForPromoting;

    private TribeLogger tribeLogger;

    /**
     * Creates a new tribe.
     *
     * @param founder Player who will own the tribe
     * @param name
     */
    public Tribe(Player founder, String name) {
        this(name, true, MathHelper.a(new Random()));
        this.founder = founder.getUniqueId();
        TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(founder).setTribe(this);
        members.put(founder.getUniqueId(), Rank.FOUNDER);
        tribeLogger.log("Tribe was founded by " + founder.getName());
    }

    /**
     * @param name     Name of the tribe
     * @param register Defines if the tribe should be cached in the TribeRegistry; set to false for a dummy object
     * @param uniqueID ID of this tribe
     */
    public Tribe(String name, boolean register, UUID uniqueID) {
        this.uniqueID = uniqueID;
        this.name = name;
        members = new HashMap<>();
        tribeLogger = new TribeLogger();
        tribeLogger.setTribe(this);

        rankForRecruitment = Rank.LEADER;
        rankForDischarge = Rank.LEADER;
        rankForPromoting = Rank.LEADER;

        tribeRegistry = TribeRegistry.getTribeRegistry();
        if (register)
            tribeRegistry.registerTribe(this);
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

    public boolean isMember(UUID player) {
        return members.containsKey(player);
    }

    public boolean isFounder(Player member) {
        return member.getUniqueId().equals(founder);
    }

    public UUID getFounder() {
        return founder;
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

    public void setRankForRecruitment(Rank newRank) {
        rankForRecruitment = newRank;
    }

    public Rank getRankForDischarge() {
        return rankForDischarge;
    }

    public void setRankForDischarge(Rank newRank) {
        rankForDischarge = newRank;
    }

    public Rank getRankForPromoting() {
        return rankForPromoting;
    }

    public void setRankForPromoting(Rank newRank) {
        rankForPromoting = newRank;
    }

    public TribeLogger getLogger() {
        return tribeLogger;
    }

    /**
     * Loads a player of this tribe.
     *
     * @param playerUUID
     * @param rank
     */
    public void loadMember(UUID playerUUID, Rank rank) {
        if (members.containsKey(playerUUID))
            return;
        if (rank.equals(Rank.FOUNDER))
            founder = playerUUID;
        members.put(playerUUID, rank);
    }

    private boolean checkTribeMembership(Player player) {
        if (members.containsKey(player.getUniqueId()))
            return true;
        MSEMain.getInstance().getLogger().log(Level.INFO, "Player " + player.getName() + " tried accessing the tribe " + this.name);
        return false;
    }

    /**
     * Adds a new player to the tribe.
     *
     * @param recruitUUID
     */
    public void addNewMember(UUID recruitUUID) {
        loadMember(recruitUUID, Rank.RECRUIT);
        TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(recruitUUID).setTribe(this);
    }

    /**
     * Removes/kicks a player from the tribe. Transfers ownership if the leaving member is the owner.
     *
     * @param dischargedMember Removed tribe member
     * @return The UUID of the new founder if one was determined or null
     */
    public UUID remove(Player dischargedMember) {

        UUID dischargedMemberUUID = dischargedMember.getUniqueId();

        if (members.containsKey(dischargedMemberUUID)) {
            TribeMemberRegistry tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
            tribeMemberRegistry.getTribeMember(dischargedMemberUUID).setTribe(null);
            members.remove(dischargedMemberUUID);
        }

        if (members.isEmpty()) {
            deleteTribe();
            return null;
        }

        // Transfer ownership of this tribe to highest ranked member
        if (founder.equals(dischargedMemberUUID)) {
            transferOwnership();
            return founder;
        }

        return null;

    }

    /**
     * Transfer ownership of this tribe to the highest ranked member.
     *
     * @return UUID of the new founder
     */
    public void transferOwnership() {
        Map.Entry<UUID, Rank> founderCandidate = null;
        for (UUID member : members.keySet()) {

            Rank currentRank = members.get(member);
            if (founderCandidate == null) {
                founderCandidate = new AbstractMap.SimpleEntry<>(member, currentRank);
                continue;
            }
            if (Rank.rankIsHigher(currentRank, founderCandidate.getValue()))
                founderCandidate = new AbstractMap.SimpleEntry<>(member, currentRank);

        }
        transferOwnership(founderCandidate.getKey());
    }

    public void transferOwnership(UUID newFounder) {
        if (members.containsKey(newFounder)) {
            members.remove(newFounder);
            members.put(newFounder, Rank.FOUNDER);
            founder = newFounder;
        }
    }

    /**
     * Changes the rank of a tribe member.
     *
     * @param promotedMember Member who's rank is being changed
     * @param newRank        New rank of the member
     */
    public void promote(Player promotedMember, Rank newRank) {

        UUID promotedMemberUUID = promotedMember.getUniqueId();
        members.remove(promotedMemberUUID);
        members.put(promotedMemberUUID, newRank);

    }

    /**
     * Deletes this tribe.
     */
    public void deleteTribe() {
        tribeRegistry.unregisterTribe(this);
    }

    /**
     * Sends a message to every tribe member that is online.
     *
     * @param message
     */
    public void sendMessageToMembers(String message) {
        getMembers().stream()
                .filter(member -> member.isOnline())
                .forEach(member -> ((Player) member)
                        .sendMessage(message));
    }

}
