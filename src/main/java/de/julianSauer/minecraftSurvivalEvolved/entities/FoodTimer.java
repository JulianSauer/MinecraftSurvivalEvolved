package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles autonomous eating of an entity
 */
public class FoodTimer<T extends EntityInsentient & MSEEntity> extends BukkitRunnable {

    private T mseEntity;
    private TameableEntityAttributes tameableEntityAttributes;
    private BaseAttributes baseAttributes;

    public FoodTimer(T mseEntity) {
        this.mseEntity = mseEntity;
        tameableEntityAttributes = mseEntity.getTameableEntityAttributes();
        baseAttributes = tameableEntityAttributes.getBaseAttributes();
    }

    @Override
    public void run() {

        if (!mseEntity.isAlive())
            this.cancel();

        tameableEntityAttributes.setCurrentFoodValue(tameableEntityAttributes.getCurrentFoodValue() - tameableEntityAttributes.getFoodDepletion());
        if (tameableEntityAttributes.isTamed()) // Hunger is updated by taming handler during a taming process
            updateHunger();
        if (tameableEntityAttributes.getCurrentFoodValue() <= 0) {
            mseEntity.damageEntity(DamageSource.GENERIC, 0.5F);
            tameableEntityAttributes.setCurrentFoodValue(0);
        } else if (Calculator.applyProbability(30))
            mseEntity.setHealth(mseEntity.getHealth() + 0.5F);

    }

    /**
     * Tries to eat food from the inventory.
     *
     * @return Saturation of the food that was eaten.
     */
    public int updateHunger() {

        Inventory inventory = mseEntity.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null)
                continue;

            Material itemMaterial = item.getType();
            if (baseAttributes.getPreferredFood().containsKey(itemMaterial)) {

                int saturation = baseAttributes.getFoodsaturationFor(item.getType().toString());
                if (saturation <= 0)
                    continue;
                if (tameableEntityAttributes.getCurrentFoodValue() + saturation + tameableEntityAttributes.getFoodDepletion() > baseAttributes.getMaxFoodValue())
                    return 0;

                tameableEntityAttributes.setCurrentFoodValue(tameableEntityAttributes.getCurrentFoodValue() + saturation + tameableEntityAttributes.getFoodDepletion());
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);

                return baseAttributes.getPreferredFood().get(itemMaterial);
            }
        }
        if (!tameableEntityAttributes.isTamed())
            // Taming bar decreases if entity is not fed continuously
            return baseAttributes.getHighestFoodSaturation() < baseAttributes.getMaxFoodValue() - tameableEntityAttributes.getCurrentFoodValue() ? -10 : 0;
        return 0;

    }

}