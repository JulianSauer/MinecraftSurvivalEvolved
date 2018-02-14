package de.juliansauer.minecraftsurvivalevolved.entities.mseentities;

import de.juliansauer.minecraftsurvivalevolved.entities.containers.TameableAttributesContainer;
import de.juliansauer.minecraftsurvivalevolved.entities.handlers.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class MSEPigZombie extends EntityPigZombie implements MSEEntity {

    private TameableAttributesContainer<MSEPigZombie> tameableAttributesContainer;

    private TamingHandler<MSEPigZombie> tamingHandler;

    private MiningHandler miningHandler;

    private MovementHandlerInterface movementHandler;

    private PathfinderHandler pathfinderHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    private String entityType;

    private boolean initialized;

    public MSEPigZombie(World world) {
        super(world);
        entityType = getName();

        tameableAttributesContainer = new TameableAttributesContainer<>(this);
        tamingHandler = new TamingHandler<>(this);
        miningHandler = new MiningHandler(this);
        movementHandler = new RidingHandler<>(this);
        pathfinderHandler = new PathfinderHandlerMonster(this);
        pitchWhileTaming = 0;

        initialized = false;
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
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void a(NBTTagCompound data) {
        super.a(data);
        MSEEntity.super.initWith(data);
        initialized = true;
    }

    @Override
    public void b(NBTTagCompound data) {
        super.b(data);
        MSEEntity.super.saveData(data);
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
    public TameableAttributesContainer getTameableAttributesContainer() {
        return tameableAttributesContainer;
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
    public PathfinderGoal getMeleeAttack() {
        return new PathfinderGoalMeleeAttack(this, 1.0D, true);
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld().getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    public EntityInsentient getEntity() {
        return this;
    }

    @Override
    public EntityLiving getHandle() {
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
        playAttackSound(Sound.ENTITY_ZOMBIE_PIG_AMBIENT);
    }

}
