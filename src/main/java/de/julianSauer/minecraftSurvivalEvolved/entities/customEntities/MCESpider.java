package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityStats;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.MovementHandlerInterface;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.RidingHandler;
import de.julianSauer.minecraftSurvivalEvolved.entities.handlers.TamingHandler;
import net.minecraft.server.v1_9_R1.EntitySpider;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MCESpider extends EntitySpider implements MSEEntity {

    private EntityStats entityStats;

    private TamingHandler tamingHandler;

    private MovementHandlerInterface movementHandler;

    private Inventory inventory;

    private float pitchWhileTaming;

    public MCESpider(World world) {
        super(world);

        tamingHandler = new TamingHandler(this);
        entityStats = new EntityStats(this);
        movementHandler = new RidingHandler(this);
        pitchWhileTaming = 0;
    }

    @Override
    public void g(float sideMot, float forMot) {

        movementHandler.handleMovement(new float[]{sideMot, forMot});

    }

    public void callSuperMovement(float[] args) {
        if (args.length == 2)
            super.g(args[0], args[1]);
    }

    public float getPitchWhileTaming() {
        return pitchWhileTaming;
    }

    public void setPitchWhileTaming(float pitch) {
        this.pitchWhileTaming = pitch;
    }

    public Inventory getInventory() {
        if (inventory == null)
            inventory = Bukkit.createInventory(this, 18, this.getName());
        return inventory;
    }

    public EntityStats getEntityStats() {
        return entityStats;
    }

    public TamingHandler getTamingHandler() {
        return tamingHandler;
    }

}
