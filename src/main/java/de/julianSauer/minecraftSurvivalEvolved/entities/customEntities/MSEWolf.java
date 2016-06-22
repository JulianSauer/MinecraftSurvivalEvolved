package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public class MSEWolf extends EntityWolf implements MSEEntity {

    private EntityStats entityStats;

    private TamingHandler tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathFinderHandler pathFinderHandlerCreature;

    private Inventory inventory;

    private float pitchWhileTaming;

    private String entityType;

    public MSEWolf(World world) {
        super(world);
        entityType = getName();

        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
        entityStats = new EntityStats(this);
        movementHandler = new RidingHandler(this);
        pathFinderHandlerCreature = new PathFinderHandlerMonster(this);
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
    public void a(NBTTagCompound data) {
        super.a(data);
        tamingHandler.initWith(data);
        entityStats.initWith(data);
    }

    @Override
    public void b(NBTTagCompound data) {
        super.b(data);
        tamingHandler.saveData(data);
        entityStats.saveData(data);
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
    public String getEntityType() {
        return entityType;
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
        return new PathfinderGoalMeleeAttack(this, 1.0D, true);
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld().getWorld(), this.locX, this.locY, this.locZ);
    }

    public TamingHandler getTamingHandler() {
        return tamingHandler;
    }

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
