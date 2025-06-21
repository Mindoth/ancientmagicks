package net.mindoth.ancientmagicks.mobeffect;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class WitchArmorEffect extends AbstractArmorEffect {

    public WitchArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    /*@Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int amp) {
        super.addAttributeModifiers(living, map, amp);
        List<MobEffectInstance> list = living.getActiveEffects().stream()
                .filter(effect -> effect.getEffect() instanceof AbstractArmorEffect && effect.getEffect() != AncientMagicksEffects.WITCH_ARMOR.get()).toList();
        for ( MobEffectInstance effect : list ) living.removeEffect(effect.getEffect());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void witchArmorDamageBlock(final LivingDamageEvent event) {
        if ( !(event.getEntity() instanceof ServerPlayer serverPlayer) ) return;
        DamageType type = event.getSource().type();
        if ( serverPlayer.hasEffect(AncientMagicksEffects.WITCH_ARMOR.get())
                && type != serverPlayer.damageSources().genericKill().type()
                && type != serverPlayer.damageSources().fellOutOfWorld().type()
                && type != serverPlayer.damageSources().outOfBorder().type()
                && type != serverPlayer.damageSources().starve().type()
                && type != serverPlayer.damageSources().drown().type() ) {
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                final double damageAmount = event.getAmount() * 5;
                if ( magic.getCurrentMana() >= damageAmount ) {
                    MagickEvents.changeMana(serverPlayer, -damageAmount);
                    event.setAmount(0);
                }
                else if ( magic.getCurrentMana() < damageAmount ) serverPlayer.removeEffect(AncientMagicksEffects.WITCH_ARMOR.get());
            });
        }
    }*/
}
