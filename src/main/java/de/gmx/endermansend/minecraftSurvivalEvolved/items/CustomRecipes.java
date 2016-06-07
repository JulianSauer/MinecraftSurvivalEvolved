package de.gmx.endermansend.minecraftSurvivalEvolved.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomRecipes {

    public void setUpNarcotics() {
        ItemStack narcotics = new ItemStack(Material.POTION);
        ItemMeta narcoticsMeta = narcotics.getItemMeta();
        narcoticsMeta.setDisplayName("Narcotics");
        narcotics.setItemMeta(narcoticsMeta);

        ShapelessRecipe recipe = new ShapelessRecipe(narcotics);
        recipe.addIngredient(Material.ROTTEN_FLESH);
        recipe.addIngredient(Material.POISONOUS_POTATO);
        Bukkit.addRecipe(recipe);
    }

    public void setUpTranquilizerArrow() {
        ItemStack tranquilizerArrow = new ItemStack(Material.ARROW);
        ItemMeta tranquilizerArrowMeta = tranquilizerArrow.getItemMeta();
        tranquilizerArrowMeta.setDisplayName("Tranquilizer Arrow");
        tranquilizerArrow.setItemMeta(tranquilizerArrowMeta);

        ShapelessRecipe recipe = new ShapelessRecipe(tranquilizerArrow);
        recipe.addIngredient(Material.ARROW);
        recipe.addIngredient(Material.POTION);
        Bukkit.addRecipe(recipe);
    }

}
