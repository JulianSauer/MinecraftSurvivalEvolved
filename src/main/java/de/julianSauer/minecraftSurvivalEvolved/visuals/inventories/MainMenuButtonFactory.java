package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons for an entity's main menu.
 */
public class MainMenuButtonFactory implements ButtonFactory {

    @Override
    public Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        Map<Integer, Button> buttonMap = new HashMap<>(2);
        buttonMap.put(0, new MainMenuInventory());
        buttonMap.put(1, new MainMenuOptions());
        return buttonMap;
    }

    public class MainMenuInventory implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getOpenInventoryButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            InventoryGUI.openInventoryGUI(player, mseEntity);
        }
    }

    class MainMenuOptions implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getOpenOptionsButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            InventoryGUI.openOptionsGUI(player, mseEntity, true);
        }
    }

}
