package net.mindoth.ancientmagicks.item.spell.numbpain;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NumbPainItem extends SpellItem {

    public NumbPainItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int life = 200;
        float range = 14.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);
        target.addEffect(new MobEffectInstance(AncientMagicksEffects.NUMBNESS.get(), life, 0));
        state = true;

        if ( state ) {
            addEnchantParticles(target, 255, 255, 255, 0.15F, 8, true);
            playMagicSound(level, center);
        }

        return state;
    }
}
