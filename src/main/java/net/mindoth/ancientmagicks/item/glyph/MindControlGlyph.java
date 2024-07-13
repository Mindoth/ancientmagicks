package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.effect.MindControlEffect;
import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class MindControlGlyph extends GlyphItem {
    public MindControlGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        Entity target = result.getEntity();
        if ( target instanceof Mob mob && !isAlly(owner, mob) ) {
            mob.getPersistentData().putUUID(MindControlEffect.NBT_KEY, owner.getUUID());
            mob.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), 600, 0));
            mob.setTarget(MindControlEffect.findMindControlTarget(mob, owner, mob.level()));
        }
    }
}
