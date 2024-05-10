package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface OwnableEntity {
    @Nullable
    UUID getOwnerUUID();

    @Nullable
    Entity getOwner();
}
