package de.julianSauer.minecraftSurvivalEvolved.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Handles smooth crafting of custom recipies.
 */
public class PrepareItemCraftListener implements BasicEventListener {

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e) {

        ItemMeta itemMeta = e.getRecipe().getResult().getItemMeta();
        if (!(e.getInventory() instanceof CraftingInventory)
                || !itemMeta.hasDisplayName()
                || !(itemMeta.getDisplayName().equals("Tranquilizer Arrow"))) {
            return;
        }
        CraftingInventory craftingInventory = e.getInventory();

        ItemStack[] inventorySlots = craftingInventory.getMatrix();

        if (getNarcotics(inventorySlots) == null || getArrow(inventorySlots) == null) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            return;
        }

    }

    /**
     * Returns an item of Material.POTION type called Narcotics or null if not used.
     *
     * @param inventorySlots Slots of the crafting inventory
     * @return A correct item or null
     */
    private ItemStack getNarcotics(ItemStack[] inventorySlots) {
        for (ItemStack inventorySlot : inventorySlots) {
            if (inventorySlot != null) {
                if (inventorySlot.getType() == Material.POTION) {
                    ItemMeta itemMeta = inventorySlot.getItemMeta();
                    if (itemMeta.hasDisplayName())
                        if (itemMeta.getDisplayName().equalsIgnoreCase("Narcotics"))
                            return inventorySlot;
                }
            }
        }
        return null;
    }

    /**
     * Returns an item of Material.ARROW type that doesn't have a display name. That way it should be a default arrow.
     *
     * @param inventorySlots Slots of the crafting inventory
     * @return A correct item or null
     */
    private ItemStack getArrow(ItemStack[] inventorySlots) {
        for (ItemStack inventorySlot : inventorySlots) {
            if (inventorySlot != null)
                if (inventorySlot.getType() == Material.ARROW) {
                    ItemMeta itemMeta = inventorySlot.getItemMeta();
                    if (!itemMeta.hasDisplayName())
                        return inventorySlot;
                }
        }
        return null;
    }

}
