package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.tribes.Rank;
import de.julianSauer.minecraftSurvivalEvolved.tribes.Tribe;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMember;
import de.julianSauer.minecraftSurvivalEvolved.tribes.TribeMemberRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons that show the ranks within a tribe. Also allow decreasing/increasing them.
 */
public class RankEditButtonFactory extends RankViewButtonFactory {

    private InventoryGUI gui;
    private ButtonIcons icons;

    private Tribe tribe;
    private Rank playerRank; // Prevents players from increasing the rank higher than their own rank

    public RankEditButtonFactory(InventoryGUI gui) {
        this.gui = gui;
        icons = new ButtonIcons();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        TribeMember member = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(player);
        tribe = member.getTribe();
        playerRank = member.getRank();

        Map<Integer, Button> buttonMap = new HashMap<>(22);
        buttonMap.put(0, new RankViewChangeRank(tribe.getRankForChangingRanks()));
        buttonMap.put(1, new RankViewRecruitment(tribe.getRankForRecruitment()));
        buttonMap.put(2, new RankViewDischarge(tribe.getRankForDischarge()));
        buttonMap.put(3, new RankViewPromoting(tribe.getRankForPromoting()));
        buttonMap.put(4, new EmptyButton());
        buttonMap.put(5, new EmptyButton());
        buttonMap.put(6, new EmptyButton());
        buttonMap.put(7, new EmptyButton());
        buttonMap.put(8, new EmptyButton());
        buttonMap.put(9, new RankIncreaseChangeRank());
        buttonMap.put(10, new RankIncreaseRecruitmentRank());
        buttonMap.put(11, new RankIncreaseDischargeRank());
        buttonMap.put(12, new RankIncreasePromotingRank());
        buttonMap.put(13, new EmptyButton());
        buttonMap.put(14, new EmptyButton());
        buttonMap.put(15, new EmptyButton());
        buttonMap.put(16, new EmptyButton());
        buttonMap.put(17, new EmptyButton());
        buttonMap.put(18, new RankDecreaseChangeRank());
        buttonMap.put(19, new RankDecreaseRecruitmentRank());
        buttonMap.put(20, new RankDecreaseDischargeRank());
        buttonMap.put(21, new RankDecreasePromotingRank());
        return buttonMap;

    }

    class RankIncreaseChangeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForChangingRanks();
            current = Rank.getNextHigher(current);
            tribe.setRankForChangingRanks(Rank.getLower(current, playerRank));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankDecreaseChangeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForChangingRanks();
            tribe.setRankForChangingRanks(Rank.getNextLower(current));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankIncreaseRecruitmentRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForRecruitment();
            current = Rank.getNextHigher(current);
            tribe.setRankForRecruitment(Rank.getLower(current, playerRank));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankDecreaseRecruitmentRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForRecruitment();
            tribe.setRankForRecruitment(Rank.getNextLower(current));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankIncreaseDischargeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForDischarge();
            current = Rank.getNextHigher(current);
            tribe.setRankForDischarge(Rank.getLower(current, playerRank));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankDecreaseDischargeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForDischarge();
            current = Rank.getNextHigher(current);
            tribe.setRankForDischarge(Rank.getNextLower(current));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankIncreasePromotingRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForPromoting();
            current = Rank.getNextHigher(current);
            tribe.setRankForPromoting(Rank.getLower(current, playerRank));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

    class RankDecreasePromotingRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankForPromoting();
            current = Rank.getNextHigher(current);
            tribe.setRankForPromoting(Rank.getNextLower(current));
            gui.openRankEditGUI(player); // Updates the GUI
        }
    }

}
