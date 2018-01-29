package de.juliansauer.minecraftsurvivalevolved.gui.inventories;

import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.MSEEntity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Maps inventory slots to buttons.
 */
interface ButtonFactory {

    /**
     * Makes buttons glow. Deactivated by default.
     */
    default void setGlowing(boolean glow) {
    }

    /**
     * Creates the buttons for an inventory that is related to a player.
     * Implement either this method or {@link #getButtons(MSEEntity)}.
     *
     * @param player Player that opens the inventory
     * @return Buttons and their positions
     */
    default Map<Integer, Button> getButtons(Player player) {
        return null;
    }

    /**
     * Creates the buttons for an inventory that is related to an MSEEntity.
     * Implement either this method or {@link #getButtons(Player)}
     *
     * @param mseEntity Entity that owns the inventory
     * @return Buttons and their positions
     */
    default Map<Integer, Button> getButtons(MSEEntity mseEntity) {
        return null;
    }

    /**
     * Can be used as an empty slot in the GUI.
     */
    class EmptyButton implements Button {

        @Override
        public ItemStack getButton() {
            return new ItemStack(Material.AIR);
        }

        @Override
        public void onClick(Player player) {
        }
    }

}
