package de.julianSauer.minecraftSurvivalEvolved.entities;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.entities.containers.AttributesContainer;
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
    private AttributesContainer attributesContainer;

    public FoodTimer(T mseEntity) {
        this.mseEntity = mseEntity;
        attributesContainer = mseEntity.getTameableAttributesContainer().getAttributesContainer();
    }

    @Override
    public void run() {

        if (!mseEntity.isAlive())
            this.cancel();

        mseEntity.setFood(mseEntity.getFood() - mseEntity.getFoodDepletion());
        if (mseEntity.isTamed()) // Hunger is updated by taming handler during a taming process
            updateHunger();
        if (mseEntity.getFood() <= 0) {
            mseEntity.damageEntity(DamageSource.GENERIC, 0.5F);
            mseEntity.setFood(0);
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
            if (attributesContainer.getPreferredFood().containsKey(itemMaterial)) {

                int saturation = attributesContainer.getFoodsaturationFor(item.getType().toString());
                if (saturation <= 0)
                    continue;
                if (mseEntity.getFood() + saturation + mseEntity.getFoodDepletion() > mseEntity.getMaxFood())
                    return 0;

                mseEntity.setFood(mseEntity.getFood() + saturation + mseEntity.getFoodDepletion());
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    inventory.setItem(i, new ItemStack(Material.AIR, 0));
                else
                    inventory.setItem(i, item);

                return attributesContainer.getPreferredFood().get(itemMaterial);
            }
        }
        if (!mseEntity.isTamed())
            // Taming bar decreases if entity is not fed continuously
            return attributesContainer.getHighestFoodSaturation() < mseEntity.getMaxFood() - mseEntity.getFood() ? -10 : 0;
        return 0;

    }

}