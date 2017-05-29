package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.TameableEntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class MSESquid extends EntitySquid implements MSEEntity {

    private TameableEntityAttributes tameableEntityAttributes;

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

        tameableEntityAttributes = new TameableEntityAttributes(this);
        tamingHandler = new TamingHandler(this);
        miningHandler = new MiningHandler(this);
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
    public TameableEntityAttributes getTameableEntityAttributes() {
        return tameableEntityAttributes;
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
    public EntityInsentient getEntity() {
        return this;
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
        playAttackSound(Sound.ENTITY_SQUID_AMBIENT);
    }

}
