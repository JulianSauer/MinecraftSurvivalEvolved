package de.juliansauer.minecraftsurvivalevolved.gui.inventories;

import de.juliansauer.minecraftsurvivalevolved.tribes.Rank;
import de.juliansauer.minecraftsurvivalevolved.tribes.RankPermission;
import de.juliansauer.minecraftsurvivalevolved.tribes.Tribe;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeMemberRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons that show the ranks within a tribe. Since the buttons are only used to display information, their
 * onClick methods are empty.
 */
public class RankViewButtonFactory implements ButtonFactory {

    private ButtonIcons icons;

    public RankViewButtonFactory() {
        icons = new ButtonIcons();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Tribe tribe = TribeMemberRegistry.getTribeMemberRegistry().getTribeMember(player).getTribe();
        Map<Integer, Button> buttonMap = new HashMap<>(4);
        buttonMap.put(0, new RankViewChangeRank(tribe.getRankFor(RankPermission.CHANGING_RANKS)));
        buttonMap.put(1, new RankViewRecruitment(tribe.getRankFor(RankPermission.RECRUITING)));
        buttonMap.put(2, new RankViewDischarge(tribe.getRankFor(RankPermission.DISCHARGING)));
        buttonMap.put(3, new RankViewPromoting(tribe.getRankFor(RankPermission.PROMOTING)));
        return buttonMap;
    }

    class RankViewChangeRank implements Button {

        private Rank rank;

        RankViewChangeRank(Rank rank) {
            this.rank = rank;
        }

        @Override
        public ItemStack getButton() {
            return icons.getRankChangeButton(rank);
        }

        @Override
        public void onClick(Player player) {
        }
    }

    class RankViewRecruitment implements Button {

        private Rank rank;

        RankViewRecruitment(Rank rank) {
            this.rank = rank;
        }

        @Override
        public ItemStack getButton() {
            return icons.getRankRecruitmentButton(rank);
        }

        @Override
        public void onClick(Player player) {
        }
    }

    class RankViewDischarge implements Button {

        private Rank rank;

        RankViewDischarge(Rank rank) {
            this.rank = rank;
        }

        @Override
        public ItemStack getButton() {
            return icons.getRankDischargeButton(rank);
        }

        @Override
        public void onClick(Player player) {
        }
    }

    class RankViewPromoting implements Button {

        private Rank rank;

        RankViewPromoting(Rank rank) {
            this.rank = rank;
        }

        @Override
        public ItemStack getButton() {
            return icons.getRankPromotingButton(rank);
        }

        @Override
        public void onClick(Player player) {
        }
    }

}
