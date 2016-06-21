package de.julianSauer.minecraftSurvivalEvolved.visuals.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.visuals.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class InventoryGUI {

    private static ButtonFactory optionsMenuButtonFactory;
    private static ButtonFactory mainMenuButtonFactory;
    private static ButtonFactory inventoryMenuButtonFactory;
    private static ButtonFactory tamingMenuButtonFactory;

    static {
        optionsMenuButtonFactory = new OptionsMenuButtonFactory();
        mainMenuButtonFactory = new MainMenuButtonFactory();
        inventoryMenuButtonFactory = new InventoryMenuButtonFactory();
        tamingMenuButtonFactory = new TamingButtonFactory();
    }

    /**
     * Opens the entity's inventory so a player can put food in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public static void openTamingGUI(Player player, MSEEntity mseEntity) {
        Inventory tamingGUI = mseEntity.getInventory();

        Map<Integer, Button> buttons = tamingMenuButtonFactory.getButtons(mseEntity);
        for (int i = 0; i < buttons.size(); i++)
            tamingGUI.setItem(i, buttons.get(i).getButton());

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

        Map<Integer, Button> buttons = mainMenuButtonFactory.getButtons(mseEntity);
        for (int i = 0; i < buttons.size(); i++)
            tamedGUI.setItem(i, buttons.get(i).getButton());

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

        Map<Integer, Button> buttons = inventoryMenuButtonFactory.getButtons(mseEntity);
        for (int i = 0; i < buttons.size(); i++)
            inventoryGUI.setItem(i, buttons.get(i).getButton());

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

        optionsMenuButtonFactory.setGlowing(levelUp);
        Map<Integer, Button> buttons = optionsMenuButtonFactory.getButtons(mseEntity);
        for (int i = 0; i < buttons.size(); i++)
            statsGUI.setItem(i, buttons.get(i).getButton());

        player.openInventory(statsGUI);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    /**
     * Closes taming inventories after an entity is tamed and prepares the entity inventory to become a GUI.
     *
     * @param mseEntity Entity that is the InventoryHolder
     * @param players   Tamers that might be looking at this entity's inventory
     */
    public static void closeTamingInventoriesOf(MSEEntity mseEntity, Player... players) {

        if (players == null || mseEntity == null)
            return;
        for (Player player : players) {
            if (player != null && player.getOpenInventory().getTopInventory().getHolder().equals(mseEntity))
                Bukkit.getScheduler().runTask(MSEMain.getInstance(), () -> player.closeInventory());
        }

    }

    /**
     * Maps an inventory click to a button.
     *
     * @param slot      Slot of the clicked button
     * @param player    Player that clicked the button
     * @param mseEntity Entity that the menu belongs to
     * @return True if a button was pressed
     */
    public static boolean mainMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
        Button button = mainMenuButtonFactory.getButtons(mseEntity).get(slot);
        if (button != null) {
            button.onClick(player, mseEntity);
            return true;
        }
        return false;
    }

    /**
     * Maps an inventory click to a button.
     *
     * @param slot      Slot of the clicked button
     * @param player    Player that clicked the button
     * @param mseEntity Entity that the menu belongs to
     * @return True if a button was pressed
     */
    public static boolean optionsMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
        Button button = optionsMenuButtonFactory.getButtons(mseEntity).get(slot);
        if (button != null) {
            button.onClick(player, mseEntity);
            openOptionsGUI(player, mseEntity, true); // Updates the GUI
            return true;
        }
        return false;
    }

    /**
     * Maps an inventory click to a button.
     *
     * @param slot      Slot of the clicked button
     * @param player    Player that clicked the button
     * @param mseEntity Entity that the menu belongs to
     * @return True if a button was pressed
     */
    public static boolean inventoryMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
        Button button = inventoryMenuButtonFactory.getButtons(mseEntity).get(slot);
        if (button != null) {
            button.onClick(player, mseEntity);
            return true;
        }
        return false;
    }

    /**
     * Maps an inventory click to a button.
     *
     * @param slot      Slot of the clicked button
     * @param player    Player that clicked the button
     * @param mseEntity Entity that the menu belongs to
     * @return True if a button was pressed
     */
    public static boolean tamingMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
        Button button = tamingMenuButtonFactory.getButtons(mseEntity).get(slot);
        if (button != null) {
            button.onClick(player, mseEntity);
            return true;
        }
        return false;
    }

}
