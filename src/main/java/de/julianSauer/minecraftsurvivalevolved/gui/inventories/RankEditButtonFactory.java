package de.juliansauer.minecraftsurvivalevolved.gui.inventories;

import de.juliansauer.minecraftsurvivalevolved.tribes.*;
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

    abstract class RankIncrease implements Button {

        RankPermission permission;

        RankIncrease(RankPermission permission) {
            this.permission = permission;
        }

        @Override
        public ItemStack getButton() {
            Rank nextRank = Rank.getNextHigher(tribe.getRankFor(permission));
            return icons.getIncreaseRankButton(nextRank);
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankFor(permission);
            current = Rank.getNextHigher(current);
            tribe.setRankFor(permission, Rank.getLower(current, playerRank));
            gui.openRankEditGUI(player); // Updates the GUI
        }

    }

    abstract class RankDecrease implements Button {

        RankPermission permission;

        RankDecrease(RankPermission permission) {
            this.permission = permission;
        }

        @Override
        public ItemStack getButton() {
            Rank nextRank = Rank.getNextLower(tribe.getRankFor(permission));
            return icons.getDecreaseRankButton(nextRank);
        }

        @Override
        public void onClick(Player player) {
            Rank current = tribe.getRankFor(permission);
            tribe.setRankFor(permission, Rank.getNextLower(current));
            gui.openRankEditGUI(player); // Updates the GUI
        }

    }

    class RankIncreaseChangeRank extends RankIncrease {
        RankIncreaseChangeRank() {
            super(RankPermission.CHANGING_RANKS);
        }
    }

    class RankDecreaseChangeRank extends RankDecrease {
        RankDecreaseChangeRank() {
            super(RankPermission.CHANGING_RANKS);
        }
    }

    class RankIncreaseRecruitmentRank extends RankIncrease {
        RankIncreaseRecruitmentRank() {
            super(RankPermission.RECRUITING);
        }
    }

    class RankDecreaseRecruitmentRank extends RankDecrease {
        RankDecreaseRecruitmentRank() {
            super(RankPermission.RECRUITING);
        }
    }

    class RankIncreaseDischargeRank extends RankIncrease {
        RankIncreaseDischargeRank() {
            super(RankPermission.DISCHARGING);
        }
    }

    class RankDecreaseDischargeRank extends RankDecrease {
        RankDecreaseDischargeRank() {
            super(RankPermission.DISCHARGING);
        }
    }

    class RankIncreasePromotingRank extends RankIncrease {
        RankIncreasePromotingRank() {
            super(RankPermission.PROMOTING);
        }
    }

    class RankDecreasePromotingRank extends RankDecrease {
        RankDecreasePromotingRank() {
            super(RankPermission.PROMOTING);
        }
    }

}
