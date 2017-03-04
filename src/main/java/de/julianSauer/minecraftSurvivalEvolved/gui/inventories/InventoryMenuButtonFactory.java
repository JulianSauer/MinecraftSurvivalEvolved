package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons for an entity's inventory.
 */
public class InventoryMenuButtonFactory implements ButtonFactory {

    private InventoryGUI gui;
    private ButtonIcons icons;

    public InventoryMenuButtonFactory(InventoryGUI gui) {
        this.gui = gui;
        icons = new ButtonIcons();
    }

    @Override
    public Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        Map<Integer, Button> buttonMap = new HashMap<>(1);
        buttonMap.put(0, new InventoryMenuBack());
        return buttonMap;
    }

    class InventoryMenuBack implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getBackButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            gui.openMainGUI(player, mseEntity);
        }

    }

}
