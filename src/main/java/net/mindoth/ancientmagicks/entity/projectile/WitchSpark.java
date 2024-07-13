package net.mindoth.ancientmagicks.entity.projectile;

import net.mindoth.ancientmagicks.item.GlyphItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

import java.util.List;

public class WitchSpark extends AbstractSpell {

    public WitchSpark(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.WITCH_SPARK.get(), level);
    }

    public WitchSpark(EntityType<WitchSpark> entityType, Level level) {
        super(entityType, level);
    }

    public WitchSpark(Level level, LivingEntity owner, Entity caster, List<GlyphItem> glyphList) {
        super(AncientMagicksEntities.WITCH_SPARK.get(), level, owner, caster, glyphList);
    }
}
