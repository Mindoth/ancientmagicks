package net.mindoth.ancientmagicks.item.spell.teleblock;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TeleblockItem extends SpellItem {

    public TeleblockItem(Properties pProperties, int spellLevel) {
        super(pProperties, spellLevel);
    }

    @Override
    public int getCooldown() {
        return 800;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int life = 400;
        float range = 32.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, range, 0.5F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        target.addEffect(new MobEffectInstance(AncientMagicksEffects.TELEBLOCK.get(), life));
        ShadowEvents.summonParticleLine(ParticleTypes.ENTITY_EFFECT, caster, ShadowEvents.getEntityCenter(caster), target.getEyePosition(),
                0, 1, 85F / 255F, 1, 1);
        state = true;

        if ( state ) playMagicSound(level, center);

        return state;
    }
}
