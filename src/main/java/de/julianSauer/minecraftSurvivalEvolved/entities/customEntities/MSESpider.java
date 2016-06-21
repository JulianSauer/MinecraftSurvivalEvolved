package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalSpiderMeleeAttack;
import net.minecraft.server.v1_9_R1.EntitySpider;
import net.minecraft.server.v1_9_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public class MSESpider extends EntitySpider implements MSEEntity {

    private EntityStats entityStats;

    private TamingHandler tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathFinderHandler pathFinderHandlerCreature;

    private Inventory inventory;

    private float pitchWhileTaming;

    public MSESpider(World world) {
        super(world);

        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
        entityStats = new EntityStats(this);
        movementHandler = new RidingHandler(this);
        pathFinderHandlerCreature = new PathFinderHandlerCreature(this);
        pitchWhileTaming = 0;
    }

    @Override
    public void g(float sideMot, float forMot) {

        movementHandler.handleMovement(new float[]{sideMot, forMot});

    }

    @Override
    public void callSuperMovement(float[] args) {
        if (args.length == 2)
            super.g(args[0], args[1]);
    }

    @Override
    public float getPitchWhileTaming() {
        return pitchWhileTaming;
    }

    @Override
    public void setPitchWhileTaming(float pitch) {
        this.pitchWhileTaming = pitch;
    }

    @Override
    public Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.createInventory(this, 18, this.getName() + " Inventory");
        return inventory;
    }

    @Override
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
        return new PathfinderGoalSpiderMeleeAttack(this);
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld().getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    public TamingHandler getTamingHandler() {
        return tamingHandler;
    }

    @Override
    public MiningHandler getMiningHandler() {
        return miningHandler;
    }

    @Override
    public PathFinderHandler getPathFinderHandler() {
        return pathFinderHandlerCreature;
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
