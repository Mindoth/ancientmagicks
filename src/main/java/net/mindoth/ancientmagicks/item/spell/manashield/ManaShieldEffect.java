package net.mindoth.ancientmagicks.item.spell.manashield;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.event.ManaEvents;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ManaShieldEffect extends MobEffect {
    public ManaShieldEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void manaShieldDamageBlock(final LivingDamageEvent event) {
        if ( !(event.getEntity() instanceof ServerPlayer serverPlayer) ) return;
        DamageType type = event.getSource().type();
        if ( serverPlayer.hasEffect(AncientMagicksEffects.MANA_SHIELD.get())
                && type != serverPlayer.damageSources().genericKill().type()
                && type != serverPlayer.damageSources().fellOutOfWorld().type()
                && type != serverPlayer.damageSources().outOfBorder().type()
                && type != serverPlayer.damageSources().starve().type()
                && type != serverPlayer.damageSources().drown().type() ) {
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                final double damageAmount = event.getAmount() * 10;
                if ( magic.getCurrentMana() >= damageAmount ) {
                    ManaEvents.changeMana(serverPlayer, -damageAmount);
                    event.setAmount(0);
                }
                else if ( magic.getCurrentMana() < damageAmount ) serverPlayer.removeEffect(AncientMagicksEffects.MANA_SHIELD.get());;
            });
        }
    }
}
