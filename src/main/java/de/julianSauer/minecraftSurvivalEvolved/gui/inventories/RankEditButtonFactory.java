package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.tribes.*;
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
        buttonMap.put(0, new RankViewChangeRank(tribe.getRankFor(RankPermission.CHANGING_RANKS)));
        buttonMap.put(1, new RankViewRecruitment(tribe.getRankFor(RankPermission.RECRUITING)));
        buttonMap.put(2, new RankViewDischarge(tribe.getRankFor(RankPermission.DISCHARGING)));
        buttonMap.put(3, new RankViewPromoting(tribe.getRankFor(RankPermission.PROMOTING)));
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

    private void increaseRankFor(RankPermission permission, Player player) {
        Rank current = tribe.getRankFor(permission);
        current = Rank.getNextHigher(current);
        tribe.setRankFor(permission, Rank.getLower(current, playerRank));
        gui.openRankEditGUI(player); // Updates the GUI
    }

    private void decreaseRankFor(RankPermission permission, Player player) {
        Rank current = tribe.getRankFor(permission);
        tribe.setRankFor(permission, Rank.getNextLower(current));
        gui.openRankEditGUI(player); // Updates the GUI
    }

    class RankIncreaseChangeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            increaseRankFor(RankPermission.CHANGING_RANKS, player);
        }
    }

    class RankDecreaseChangeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            decreaseRankFor(RankPermission.CHANGING_RANKS, player);
        }
    }

    class RankIncreaseRecruitmentRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            increaseRankFor(RankPermission.RECRUITING, player);
        }
    }

    class RankDecreaseRecruitmentRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            decreaseRankFor(RankPermission.RECRUITING, player);
        }
    }

    class RankIncreaseDischargeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            increaseRankFor(RankPermission.DISCHARGING, player);
        }
    }

    class RankDecreaseDischargeRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            decreaseRankFor(RankPermission.DISCHARGING, player);
        }
    }

    class RankIncreasePromotingRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getIncreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            increaseRankFor(RankPermission.PROMOTING, player);
        }
    }

    class RankDecreasePromotingRank implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getDecreaseRankButton();
        }

        @Override
        public void onClick(Player player) {
            decreaseRankFor(RankPermission.PROMOTING, player);
        }
    }

}
