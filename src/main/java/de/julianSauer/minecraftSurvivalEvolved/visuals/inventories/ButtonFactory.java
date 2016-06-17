package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

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

    Map<Integer, Button> getButtons();

}
