package net.mindoth.ancientmagicks.item.effect.chaoticpolymorph;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class ChaoticPolymorphEffectItem extends PotionEffectItem {

    public ChaoticPolymorphEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result) {
        return result instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof Mob
                && allyFilter(owner, entityHitResult.getEntity(), isHarmful()) && mobTypeFilter(entityHitResult.getEntity());
    }

    @Override
    protected List<MobEffect> getEffects(String data) {
        List<MobEffect> effects = Lists.newArrayList();
        effects.add(AncientMagicksEffects.CHAOTIC_POLYMORPH.get());
        return effects;
    }
}
