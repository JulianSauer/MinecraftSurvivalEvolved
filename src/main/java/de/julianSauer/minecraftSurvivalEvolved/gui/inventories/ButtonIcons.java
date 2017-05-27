package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.tribes.Rank;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;
import java.util.List;

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

    private ItemStack createItemStackWithoutFlags(String name, List<String> lore, Material material) {
        ItemStack itemStack = createItemStackWithoutFlags(name, material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack changeColorOfLeatherArmor(ItemStack leather, Color color) {
        LeatherArmorMeta meta = ((LeatherArmorMeta) leather.getItemMeta());
        meta.setColor(color);
        leather.setItemMeta(meta);
        return leather;
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

    public ItemStack getRankChangeButton(Rank rank) {
        return createItemStackWithoutFlags("Change ranks:", Collections.singletonList(rank.toString()), Material.LEATHER_HELMET);
    }

    public ItemStack getRankRecruitmentButton(Rank rank) {
        return createItemStackWithoutFlags("Recruit members:", Collections.singletonList(rank.toString()), Material.LEATHER_HELMET);
    }

    public ItemStack getRankDischargeButton(Rank rank) {
        return createItemStackWithoutFlags("Discharge members:", Collections.singletonList(rank.toString()), Material.LEATHER_HELMET);
    }

    public ItemStack getRankPromotingButton(Rank rank) {
        return createItemStackWithoutFlags("Promote members:", Collections.singletonList(rank.toString()), Material.LEATHER_HELMET);
    }

    public ItemStack getIncreaseRankButton(Rank rank) {
        ItemStack itemStack = createItemStackWithoutFlags("Increase to:", Collections.singletonList(rank.toString()), Material.LEATHER_HELMET);
        return changeColorOfLeatherArmor(itemStack, Color.GREEN);
    }

    public ItemStack getDecreaseRankButton(Rank rank) {
        ItemStack itemStack = createItemStackWithoutFlags("Decrease to:", Collections.singletonList(rank.toString()), Material.LEATHER_HELMET);
        return changeColorOfLeatherArmor(itemStack, Color.RED);
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
