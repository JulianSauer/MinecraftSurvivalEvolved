package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates buttons for an entity's inventory while it's being tamed.
 */
public class TamingButtonFactory implements ButtonFactory {

    private ButtonIcons icons;

    public TamingButtonFactory() {
        icons = new ButtonIcons();
    }

    @Override
    public Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        Map<Integer, Button> buttonMap = new HashMap<>(5);
        buttonMap.put(0, new TamingMenuForceFeed());
        return buttonMap;
    }

    class TamingMenuForceFeed implements Button {

        @Override
        public ItemStack getButton() {
            return icons.getForceFeedButton();
        }

        @Override
        public void onClick(Player player, MSEEntity mseEntity) {
            mseEntity.getTamingHandler().feedNarcotics();
        }
    }
}