package de.julianSauer.minecraftSurvivalEvolved.visuals;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryGUI {

    private static ItemStack[] statsButtons;
    private static ItemStack[] tamedButtons;

    /**
     * Initializes an entity's statistics menu.
     */
    private static void initializeStatsButtons() {

        // Back button
        ItemStack back = new ItemStack(Material.BANNER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("Back");
        back.setItemMeta(backMeta);

        // Name button
        ItemStack changeName = new ItemStack(Material.SIGN, 1);
        ItemMeta changeNameMeta = changeName.getItemMeta();
        changeNameMeta.setDisplayName("Change Name");
        changeName.setItemMeta(changeNameMeta);

        // Health button
        ItemStack health = new ItemStack(Material.NETHER_WARTS);
        ItemMeta healthMeta = health.getItemMeta();
        healthMeta.setDisplayName("Health");
        health.setItemMeta(healthMeta);

        // Damage button
        ItemStack damage = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta damageMeta = damage.getItemMeta();
        damageMeta.setDisplayName("Damage");
        damage.setItemMeta(damageMeta);

        // Food button
        ItemStack food = new ItemStack(Material.RAW_BEEF);
        ItemMeta foodMeta = food.getItemMeta();
        foodMeta.setDisplayName("Food");
        food.setItemMeta(foodMeta);

        statsButtons = new ItemStack[] {back, changeName, health, damage, food};

    }

    /**
     * Initializes the buttons for an entity's main menu.
     */
    private static void initializeTamedButtons() {
        // Inventory button
        ItemStack openInventory = new ItemStack(Material.CHEST, 1);
        ItemMeta openInventoryMeta = openInventory.getItemMeta();
        openInventoryMeta.setDisplayName("Open Inventory");
        openInventory.setItemMeta(openInventoryMeta);

        // Stats button
        ItemStack openStatsInventory = new ItemStack(Material.BOOK, 1);
        ItemMeta openStatsInventoryMeta = openStatsInventory.getItemMeta();
        openStatsInventoryMeta.setDisplayName("Open Entity Stats");
        openStatsInventory.setItemMeta(openStatsInventoryMeta);

        tamedButtons = new ItemStack[] {openInventory, openStatsInventory};
    }

    public static ItemStack[] getStatsButtons() {
        if(statsButtons == null)
            initializeStatsButtons();
        return statsButtons;
    }

    public static ItemStack[] getTamedButtons() {
        if(tamedButtons == null)
            initializeTamedButtons();
        return tamedButtons;
    }

    public static void openTamingGUI(Player player, MSEEntity mseEntity) {
        Inventory tamingGUI = mseEntity.getInventory();
        player.openInventory(tamingGUI);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    public static void openTamedGUI(Player player, MSEEntity mseEntity) {
        Inventory tamedGUI = Bukkit.createInventory(mseEntity, InventoryType.HOPPER, mseEntity.getEntityStats().getEntityType());

        if(tamedButtons == null)
            initializeTamedButtons();
        for(int i = 0; i<tamedButtons.length; i++)
            tamedGUI.setItem(i, tamedButtons[i]);

        player.openInventory(tamedGUI);
    }

    public static void openStatsGUI(Player player, MSEEntity mseEntity) {
        Inventory statsGUI = Bukkit.createInventory(mseEntity, 9, "Entity Stats");

        if(statsButtons == null)
            initializeStatsButtons();
        for(int i = 0; i<statsButtons.length; i++)
            statsGUI.setItem(i, statsButtons[i]);

        player.openInventory(statsGUI);
    }

}
