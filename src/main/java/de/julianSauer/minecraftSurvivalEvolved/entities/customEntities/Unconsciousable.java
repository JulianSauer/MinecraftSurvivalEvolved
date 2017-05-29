package de.julianSauer.minecraftSurvivalEvolved.entities.customEntities;

import de.julianSauer.minecraftSurvivalEvolved.entities.EntityAttributes;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimer;
import de.julianSauer.minecraftSurvivalEvolved.entities.UnconsciousnessTimerHuman;
import de.julianSauer.minecraftSurvivalEvolved.main.MSEMain;
import net.minecraft.server.v1_9_R1.EntityInsentient;

/**
 * Basic functionality of an entity that can be knocked out.
 */
public interface Unconsciousable<T extends Unconsciousable> {

    /**
     * Increases the torpidity and updates the consciousness of an entity.
     */
    default void increaseTorpidityBy(int torpidityIncrease) {
        getEntityAttributes().setTorpidity(getEntityAttributes().getTorpidity() + torpidityIncrease);
        if (getEntityAttributes().getTorpidity() > getEntityAttributes().getMaxTorpidity())
            getEntityAttributes().setTorpidity(getEntityAttributes().getMaxTorpidity());
        updateConsciousness();
    }

    default void decreaseTorpidityBy(int torpidityDecrease) {
        getEntityAttributes().setTorpidity(getEntityAttributes().getTorpidity() - torpidityDecrease);
        if (getEntityAttributes().getTorpidity() < 0)
            getEntityAttributes().setTorpidity(0);
        updateConsciousness();
    }

    void feedNarcotics();

    EntityAttributes getEntityAttributes();

    EntityInsentient getEntity();

    UnconsciousnessTimer getUnconsciousnessTimer();

    void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer);

    /**
     * Updates the consciousness of the entity.
     */
    default void updateConsciousness() {

        if (getEntityAttributes().isUnconscious() && getEntityAttributes().getTorpidity() <= 0) {
            // Wake up

            getEntityAttributes().setUnconscious(false);
            if (getUnconsciousnessTimer() != null && getUnconsciousnessTimer().isThreadCurrentlyRunning())
                getUnconsciousnessTimer().cancel();

        } else if ((!getEntityAttributes().isUnconscious() && getEntityAttributes().getFortitude() != null && getEntityAttributes().getTorpidity() >= getEntityAttributes().getFortitude())) {
            // Fall asleep
            getEntityAttributes().setUnconscious(true);
            setUnconsciousnessTimer(new UnconsciousnessTimerHuman(getEntity()));
            getUnconsciousnessTimer().runTaskTimer(MSEMain.getInstance(), 0, getUnconsciousnessTimer().getUnconsciousnessUpdateInterval());
        }

    }

}
