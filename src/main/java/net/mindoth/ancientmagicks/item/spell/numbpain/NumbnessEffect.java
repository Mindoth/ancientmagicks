package net.mindoth.ancientmagicks.item.spell.numbpain;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.network.capabilities.numbnessdamage.NumbnessDamageProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class NumbnessEffect extends MobEffect {

    public NumbnessEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public static void storeDamage(final LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        DamageType type = event.getSource().type();
        final float damageAmount = event.getAmount();
        if ( living.hasEffect(AncientMagicksEffects.NUMBNESS.get()) && damageAmount > 0 && type != living.damageSources().genericKill().type()) {
            living.getCapability(NumbnessDamageProvider.NUMBNESS_DAMAGE).ifPresent(numbnessDamage -> numbnessDamage.setDamage(numbnessDamage.getDamage() + damageAmount));
            event.setAmount(0);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( living.getCapability(NumbnessDamageProvider.NUMBNESS_DAMAGE).isPresent() ) {
            living.getCapability(NumbnessDamageProvider.NUMBNESS_DAMAGE).ifPresent(numbnessDamage -> {
                float damageAmount = numbnessDamage.getDamage();
                if ( damageAmount > 0 ) living.hurt(living.damageSources().generic(), damageAmount);
                numbnessDamage.setDamage(0);
            });
        }
    }
}
