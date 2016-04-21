package de.gmx.endermansend.tameableCreatures.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {

        if (!(e.getInventory() instanceof CraftingInventory))
            return;

        CraftingInventory craftingInventory = e.getInventory();

        ItemStack[] ingredients = craftingInventory.getMatrix();

        if (ingredients.length == 2) {
            if (ingredients[0].getType() == Material.ARROW &&
                    ingredients[1].getType() == Material.POTION) {
                if (!correctIngredients(ingredients[0], ingredients[1])) {
                    e.setCancelled(true);
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
            } else if (ingredients[1].getType() == Material.ARROW &&
                    ingredients[0].getType() == Material.POTION) {
                if (!correctIngredients(ingredients[1], ingredients[0])) {
                    e.setCancelled(true);
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }

            } else {
                e.setCancelled(true);
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }

    }

    public boolean correctIngredients(ItemStack arrow, ItemStack narcotics) {
        if (narcotics.getItemMeta().getDisplayName().equalsIgnoreCase("Narcotics"))
            return true;
        return false;
    }

}
