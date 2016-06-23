package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import de.julianSauer.minecraftSurvivalEvolved.entities.pathfinders.PathfinderGoalSpiderMeleeAttack;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;

public class MSESpider extends EntitySpider implements MSEEntity {

    private EntityStats entityStats;

    private TamingHandler tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathFinderHandler pathFinderHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    private String entityType;

    public MSESpider(World world) {
        super(world);
        entityType = getName();

        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
        entityStats = new EntityStats(this);
        movementHandler = new RidingHandler(this);
        pathFinderHandler = new PathFinderHandlerMonster(this);
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
        pathFinderHandler.initWith(data);
    }

    @Override
    public void b(NBTTagCompound data) {
        super.b(data);
        tamingHandler.saveData(data);
        entityStats.saveData(data);
        pathFinderHandler.saveData(data);
        data.setBoolean("MSEInitialized", true);
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
        return new PathfinderGoalSpiderMeleeAttack(this);
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld().getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    public org.bukkit.entity.Entity getCraftEntity() {
        return CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this);
    }

    @Override
    public net.minecraft.server.v1_9_R1.EntityInsentient getHandle() {
        return this;
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
        return pathFinderHandler;
    }

    @Override
    public void setPassiveGoals() {
        pathFinderHandler.setPassiveGoals();
    }

    @Override
    public void setNeutralGoals() {
        pathFinderHandler.setNeutralGoals();
    }

    @Override
    public void setAggressiveGoals() {
        pathFinderHandler.setAggressiveGoals();
    }

    @Override
    public void setWandering(boolean wandering) {
        pathFinderHandler.setWandering(wandering);
    }

    @Override
    public void toggleFollowing(EntityPlayer player) {
        pathFinderHandler.toggleFollowing(player);
    }

    @Override
    public EntityPlayer getFollowingPlayer() {
        return pathFinderHandler.getFollowingPlayer();
    }

    @Override
    public boolean isFollowing() {
        return pathFinderHandler.isFollowing();
    }

}
