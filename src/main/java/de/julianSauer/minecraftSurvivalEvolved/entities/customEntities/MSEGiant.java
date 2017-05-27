package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.inventory.Inventory;

public class MSEGiant extends EntityGiantZombie implements MSEEntity {

    private EntityAttributes entityAttributes;

    private TamingHandler tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathfinderHandler pathfinderHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    private String entityType;

    public MSEGiant(World world) {
        super(world);
        entityType = getName();

        entityAttributes = new EntityAttributes(this);
        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
        movementHandler = new RidingHandler(this);
        pathfinderHandler = new PathfinderHandlerMonster(this);
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
        MSEEntity.super.load(data);
    }

    @Override
    public void b(NBTTagCompound data) {
        super.b(data);
        MSEEntity.super.save(data);
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
    public EntityAttributes getEntityAttributes() {
        return entityAttributes;
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
    public PathfinderHandler getPathfinderHandler() {
        return pathfinderHandler;
    }

    @Override
    public void playAttackSound() {
        playAttackSound(Sound.ENTITY_ZOMBIE_AMBIENT);
    }

}
