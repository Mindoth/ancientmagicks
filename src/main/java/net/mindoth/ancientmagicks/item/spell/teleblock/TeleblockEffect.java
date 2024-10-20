package net.mindoth.ancientmagicks.item.spell.teleblock;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class TeleblockEffect extends MobEffect {
    public TeleblockEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void preventTeleportation(final EntityTeleportEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        if ( event.getEntity() instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.TELEBLOCK.get()) ) {
            event.setCanceled(true);
        }
    }
}
