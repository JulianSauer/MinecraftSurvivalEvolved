package de.juliansauer.minecraftsurvivalevolved.entities.mseentities;

import de.juliansauer.minecraftsurvivalevolved.gui.inventories.InventoryGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Interactable {

    /**
     * Lets a player interact with an entity. Per default a gui is opened if the target is unconscious/tamed.
     *
     * @param target            Target entity as {@link Entity}
     * @param mseTarget         Target entity as {@link MSEEntity}
     * @param interactingPlayer Invoker
     * @param gui               GUI for opening a custom inventory
     * @return True if an inventory was opened
     */
    default boolean interact(Entity target, MSEEntity mseTarget, Player interactingPlayer, InventoryGUI gui) {
        if (mseTarget.isUnconscious() && !mseTarget.isTamed() && !mseTarget.isAlpha()) {
            gui.openTamingGUI(interactingPlayer, mseTarget);
            return true;
        } else if (mseTarget.isTamed() && mseTarget.isOwner(interactingPlayer.getUniqueId())) {
            if (interactingPlayer.isSneaking() || mseTarget.isUnconscious()) {
                gui.openMainGUI(interactingPlayer, mseTarget);
                return true;
            }
        }
        return false;
    }

}
