package de.julianSauer.minecraftSurvivalEvolved.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class CustomRecipes {

    public void setUpNarcotics() {
        ItemStack narcotics = new ItemStack(Material.POTION);
        ItemMeta narcoticsMeta = narcotics.getItemMeta();
        narcoticsMeta.setDisplayName("Narcotics");
        narcotics.setItemMeta(narcoticsMeta);
        narcotics = changeStackSizeOf(narcotics, 64);

        ShapelessRecipe recipe = new ShapelessRecipe(narcotics);
        recipe.addIngredient(Material.ROTTEN_FLESH);
        recipe.addIngredient(Material.POISONOUS_POTATO);
        Bukkit.addRecipe(recipe);
    }

    public void setUpTranquilizerArrow() {
        ShapelessRecipe recipe = new ShapelessRecipe(getTranquilizerArrow());
        recipe.addIngredient(Material.ARROW);
        recipe.addIngredient(Material.POTION);
        Bukkit.addRecipe(recipe);
    }

    public static ItemStack getTranquilizerArrow() {
        ItemStack tranquilizerArrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta tranquilizerArrowMeta = (PotionMeta) tranquilizerArrow.getItemMeta();
        tranquilizerArrowMeta.setDisplayName("Tranquilizer Arrow");
        tranquilizerArrowMeta.setBasePotionData(new PotionData(PotionType.JUMP));
        tranquilizerArrowMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        tranquilizerArrow.setItemMeta(tranquilizerArrowMeta);
        return tranquilizerArrow;
    }

    private ItemStack changeStackSizeOf(ItemStack itemStack, int size) {
        net.minecraft.server.v1_9_R1.ItemStack mcTranquilizierArrow = CraftItemStack.asNMSCopy(itemStack);
        mcTranquilizierArrow.getItem().d(size);
        return CraftItemStack.asBukkitCopy(mcTranquilizierArrow);
    }

}
