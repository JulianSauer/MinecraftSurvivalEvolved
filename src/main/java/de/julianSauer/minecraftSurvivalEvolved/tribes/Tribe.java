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

    /**
     * Loads a player of this tribe.
     *
     * @param playerUUID
     * @param rank
     */
    public void loadMember(UUID playerUUID, Rank rank) {
        if (members.containsKey(playerUUID))
            return;
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
     * Removes/kicks a player from the tribe.
     *
     * @param dischargedMember Removed tribe member
     */
    public void remove(Player dischargedMember) {

        UUID dischargedMemberUUID = dischargedMember.getUniqueId();

        if (members.containsKey(dischargedMemberUUID)) {
            TribeMemberRegistry tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
            tribeMemberRegistry.getTribeMember(dischargedMemberUUID).setTribe(null);
            members.remove(dischargedMemberUUID);
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
