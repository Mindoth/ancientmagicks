package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientSpell;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity() instanceof ServerPlayer player ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), player);
            player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                CompoundTag tag = new CompoundTag();
                if ( spell.getSpell() != null ) tag.putString("am_spell", spell.getSpell());
                else spell.setSpell("minecraft:air");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientSpell(tag), player);
            });
        }
    }

    @SubscribeEvent
    public static void hideWithGhostwalk(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) event.modifyVisibility(0);
    }

    @SubscribeEvent
    public static void onLivingFall(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        if ( !living.hasEffect(AncientMagicksEffects.SPOOK.get()) || event.getSource().type() != living.damageSources().fall().type() ) return;
        if ( event.getAmount() < living.getHealth() ) event.setCanceled(true);
    }
}
