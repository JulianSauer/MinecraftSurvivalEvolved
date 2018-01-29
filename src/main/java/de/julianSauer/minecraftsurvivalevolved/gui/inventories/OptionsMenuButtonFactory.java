package de.juliansauer.minecraftsurvivalevolved.gui.inventories;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import de.juliansauer.minecraftsurvivalevolved.gui.SignGUI;
import de.juliansauer.minecraftsurvivalevolved.utils.NameChanger;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons for an entity's options menu.
 */
public class OptionsMenuButtonFactory implements ButtonFactory {

    private InventoryGUI gui;
    private ButtonIcons icons;

    private boolean glow;

    public OptionsMenuButtonFactory(InventoryGUI gui) {
        this.gui = gui;
        icons = new ButtonIcons();
        glow = false;
    }

    @Override
    public void setGlowing(boolean glow) {
        this.glow = glow;
    }

    @Override
    public Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        Map<Integer, Button> buttonMap = new HashMap<>(7);
        buttonMap.put(0, new OptionsMenuBack());
        buttonMap.put(1, new OptionsMenuName());
        switch (mseEntity.getEntityMode()) {
            case PASSIVE:
                buttonMap.put(2, new OptionsMenuPassiveMode());
                break;
            case NEUTRAL:
                buttonMap.put(2, new OptionsMenuNeutralMode());
                break;
            case AGGRESSIVE:
                buttonMap.put(2, new OptionsMenuAggressiveMode());
                break;
        }
        if (mseEntity.isFollowing() && mseEntity.getFollowingPlayer() != null)
            buttonMap.put(3, new OptionsMenuFollowingPlayer(mseEntity.getFollowingPlayer().getName()));
        else
            buttonMap.put(3, new OptionsMenuFollowing());
        buttonMap.put(4, new OptionsMenuHealth());
        buttonMap.put(5, new OptionsMenuDamage());
        buttonMap.put(6, new OptionsMenuFood());

        return buttonMap;
    }

    class OptionsMenuBack implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getBackButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            gui.openMainGUI(player, mseEntity);
        }
    }

    class OptionsMenuName implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getChangeNameButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            player.closeInventory();
            NameChanger.markEntityForNameChange(player.getUniqueId(), mseEntity);
            SignGUI.sendSignToPlayer(player);
        }
    }

    class OptionsMenuPassiveMode implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getPassiveModeButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.setNeutralGoals();
            gui.openOptionsGUI(player, mseEntity, true); // Updates the GUI
        }
    }

    class OptionsMenuNeutralMode implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getNeutralModeButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.setAggressiveGoals();
            gui.openOptionsGUI(player, mseEntity, true); // Updates the GUI
        }
    }

    class OptionsMenuAggressiveMode implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getAggressiveModeButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.setPassiveGoals();
            gui.openOptionsGUI(player, mseEntity, true); // Updates the GUI
        }
    }

    class OptionsMenuFollowing implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getFollowingButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            if (player instanceof CraftPlayer)
                mseEntity.toggleFollowing(((CraftPlayer) player).getHandle());
            gui.openOptionsGUI(player, mseEntity, true); // Updates the GUI
        }

    }

    class OptionsMenuFollowingPlayer implements Button {

        private final String player;

        OptionsMenuFollowingPlayer(String player) {
            this.player = player;
        }

        @Override
        public ItemStack getButton() {
            return icons.getFollowingPlayerButton(player);
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            if (player instanceof CraftPlayer)
                mseEntity.toggleFollowing(((CraftPlayer) player).getHandle());
            gui.openOptionsGUI(player, mseEntity, true); // Updates the GUI
        }

    }

    class OptionsMenuHealth implements Button {

        @Override
        public ItemStack getButton() {
            if (glow)
                return icons.makeGlow(icons.getIncreaseHealthButton());
            return icons.getIncreaseHealthButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {

        }
    }

    class OptionsMenuDamage implements Button {

        @Override
        public ItemStack getButton() {
            if (glow)
                return icons.makeGlow(icons.getIncreaseDamageButton());
            return icons.getIncreaseDamageButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {

        }
    }

    class OptionsMenuFood implements Button {

        @Override
        public ItemStack getButton() {
            if (glow)
                return icons.makeGlow(icons.getIncreaseFoodButton());
            return icons.getIncreaseFoodButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {

        }
    }

}
