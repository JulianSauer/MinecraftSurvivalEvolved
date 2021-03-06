package de.juliansauer.minecraftsurvivalevolved.entities.mseentities.player;

import de.juliansauer.minecraftsurvivalevolved.entities.UnconsciousnessTimer;
import de.juliansauer.minecraftsurvivalevolved.entities.containers.AttributesContainer;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.AttributedEntity;
import de.juliansauer.minecraftsurvivalevolved.entities.mseentities.Unconsciousable;
import de.juliansauer.minecraftsurvivalevolved.tribes.Rank;
import de.juliansauer.minecraftsurvivalevolved.tribes.RankPermission;
import de.juliansauer.minecraftsurvivalevolved.tribes.Tribe;
import de.juliansauer.minecraftsurvivalevolved.tribes.TribeMember;
import net.minecraft.server.v1_9_R1.EntityLiving;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Wrapper class for players.
 */
public class MSEPlayer extends AttributesContainer implements Unconsciousable, AttributedEntity, TribeMember {

    private boolean initialized;

    Player player;

    UnconsciousnessTimer unconsciousnessTimer;

    private Tribe tribe;

    MSEPlayer(Player player) {
        super("Player");
        this.player = player;
        level = 1;
        currentXp = 0;
        initialized = true;
    }

    public void initWithAttributeMap(Map<String, Object> attributes) {
        if (attributes.get("Level") != null)
            level = (int) attributes.get("Level");
        if (attributes.get("CurrentXp") != null)
            currentXp = ((Number) attributes.get("CurrentXp")).floatValue();
        if (attributes.get("Torpidity") != null)
            torpidity = (int) attributes.get("Torpidity");
        if (attributes.get("Unconscious") != null)
            unconscious = (boolean) attributes.get("Unconscious");
        initialized = true;
    }

    public Map<String, Object> getAttributesMap() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("Name", getName());
        attributes.put("Level", level);
        attributes.put("CurrentXp", currentXp);
        attributes.put("Torpidity", torpidity);
        attributes.put("Unconscious", unconscious);
        return attributes;
    }

    @Override
    public AttributedEntity getAttributedEntity() {
        return this;
    }

    @Override
    public UUID getUniqueID() {
        return player.getUniqueId();
    }

    @Override
    public float getHealth() {
        return (float) player.getHealth();
    }

    @Override
    public float getSpeed() {
        return player.getWalkSpeed();
    }

    @Override
    public int getFood() {
        return player.getFoodLevel();
    }

    @Override
    public void setFood(int food) {
        player.setFoodLevel(food);
    }

    @Override
    public int getMaxFood() {
        return 30;
    }

    @Override
    public int getFoodDepletion() {
        return 0;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getDefaultName() {
        return player.getName();
    }

    @Override
    public String getEntityType() {
        return "Player";
    }

    @Override
    public void eatAnimation() {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public UnconsciousnessTimer getUnconsciousnessTimer() {
        return unconsciousnessTimer;
    }

    @Override
    public void setUnconsciousnessTimer(UnconsciousnessTimer unconsciousnessTimer) {
        this.unconsciousnessTimer = unconsciousnessTimer;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public EntityLiving getHandle() {
        if (player instanceof CraftPlayer)
            return ((CraftPlayer) player).getHandle();
        return null;
    }

    // TribeMember

    @Override
    public boolean hasTribe() {
        return tribe != null;
    }

    @Override
    public boolean isAllowedTo(RankPermission permission) {
        return Rank.rankIsEqualOrHigher(tribe.getRankOfMember(player), tribe.getRankFor(permission));
    }

    @Override
    public Tribe getTribe() {
        return tribe;
    }

    @Override
    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }

    @Override
    public Rank getRank() {
        return tribe.getRankOfMember(player);
    }

    @Override
    public void setRank(Rank newRank) {
        tribe.setRankOf(player, newRank);
    }

}
