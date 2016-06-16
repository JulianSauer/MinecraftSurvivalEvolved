package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MovementHandlerInterface;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.SwimmingHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import net.minecraft.server.v1_9_R1.EntitySquid;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MCESquid extends EntitySquid implements MSEEntity {

    private EntityStats entityStats;

    private TamingHandler tamingHandler;

    private MovementHandlerInterface movementHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    public MCESquid(World world) {
        super(world);

        tamingHandler = new TamingHandler(this);
        entityStats = new EntityStats(this);
        movementHandler = new SwimmingHandler(this);
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

    public TamingHandler getTamingHandler() {
        return tamingHandler;
    }

}
