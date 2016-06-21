package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import net.minecraft.server.v1_9_R1.EntitySquid;
import net.minecraft.server.v1_9_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MSESquid extends EntitySquid implements MSEEntity {

    private EntityStats entityStats;

    private TamingHandler tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathFinderHandler pathFinderHandlerCreature;

    private Inventory inventory;

    private float pitchWhileTaming;

    public MSESquid(World world) {
        super(world);

        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
        entityStats = new EntityStats(this);
        movementHandler = new SwimmingHandler(this);
        pathFinderHandlerCreature = new PathFinderHandlerAnimal(this);
        pitchWhileTaming = 0;
    }

    @Override
    public void n() {

        movementHandler.handleMovement(new float[]{});

    }

    public void callSuperMovement(float[] args) {
        super.n();
    }

    public float getPitchWhileTaming() {
        return pitchWhileTaming;
    }

    public void setPitchWhileTaming(float pitch) {
        this.pitchWhileTaming = pitch;
    }

    public Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.createInventory(this, 18, this.getName() + " Inventory");
        return inventory;
    }

    public EntityStats getEntityStats() {
        return entityStats;
    }

    @Override
    public PathfinderGoalSelector getGoalSelector() {
        return goalSelector;
    }

    @Override
    public PathfinderGoalSelector getTargetSelector() {
        return targetSelector;
    }

    @Override
    public PathfinderGoalMeleeAttack getMeleeAttack() {
        return null;
    }

    public TamingHandler getTamingHandler() {
        return tamingHandler;
    }

    public MiningHandler getMiningHandler() {
        return miningHandler;
    }

    @Override
    public void setPassiveGoals() {
        pathFinderHandlerCreature.setPassiveGoals();
    }

    @Override
    public void setNeutralGoals() {
        pathFinderHandlerCreature.setNeutralGoals();
    }

    @Override
    public void setAggressiveGoals() {
        pathFinderHandlerCreature.setAggressiveGoals();
    }

    @Override
    public void setWandering(boolean wandering) {
        pathFinderHandlerCreature.setWandering(wandering);
    }


}
