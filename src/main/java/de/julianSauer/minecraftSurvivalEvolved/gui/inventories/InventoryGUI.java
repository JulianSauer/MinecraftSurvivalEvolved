package de.julianSauer.minecraftSurvivalEvolved.gui.inventories;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.gui.visuals.ScoreboardHandler;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class InventoryGUI {

    private ButtonFactory optionsMenuButtonFactory;
    private ButtonFactory mainMenuButtonFactory;
    private ButtonFactory inventoryMenuButtonFactory;
    private ButtonFactory tamingMenuButtonFactory;
    private ButtonFactory rankViewButtonFactory;
    private ButtonFactory rankEditButtonFactory;

    public InventoryGUI() {
        optionsMenuButtonFactory = new OptionsMenuButtonFactory(this);
        mainMenuButtonFactory = new MainMenuButtonFactory(this);
        inventoryMenuButtonFactory = new InventoryMenuButtonFactory(this);
        tamingMenuButtonFactory = new TamingButtonFactory();
        rankViewButtonFactory = new RankViewButtonFactory();
        rankEditButtonFactory = new RankEditButtonFactory(this);
    }

    public void openRankViewGUI(Player player) {
        openGenericPlayerGUI(player, rankViewButtonFactory, 9, "Current ranks:");
    }

    public void openRankEditGUI(Player player) {
        openGenericPlayerGUI(player, rankEditButtonFactory, 27, "Edit current ranks:");
    }

    /**
     * Opens the entity's inventory so a player can put food in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public void openTamingGUI(Player player, MSEEntity mseEntity) {
        openGenericMSEEntityGUI(player, mseEntity, tamingMenuButtonFactory, mseEntity.getInventory());
    }

    /**
     * Opens a main menu from where the entity's inventory and stats can be accessed.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public void openMainGUI(Player player, MSEEntity mseEntity) {
        Inventory tamedGUI = Bukkit.createInventory(mseEntity, InventoryType.HOPPER, mseEntity.getEntityType());
        openGenericMSEEntityGUI(player, mseEntity, mainMenuButtonFactory, tamedGUI);
    }

    /**
     * Opens the entity's inventory so a player can put items in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public void openInventoryGUI(Player player, MSEEntity mseEntity) {
        openGenericMSEEntityGUI(player, mseEntity, inventoryMenuButtonFactory, mseEntity.getInventory());
    }

    /**
     * Opens a menu where entity options like name or stats can be changed.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     * @param levelUp   Set to true if a level up is accessible
     */
    public void openOptionsGUI(Player player, MSEEntity mseEntity, boolean levelUp) {
        Inventory statsGUI = Bukkit.createInventory(mseEntity, 9, "Entity Options");
        optionsMenuButtonFactory.setGlowing(levelUp);
        openGenericMSEEntityGUI(player, mseEntity, optionsMenuButtonFactory, statsGUI);
    }

    /**
     * Closes taming inventories after an entity is tamed and prepares the entity inventory to become a GUI.
     *
     * @param mseEntity Entity that is the InventoryHolder
     * @param players   Tamers that might be looking at this entity's inventory
     */
    public void closeTamingInventoriesOf(MSEEntity mseEntity, Player... players) {

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
    public boolean mainMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
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
    public boolean optionsMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
        Button button = optionsMenuButtonFactory.getButtons(mseEntity).get(slot);
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
    public boolean inventoryMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
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
    public boolean tamingMenuButtonClicked(int slot, Player player, MSEEntity mseEntity) {
        Button button = tamingMenuButtonFactory.getButtons(mseEntity).get(slot);
        if (button != null) {
            button.onClick(player, mseEntity);
            return true;
        }
        return false;
    }

    /**
     * Maps an inventory click to a button.
     *
     * @param slot   Slot of the clicked button
     * @param player Player that clicked the button
     * @return True if a button was pressed
     */
    public boolean rankViewButtonclicked(int slot, Player player) {
        Button button = rankViewButtonFactory.getButtons(player).get(slot);
        if (button != null) {
            button.onClick(player);
            return true;
        }
        return false;
    }

    /**
     * Maps an inventory click to a button.
     *
     * @param slot   Slot of the clicked button
     * @param player Player that clicked the button
     * @return True if a button was pressed
     */
    public boolean rankEditButtonClicked(int slot, Player player) {
        Button button = rankEditButtonFactory.getButtons(player).get(slot);
        if (button != null) {
            button.onClick(player);
            return true;
        }
        return false;
    }

    /**
     * Opens the inventory as a UI with buttons from the buttonFactory. Used for MSEEntity related UIs.
     *
     * @param player        Player that opened the inventory
     * @param mseEntity     Entity that is associated with the inventory
     * @param buttonFactory Provides the buttons
     * @param inventory     Inventory that will be used as a base for the UI
     */
    private void openGenericMSEEntityGUI(Player player, MSEEntity mseEntity, ButtonFactory buttonFactory, Inventory inventory) {
        Map<Integer, Button> buttons = buttonFactory.getButtons(mseEntity);
        for (int i = 0; i < buttons.size(); i++)
            inventory.setItem(i, buttons.get(i).getButton());

        player.openInventory(inventory);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

    /**
     * Opens the inventory as a UI with buttons from the buttonFactory. Used for player related UIs.
     *
     * @param player        Player that opened the inventory
     * @param buttonFactory Provides the buttons
     * @param inventorySize Size of the inventory
     * @param inventoryName Name of the inventory
     */
    private void openGenericPlayerGUI(Player player, ButtonFactory buttonFactory, int inventorySize, String inventoryName) {
        Map<Integer, Button> buttons = buttonFactory.getButtons(player);
        Inventory inventory = Bukkit.createInventory(null, inventorySize, inventoryName);
        for (int i = 0; i < buttons.size(); i++)
            inventory.setItem(i, buttons.get(i).getButton());

        player.openInventory(inventory);
    }

}
