package net.mindoth.ancientmagicks.item.spell.abstractspell.summon;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface OwnableEntity {
    @Nullable
    UUID getOwnerUUID();

    @Nullable
    Entity getOwner();
}
