package net.mindoth.ancientmagicks.item.effect;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.mobeffect.MindControlEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.List;

public class MindControlEffectItem extends PotionEffectItem {

    public MindControlEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected int getRenderType() {
        return 3;
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob;
    }

    @Override
    protected List<MobEffect> getEffects(String data) {
        List<MobEffect> effects = Lists.newArrayList();
        effects.add(AncientMagicksEffects.MIND_CONTROL.get());
        return effects;
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        Mob mob = (Mob)((EntityHitResult)result).getEntity();
        addEnchantParticles(mob, 0.15F, 8, stats);
        int amp = Math.max(0, (Mth.floor(stats.get(POWER)) - 1) / 10);
        int life = Mth.floor(stats.get(LIFE) - 100) * 30 + 600;
        mob.getPersistentData().putUUID(MindControlEffect.NBT_KEY_CONTROL, owner.getUUID());
        for ( MobEffect effect : getEffects(data) ) {
            mob.addEffect(new MobEffectInstance(effect, life, amp, false, true));
            //if ( mob instanceof PathfinderMob pthMob ) mob.goalSelector.addGoal(0, new MeleeAttackGoal(pthMob, 1.0F, true));
            mob.setTarget(MindControlEffect.findMindControlTarget(mob, owner, mob.level()));
        }
        return true;
    }
}
