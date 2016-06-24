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

    private ButtonFactory optionsMenuButtonFactory;
    private ButtonFactory mainMenuButtonFactory;
    private ButtonFactory inventoryMenuButtonFactory;
    private ButtonFactory tamingMenuButtonFactory;

    public InventoryGUI() {
        optionsMenuButtonFactory = new OptionsMenuButtonFactory(this);
        mainMenuButtonFactory = new MainMenuButtonFactory(this);
        inventoryMenuButtonFactory = new InventoryMenuButtonFactory(this);
        tamingMenuButtonFactory = new TamingButtonFactory();
    }

    /**
     * Opens the entity's inventory so a player can put food in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public void openTamingGUI(Player player, MSEEntity mseEntity) {
        openGenericGUI(player, mseEntity, tamingMenuButtonFactory, mseEntity.getInventory());
    }

    /**
     * Opens a main menu from where the entity's inventory and stats can be accessed.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public void openMainGUI(Player player, MSEEntity mseEntity) {
        Inventory tamedGUI = Bukkit.createInventory(mseEntity, InventoryType.HOPPER, mseEntity.getEntityType());
        openGenericGUI(player, mseEntity, mainMenuButtonFactory, tamedGUI);
    }

    /**
     * Opens the entity's inventory so a player can put items in it.
     *
     * @param player    Player that will see the GUI
     * @param mseEntity InventoryHolder
     */
    public void openInventoryGUI(Player player, MSEEntity mseEntity) {
        openGenericGUI(player, mseEntity, inventoryMenuButtonFactory, mseEntity.getInventory());
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
        openGenericGUI(player, mseEntity, optionsMenuButtonFactory, statsGUI);
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
     * Opens the inventory as a UI with buttons from the buttonFactory.
     *
     * @param player        Player that opened the inventory
     * @param mseEntity     Entity that is associated with the inventory
     * @param buttonFactory Provides the buttons
     * @param inventory     Inventory that will be used as a base for the UI
     */
    private void openGenericGUI(Player player, MSEEntity mseEntity, ButtonFactory buttonFactory, Inventory inventory) {
        Map<Integer, Button> buttons = buttonFactory.getButtons(mseEntity);
        for (int i = 0; i < buttons.size(); i++)
            inventory.setItem(i, buttons.get(i).getButton());

        player.openInventory(inventory);
        ScoreboardHandler.addPlayer(mseEntity, player);
    }

}
