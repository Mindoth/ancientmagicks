package net.mindoth.ancientmagicks.item.effect;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;

public class PotionEffectItem extends EntityTargetEffect {

    public PotionEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result, String data) {
        return result instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity
                && allyFilter(owner, entityHitResult.getEntity(), isHarmful(data)) && mobTypeFilter(entityHitResult.getEntity());
    }

    protected List<MobEffect> getEffects(String data) {
        List<MobEffect> effects = Lists.newArrayList();
        return effects;
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        LivingEntity target = (LivingEntity)((EntityHitResult)result).getEntity();
        addEnchantParticles(target, 0.15F, 8, stats);
        int amp = Mth.floor(Math.max(0, (stats.get(POWER) - 1)));
        int life = 600 * Mth.floor(stats.get(LIFE));
        for ( MobEffect effect : getEffects(data) ) {
            if ( effect.isInstantenous() ) {
                life = 1;
                amp = Mth.floor(Math.max(0, (stats.get(POWER) - 1) / 4));
                target.addEffect(new MobEffectInstance(effect, life, amp));
            }
            else target.addEffect(new MobEffectInstance(effect, life, amp, false, !effect.isBeneficial()));
        }
        return true;
    }
}
