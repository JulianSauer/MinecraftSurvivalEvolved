package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.inventory.Inventory;

public class MSESquid extends EntitySquid implements MSEEntity {

    private GeneralBehaviorHandler generalBehaviorHandler;

    private TamingHandler tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathfinderHandler pathfinderHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    private String entityType;

    public MSESquid(World world) {
        super(world);
        entityType = getName();

        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
        generalBehaviorHandler = new GeneralBehaviorHandler(this);
        movementHandler = new SwimmingHandler(this);
        pathfinderHandler = new PathfinderHandlerAnimal(this);
        pitchWhileTaming = 0;
    }

    @Override
    public void n() {
        movementHandler.handleMovement(new float[]{});
    }

    @Override
    public void callSuperMovement(float[] args) {
        super.n();
    }

    @Override
    public void a(NBTTagCompound data) {
        super.a(data);
        tamingHandler.initWith(data);
        generalBehaviorHandler.initWith(data);
        pathfinderHandler.initWith(data);
    }

    @Override
    public void b(NBTTagCompound data) {
        super.b(data);
        tamingHandler.saveData(data);
        generalBehaviorHandler.saveData(data);
        pathfinderHandler.saveData(data);
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

    public GeneralBehaviorHandler getGeneralBehaviorHandler() {
        return generalBehaviorHandler;
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
        return null;
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

    public TamingHandler getTamingHandler() {
        return tamingHandler;
    }

    public MiningHandler getMiningHandler() {
        return miningHandler;
    }

    public PathfinderHandler getPathfinderHandler() {
        return pathfinderHandler;
    }

    @Override
    public void setPassiveGoals() {
        pathfinderHandler.setPassiveGoals();
    }

    @Override
    public void setNeutralGoals() {
        pathfinderHandler.setNeutralGoals();
    }

    @Override
    public void setAggressiveGoals() {
        pathfinderHandler.setAggressiveGoals();
    }

    @Override
    public void setWandering(boolean wandering) {
        pathfinderHandler.setWandering(wandering);
    }

    @Override
    public void toggleFollowing(EntityPlayer player) {
        pathfinderHandler.toggleFollowing(player);
    }

    @Override
    public EntityPlayer getFollowingPlayer() {
        return pathfinderHandler.getFollowingPlayer();
    }

    @Override
    public boolean isFollowing() {
        return pathfinderHandler.isFollowing();
    }

}
