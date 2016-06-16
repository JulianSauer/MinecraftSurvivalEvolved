package de.julianSauer.minecraftSurvivalEvolved.visuals;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.ThisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryGUI {

    private static ItemStack[] optionsMenuButtons;
    private static ItemStack[] mainMenuButtons;
    private static ItemStack[] inventoryMenuButtons;

    /**
     * Initializes an entity's statistics menu.
     */
    private static void initializeOptionsMenuButtons() {

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
        ItemStack health = new ItemStack(Material.NETHERRACK);
        ItemMeta healthMeta = health.getItemMeta();
        healthMeta.setDisplayName("Health");
        healthMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        health.setItemMeta(healthMeta);

        // Damage button
        ItemStack damage = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta damageMeta = damage.getItemMeta();
        damageMeta.setDisplayName("Damage");
        damageMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        damageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        damage.setItemMeta(damageMeta);

        // Food button
        ItemStack food = new ItemStack(Material.RAW_BEEF);
        ItemMeta foodMeta = food.getItemMeta();
        foodMeta.setDisplayName("Food");
        foodMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        food.setItemMeta(foodMeta);

        optionsMenuButtons = new ItemStack[]{back, changeName, health, damage, food};

    }

    /**
     * Adds an enchantment glow to an item.
     *
     * @param itemStack Item that will glow
     */
    private static void addGlowTo(ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("+" + itemMeta.getDisplayName());
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Removes the enchantment glow from an item.
     *
     * @param itemStack Glowing item
     */
    private static void removeGlowFrom(ItemStack itemStack) {
        if (itemStack.containsEnchantment(Enchantment.LUCK))
            itemStack.removeEnchantment(Enchantment.LUCK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemMeta.getDisplayName().replace("+", ""));
    }

    /**
     * Initializes the buttons for an entity's main menu.
     */
    private static void initializeMainMenuButtons() {
        // Inventory button
        ItemStack openInventory = new ItemStack(Material.CHEST, 1);
        ItemMeta openInventoryMeta = openInventory.getItemMeta();
        openInventoryMeta.setDisplayName("Open Inventory");
        openInventory.setItemMeta(openInventoryMeta);

        // Stats button
        ItemStack openStatsInventory = new ItemStack(Material.BOOK, 1);
        ItemMeta openStatsInventoryMeta = openStatsInventory.getItemMeta();
        openStatsInventoryMeta.setDisplayName("Open Entity Options");
        openStatsInventory.setItemMeta(openStatsInventoryMeta);

        mainMenuButtons = new ItemStack[]{openInventory, openStatsInventory};
    }

    /**
     * Initializes the buttons for an entity's inventory menu.
     */
    private static void initializeInventoryMenuButtons() {
        // Back button
        ItemStack back = new ItemStack(Material.BANNER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("Back");
        back.setItemMeta(backMeta);

        inventoryMenuButtons = new ItemStack[]{back};
    }

    public static ItemStack[] getOptionsMenuButtons() {
        if (optionsMenuButtons == null)
            initializeOptionsMenuButtons();
        return optionsMenuButtons;
    }

    public static ItemStack[] getMainMenuButtons() {
        if (mainMenuButtons == null)
            initializeMainMenuButtons();
        return mainMenuButtons;
    }

    public static ItemStack[] getInventoryMenuButtons() {
        if (inventoryMenuButtons == null)
            initializeInventoryMenuButtons();
        return inventoryMenuButtons;
    }

    /**
     * Opens the entity's inventory so a player can put food in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public static void openTamingGUI(Player player, MSEEntity mseEntity) {
        Inventory tamingGUI = mseEntity.getInventory();
        player.openInventory(tamingGUI);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    /**
     * Opens a main menu from where the entity's inventory and stats can be accessed.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public static void openMainGUI(Player player, MSEEntity mseEntity) {
        Inventory tamedGUI = Bukkit.createInventory(mseEntity, InventoryType.HOPPER, mseEntity.getEntityStats().getEntityType());

        if (mainMenuButtons == null)
            initializeMainMenuButtons();
        for (int i = 0; i < mainMenuButtons.length; i++)
            tamedGUI.setItem(i, mainMenuButtons[i]);

        player.openInventory(tamedGUI);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    /**
     * Opens the entity's inventory so a player can put items in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public static void openInventoryGUI(Player player, MSEEntity mseEntity) {
        Inventory inventoryGUI = mseEntity.getInventory();

        if (inventoryMenuButtons == null)
            initializeInventoryMenuButtons();

        for (int i = 0; i < inventoryMenuButtons.length; i++)
            inventoryGUI.setItem(i, inventoryMenuButtons[i]);


        player.openInventory(inventoryGUI);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    /**
     * Opens a menu where entity options like name or stats can be changed.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     * @param levelUp   Set to true if a level up is accessible
     */
    public static void openOptionsGUI(Player player, MSEEntity mseEntity, boolean levelUp) {
        Inventory statsGUI = Bukkit.createInventory(mseEntity, 9, "Entity Options");

        if (optionsMenuButtons == null)
            initializeOptionsMenuButtons();

        if (levelUp) {
            addGlowTo(optionsMenuButtons[2]);
            addGlowTo(optionsMenuButtons[3]);
            addGlowTo(optionsMenuButtons[4]);

        } else if (optionsMenuButtons[2].containsEnchantment(Enchantment.LUCK)) {
            removeGlowFrom(optionsMenuButtons[2]);
            removeGlowFrom(optionsMenuButtons[3]);
            removeGlowFrom(optionsMenuButtons[4]);
        }

        for (int i = 0; i < optionsMenuButtons.length; i++)
            statsGUI.setItem(i, optionsMenuButtons[i]);

        player.openInventory(statsGUI);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    public static void closeTamingInventoriesOf(MSEEntity mseEntity, Player... players) {

        for (Player player : players) {
            if (player.getOpenInventory().getTopInventory().getHolder().equals(mseEntity))
                Bukkit.getScheduler().runTask(ThisPlugin.getInstance(), () -> player.closeInventory());
        }

        // Check if another item than the back button is placed here
        Inventory inventory = mseEntity.getInventory();
        ItemStack itemAtBackButtonSlot = inventory.getItem(0);
        if (itemAtBackButtonSlot == null)
            return;
        if (itemAtBackButtonSlot.getType() != Material.AIR) {
            if (itemAtBackButtonSlot.getType() != InventoryGUI.getInventoryMenuButtons()[0].getType()
                    || !itemAtBackButtonSlot.getItemMeta().hasDisplayName()
                    || !itemAtBackButtonSlot.getItemMeta().getDisplayName().equals(InventoryGUI.getInventoryMenuButtons()[0].getItemMeta().getDisplayName())) {
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                        inventory.setItem(i, itemAtBackButtonSlot);
                        inventory.setItem(0, new ItemStack(Material.AIR));
                        return;
                    }
                }
            }
        }
        // No free space found - drop item
        Location location = mseEntity.getTamingHandler().getLocation();
        Bukkit.getScheduler().runTask(ThisPlugin.getInstance(), () -> location.getWorld().dropItem(location, itemAtBackButtonSlot));
        inventory.setItem(0, new ItemStack(Material.AIR));

    }

}
