package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonIcons {

    private ItemStack createItemWith(String name, Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createItemStackWithoutFlags(String name, Material material) {
        ItemStack itemStack = createItemWith(name, material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getOpenInventoryButton() {
        return createItemWith("Open Inventory", Material.CHEST);
    }

    public ItemStack getOpenOptionsButton() {
        return createItemWith("Open Entity Options", Material.BOOK);
    }

    public ItemStack getBackButton() {
        return createItemWith("Back", Material.BANNER);
    }

    public ItemStack getChangeNameButton() {
        return createItemWith("Change Name", Material.NAME_TAG);
    }

    public ItemStack getPassiveModeButton() {
        return createItemWith("Currently Passive", Material.WOOD_SPADE);
    }

    public ItemStack getNeutralModeButton() {
        return createItemWith("Currently Neutral", Material.IRON_AXE);
    }

    public ItemStack getAggressiveModeButton() {
        return createItemWith("Currently Aggressive", Material.DIAMOND_SWORD);
    }

    public ItemStack getIncreaseHealthButton() {
        return createItemStackWithoutFlags("Health", Material.NETHERRACK);
    }

    public ItemStack getIncreaseDamageButton() {
        return createItemStackWithoutFlags("Damage", Material.IRON_SWORD);
    }

    public ItemStack getIncreaseFoodButton() {
        return createItemStackWithoutFlags("Food", Material.RAW_BEEF);
    }

    public ItemStack getForceFeedButton() {
        return createItemWith("Feed Narcotics", Material.LEASH);
    }

    public ItemStack getFollowingButton() {
        return createItemWith("Follow", Material.LEASH);
    }

    public ItemStack getFollowingPlayerButton(String player) {
        return createItemWith("Following: " + player, Material.LEASH);
    }

    /**
     * Adds an enchantment glow to an item.
     *
     * @param itemStack Item that will glow
     * @return The glowing button
     */
    public ItemStack makeGlow(ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("+" + itemMeta.getDisplayName());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Removes the enchantment glow from an item.
     *
     * @param itemStack Glowing item
     * @return The normal button
     */
    public ItemStack stopGlow(ItemStack itemStack) {
        if (itemStack.containsEnchantment(Enchantment.LUCK))
            itemStack.removeEnchantment(Enchantment.LUCK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemMeta.getDisplayName().replace("+", ""));
        return itemStack;
    }

    /**
     * Compares an item with a button icon.
     *
     * @param item      Item that could be a button
     * @param reference Icon reference
     * @return True if the item is a button
     */
    public boolean isButtonIcon(ItemStack item, ItemStack reference) {
        return item != null
                && item.getType() == reference.getType()
                && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName().equals(reference.getItemMeta().getDisplayName());
    }

}
