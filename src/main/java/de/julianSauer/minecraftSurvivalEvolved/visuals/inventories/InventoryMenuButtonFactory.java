package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons for an entity's inventory.
 */
public class InventoryMenuButtonFactory implements ButtonFactory {

    @Override
    public Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        Map<Integer, Button> buttonMap = new HashMap<>(1);
        buttonMap.put(0, new InventoryMenuBack());
        return buttonMap;
    }

    class InventoryMenuBack implements Button {

        @Override
        public ItemStack getButton() {
            return ButtonIcons.getBackButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            InventoryGUI.openMainGUI(player, mseEntity);
        }

    }

}
