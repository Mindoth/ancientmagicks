package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class PolymorphGlyph extends GlyphItem {
    public PolymorphGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        if ( spell != null ) {
            if ( spell.level() instanceof ServerLevel serverLevel ) {
                if ( result.getEntity() instanceof Mob mob && !(mob instanceof Sheep) ) {
                    Sheep sheep = mob.convertTo(EntityType.SHEEP, false);
                    sheep.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                }
            }
        }
    }

    @Override
    public void onBlockHit(@Nullable AbstractSpell spell, LivingEntity owner, BlockHitResult result) {
    }
}
