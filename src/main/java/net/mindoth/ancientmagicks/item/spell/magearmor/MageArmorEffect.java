package net.mindoth.ancientmagicks.item.spell.magearmor;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class MageArmorEffect extends MobEffect {
    public MageArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int pAmplifier) {
        if ( living.hasEffect(AncientMagicksEffects.MAGE_ARMOR.get()) && living.getArmorCoverPercentage() > 0 ) {
            living.removeEffect(AncientMagicksEffects.MAGE_ARMOR.get());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void mageArmorDamageResistance(final LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        DamageType type = event.getSource().type();
        final float damageAmount = event.getAmount();
        if ( living.hasEffect(AncientMagicksEffects.MAGE_ARMOR.get())
                && type != living.damageSources().genericKill().type()
                && type != living.damageSources().fellOutOfWorld().type()
                && type != living.damageSources().outOfBorder().type()
                && type != living.damageSources().starve().type()
                && type != living.damageSources().drown().type() ) {
            event.setAmount(damageAmount * 0.5F);
        }
    }
}