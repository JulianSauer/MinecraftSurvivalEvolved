package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;

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

    Map<Integer, Button> getButtons(MSEEntity mseEntity);

}
