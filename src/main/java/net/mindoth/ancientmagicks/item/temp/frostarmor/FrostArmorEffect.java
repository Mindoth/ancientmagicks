package net.mindoth.ancientmagicks.item.temp.frostarmor;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractArmorEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class FrostArmorEffect extends AbstractArmorEffect {

    public FrostArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int amp) {
        super.addAttributeModifiers(living, map, amp);
        List<MobEffectInstance> list = living.getActiveEffects().stream()
                .filter(effect -> effect.getEffect() instanceof AbstractArmorEffect && effect.getEffect() != AncientMagicksEffects.FROST_ARMOR.get()).toList();
        for ( MobEffectInstance effect : list ) living.removeEffect(effect.getEffect());
    }

    @SubscribeEvent
    public static void frostArmorEffect(final LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(AncientMagicksEffects.FROST_ARMOR.get()) ) {
            if ( event.getSource().getEntity() instanceof LivingEntity source ) {
                source.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, false, false));
            }
        }
    }
}
