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

    private InventoryGUI gui;
    private ButtonIcons icons;

    public MainMenuButtonFactory(InventoryGUI gui) {
        this.gui = gui;
        icons = new ButtonIcons();
    }

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
            return icons.getOpenInventoryButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            gui.openInventoryGUI(player, mseEntity);
        }
    }

    class MainMenuOptions implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getOpenOptionsButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            gui.openOptionsGUI(player, mseEntity, true);
        }
    }

}
