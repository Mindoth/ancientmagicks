package net.mindoth.ancientmagicks.item.spell.alacrity;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AlacrityItem extends SpellItem {

    public AlacrityItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getCooldown() {
        return 1200;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int life = 1200;
        float range = 14.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( target instanceof Player && isAlly(owner, target)) {
            target.addEffect(new MobEffectInstance(AncientMagicksEffects.ALACRITY.get(), life, 0, false, false));
        }

        state = true;

        if ( state ) playMagicSound(level, center);

        return state;
    }
}
