package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class NumbPainGlyph extends GlyphItem {
    public NumbPainGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        Entity target = result.getEntity();
        if ( target instanceof LivingEntity living ) {
            living.addEffect(new MobEffectInstance(AncientMagicksEffects.NUMBNESS.get(), 200, 0));
        }
    }
}
