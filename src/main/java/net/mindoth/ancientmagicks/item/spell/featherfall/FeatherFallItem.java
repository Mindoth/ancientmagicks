package net.mindoth.ancientmagicks.item.spell.featherfall;

import net.mindoth.ancientmagicks.event.ManaEvents;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FeatherFallItem extends SpellItem {

    public FeatherFallItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int life = 400;
        float size = 5.0F;

        List<Entity> list = ShadowEvents.getEntitiesAround(caster, size, size, size);
        if ( caster instanceof LivingEntity living ) list.add(living);
        for ( Entity entity : list ) {
            if ( entity instanceof LivingEntity living ) living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, life, 0, false, false));
        }

        state = true;

        if ( state ) {
            ManaEvents.changeMana(owner, -this.manaCost);
            playMagicSound(level, center);
        }

        return state;
    }
}
