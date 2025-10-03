package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capability.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMana;
import net.mindoth.ancientmagicks.registries.ModEffects;
import net.mindoth.ancientmagicks.registries.attribute.ModAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class MagickEvents {

    @SubscribeEvent
    public static void baseRegen(final TickEvent.LevelTickEvent event) {
        if ( event.phase != TickEvent.Phase.END || event.level.isClientSide ) return;
        event.level.players().stream().toList().forEach(player -> {
            if ( !(player instanceof ServerPlayer serverPlayer ) || player.isDeadOrDying() || player.isRemoved() ) return;
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                final double maxMagick = serverPlayer.getAttributeValue(ModAttributes.MAGICK.get());
                final double currentMagick = magic.getCurrentMana();
                final double magickRegen = 6;
                if ( player.tickCount % 120 == 0 ) changeMagick(player, magickRegen);
                if ( currentMagick > maxMagick ) changeMagick(player, maxMagick - currentMagick);
            });
        });
    }

    //ANY CHANGES IN A PLAYER'S RESOURCE SHOULD BE DONE HERE
    public static void changeMagick(Entity entity, double addition) {
        if ( !(entity instanceof ServerPlayer serverPlayer) || serverPlayer.isRemoved() || (serverPlayer.isCreative() && addition < 0) ) return;
        serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            final double maxMagick = serverPlayer.getAttributeValue(ModAttributes.MAGICK.get());
            final double currentMagick = magic.getCurrentMana();
            final double newMagick = Math.max(0.0D, Math.min(maxMagick, currentMagick + addition));
            magic.setCurrentMana(newMagick);
            ModNetwork.sendToPlayer(new PacketSyncClientMana(newMagick), serverPlayer);
        });
    }

    /*@SubscribeEvent
    public static void hideWithInvisibilitySpells(final LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get()) || living.hasEffect(AncientMagicksEffects.PERFECT_INVISIBILITY.get()) ) {
            event.modifyVisibility(0);
        }
    }*/

    @SubscribeEvent
    public static void onLivingFallSpook(final LivingFallEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(ModEffects.FALL_CONTROL.get()) ) {
            if ( calculateFallDamage(living, event.getDistance(), event.getDamageMultiplier()) < living.getHealth() ) {
                event.setCanceled(true);
            }
        }
    }

    //This is here to check if fall damage is lethal in onLivingFallSpook
    protected static int calculateFallDamage(LivingEntity living, float pFallDistance, float pDamageMultiplier) {
        if ( living.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE) ) return 0;
        else {
            MobEffectInstance mobeffectinstance = living.getEffect(MobEffects.JUMP);
            float f = mobeffectinstance == null ? 0.0F : (float)(mobeffectinstance.getAmplifier() + 1);
            return Mth.ceil((pFallDistance - 3.0F - f) * pDamageMultiplier);
        }
    }

    @SubscribeEvent
    public static void disableInteraction(final PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if ( !CastingItem.getHeldCastingItem(player).isEmpty() ) event.setCanceled(true);
    }
}
