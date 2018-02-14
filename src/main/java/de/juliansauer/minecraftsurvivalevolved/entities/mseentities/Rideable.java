package de.juliansauer.minecraftsurvivalevolved.entities.mseentities;

import de.juliansauer.minecraftsurvivalevolved.gui.inventories.InventoryGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Rideable extends Interactable {

    /**
     * Lets the invoking player ride the entity or open a gui.
     *
     * @param target            Target entity as {@link Entity}
     * @param mseTarget         Target entity as {@link MSEEntity}
     * @param interactingPlayer Invoker
     * @param gui               GUI for opening a custom inventory
     * @return True if the player rides the entity or opened an inventory
     */
    @Override
    default boolean interact(Entity target, MSEEntity mseTarget, Player interactingPlayer, InventoryGUI gui) {
        if (!Interactable.super.interact(target, mseTarget, interactingPlayer, gui)
                && target.isEmpty()
                && mseTarget.isTamed()
                && mseTarget.isOwner(interactingPlayer.getUniqueId())) {
            target.setPassenger(interactingPlayer);
            return true;
        }
        return false;
    }

}
