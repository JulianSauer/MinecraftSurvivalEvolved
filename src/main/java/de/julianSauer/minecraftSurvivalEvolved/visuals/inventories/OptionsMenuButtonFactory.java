package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.utils.NameChanger;
import de.julianSauer.minecraftSurvivalEvolved.visuals.SignGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons for an entity's options menu.
 */
public class OptionsMenuButtonFactory implements ButtonFactory {

    private boolean glow;

    public OptionsMenuButtonFactory() {
        glow = false;
    }

    @Override
    public void setGlowing(boolean glow) {
        this.glow = glow;
    }

    @Override
    public Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        Map<Integer, Button> buttonMap = new HashMap<>(6);
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
        buttonMap.put(3, new OptionsMenuHealth());
        buttonMap.put(4, new OptionsMenuDamage());
        buttonMap.put(5, new OptionsMenuFood());

        return buttonMap;
    }

    class OptionsMenuBack implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getBackButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            InventoryGUI.openMainGUI(player, mseEntity);
        }
    }

    class OptionsMenuName implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getChangeNameButton();
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
            return ButtonIcons.getPassiveModeButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.setNeutralGoals();
        }
    }

    class OptionsMenuNeutralMode implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getNeutralModeButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.setAggressiveGoals();
        }
    }

    class OptionsMenuAggressiveMode implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getAggressiveModeButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.setPassiveGoals();
        }
    }

    class OptionsMenuHealth implements Button {

        @Override
        public ItemStack getButton() {
            if (glow)
                return ButtonIcons.makeGlow(ButtonIcons.getIncreaseHealthButton());
            return ButtonIcons.getIncreaseHealthButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {

        }
    }

    class OptionsMenuDamage implements Button {

        @Override
        public ItemStack getButton() {
            if (glow)
                return ButtonIcons.makeGlow(ButtonIcons.getIncreaseDamageButton());
            return ButtonIcons.getIncreaseDamageButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {

        }
    }

    class OptionsMenuFood implements Button {

        @Override
        public ItemStack getButton() {
            if (glow)
                return ButtonIcons.makeGlow(ButtonIcons.getIncreaseFoodButton());
            return ButtonIcons.getIncreaseFoodButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {

        }
    }

}
