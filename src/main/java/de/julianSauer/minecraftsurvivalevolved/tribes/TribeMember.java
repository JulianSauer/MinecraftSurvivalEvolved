package de.juliansauer.minecraftsurvivalevolved.tribes;

import java.util.UUID;

public interface TribeMember {

    boolean hasTribe();

    boolean isAllowedTo(RankPermission permission);

    Tribe getTribe();

    void setTribe(Tribe tribe);

    UUID getUniqueID();

    Rank getRank();

    void setRank(Rank newRank);

}
