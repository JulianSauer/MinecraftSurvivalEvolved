package de.julianSauer.minecraftSurvivalEvolved.entities.handlers;

import de.julianSauer.minecraftSurvivalEvolved.entities.customEntities.MSEEntity;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import de.julianSauer.minecraftSurvivalEvolved.utils.Calculator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;

/**
 * Handles custom mining with entities.
 */
public class MiningHandler {

    private MSEEntity mseEntity;

    private Map<Material, Integer> mineableBlocks;

    double damageInPercent;

    public MiningHandler(MSEEntity mseEntity) {
        this.mseEntity = mseEntity;
        mineableBlocks = MSEMain.getConfigHandler().getMineableBlocksFor(mseEntity.getEntityType());
        damageInPercent = 0;
    }

    /**
     * Checks if the entity is allowed to mine a certain material.
     *
     * @param material Material that is going to be checked
     * @return True if the entity can mine it
     */
    public boolean canMineBlock(Material material) {
        return mineableBlocks.keySet().contains(material);
    }

    /**
     * Calculates the time in game ticks, that an entity needs to break a certain block. Based on the damage compared
     * to the possible maximum damage as well es a block durability from config.
     *
     * @param material Target material
     * @return Time in game ticks to break blocks with that material
     */
    public long calculateMiningTimeFor(Material material) {
        if (!canMineBlock(material))
            return -1;

        double damage = mseEntity.getEntityStats().getDamage();
        double maxDamage = mseEntity.getEntityStats().getMaxDamage() * 2;
        damageInPercent = (damage / maxDamage) * 100;
        return (long) (mineableBlocks.get(material) * (100 - damageInPercent) * 0.2);

    }

    /**
     * Tries to mine all blocks in a 3 x 3 x radius. Amount of success depends on entity damage.
     *
     * @param block Block that was mined
     */
    public void mineBlocks(Block block) {
        if (block != null
                && mseEntity.getMiningHandler().canMineBlock(block.getType())) {
            Location location = block.getLocation();
            Material material = block.getType();

            block.breakNaturally();
            location.add(-1, -1, -1);
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    for (int k = 0; k < 3; k++)
                        if (breakBlock(location.clone().add(i, j, k).getBlock(), material))
                            break;

        }
    }

    /**
     * Tries breaking a certain block. Success depends on entity damage.
     *
     * @param block    Target block
     * @param material Material that the block must have to be mined
     * @return True if the probability failed and the process has to be stopped
     */
    private boolean breakBlock(Block block, Material material) {
        if (block.getType() == material) {
            if (Calculator.getRandomInt(101) <= damageInPercent) {
                block.breakNaturally();
                return false;
            } else
                return true;
        }
        return false;
    }

}
