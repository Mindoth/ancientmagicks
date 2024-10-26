package net.mindoth.ancientmagicks.item.spell.frostarmor;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractArmorEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class FrostArmorEffect extends AbstractArmorEffect {

    public FrostArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void frostArmorEffect(final LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(AncientMagicksEffects.FROST_ARMOR.get()) ) {
            if ( event.getSource().getEntity() instanceof LivingEntity source && source.getTicksFrozen() < 160 ) source.setTicksFrozen(160);
        }
    }
}
