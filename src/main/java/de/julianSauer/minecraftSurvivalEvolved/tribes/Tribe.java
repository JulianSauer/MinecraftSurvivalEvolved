package de.julianSauer.minecraftSurvivalEvolved.tribes;

import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.messages.ChatMessages;
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

    private Map<RankPermission, Rank> ranks;

    private TribeLogger tribeLogger;

    /**
     * Creates a new tribe.
     *
     * @param founder Player who will own the tribe
     * @param name    Name of the tribe
     */
    public Tribe(Player founder, String name) {
        this(name, true, MathHelper.a(new Random()));
        this.founder = founder.getUniqueId();
        TribeMember foundingMember = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(founder);
        if (foundingMember == null)
            MSEMain.getInstance().getLogger().warning("Could not create tribe " + name + ". Founder " + founder.getName() + " is offline."); // Throws NPE afterwards
        foundingMember.setTribe(this);
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

        ranks = new HashMap<>();
        // Default values
        ranks.put(RankPermission.CHANGING_RANKS, Rank.FOUNDER);
        ranks.put(RankPermission.RECRUITING, Rank.LEADER);
        ranks.put(RankPermission.DISCHARGING, Rank.LEADER);
        ranks.put(RankPermission.PROMOTING, Rank.LEADER);

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

    public OfflinePlayer getMember(String name) {
        for (OfflinePlayer member : getMembers())
            if (member.getName().equalsIgnoreCase(name))
                return member;
        return null;
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

    public void setFounder(UUID newFounder) {
        members.remove(newFounder);
        members.put(newFounder, Rank.FOUNDER);
        founder = newFounder;
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

    public Rank getRankFor(RankPermission permission) {
        return ranks.get(permission);
    }

    public Map<RankPermission, Rank> getPermissionsForRanks() {
        return ranks;
    }

    public void setRankFor(RankPermission permission, Rank newRank) {
        ranks.put(permission, newRank);
    }

    public TribeLogger getLogger() {
        return tribeLogger;
    }

    /**
     * Loads a player of this tribe.
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
        return remove(dischargedMember.getUniqueId());
    }

    /**
     * Removes/kicks a player from the tribe. Transfers ownership if the leaving member is the owner.
     *
     * @param dischargedMemberUUID Removed tribe member
     * @return The UUID of the new founder if one was determined or null
     */
    public UUID remove(UUID dischargedMemberUUID) {

        if (members.containsKey(dischargedMemberUUID)) {
            TribeMemberRegistry tribeMemberRegistry = TribeMemberRegistry.getTribeMemberRegistry();
            TribeMember dischargedMember = tribeMemberRegistry.getTribeMember(dischargedMemberUUID);
            if (dischargedMember != null)
                dischargedMember.setTribe(null);
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
     */
    private void transferOwnership() {
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
        UUID newFounder = founderCandidate.getKey();
        if (members.containsKey(newFounder)) {
            members.remove(newFounder);
            members.put(newFounder, Rank.FOUNDER);
            founder = newFounder;
        }
    }

    /**
     * Changes the rank of a tribe member.
     *
     * @param member  Member who's rank is being changed
     * @param newRank New rank of the member
     */
    public void setRankOf(Player member, Rank newRank) {
        setRankOf(member.getUniqueId(), newRank, member.getName());
    }

    /**
     * Changes the rank of a tribe member.
     *
     * @param member  Member who's rank is being changed
     * @param newRank New rank of the member
     */
    public void setRankOf(UUID member, Rank newRank) {
        setRankOf(member, newRank, Bukkit.getOfflinePlayer(member).getName());
    }


    /**
     * Changes the rank of a tribe member.
     *
     * @param member  Member who's rank is being changed
     * @param newRank New rank of the member
     * @param name    Name of the member
     */
    private void setRankOf(UUID member, Rank newRank, String name) {
        members.remove(member);
        members.put(member, newRank);
        tribeLogger.log(ChatMessages.TRIBE_MEMBER_RANK_CHANGED.setParams(name, newRank.toString()));
    }

    /**
     * Deletes this tribe.
     */
    public void deleteTribe() {
        tribeRegistry.unregisterTribe(this);
    }

    /**
     * Sends a message to every tribe member that is online and saves it to the tribe log.
     */
    public void sendMessageToMembers(String message) {
        getMembers().stream()
                .filter(OfflinePlayer::isOnline)
                .forEach(member -> ((Player) member)
                        .sendMessage(message));
        tribeLogger.log(message);
    }

}
