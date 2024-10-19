package net.mindoth.ancientmagicks.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellPearlItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientSpell;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity() instanceof ServerPlayer player ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), player);
            player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                CompoundTag tag = new CompoundTag();
                if ( spell.getCurrentSpell() != null ) tag.putString("am_spell", spell.getCurrentSpell());
                else spell.setCurrentSpell("minecraft:air");
                if ( spell.getKnownSpells() != null ) tag.putString("am_known_spells", spell.getKnownSpells());
                else spell.setKnownSpells("");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientSpell(tag), player);
            });
        }
    }

    @SubscribeEvent
    public static void hideWithGhostwalk(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) event.modifyVisibility(0);
    }

    @SubscribeEvent
    public static void onLivingFallSpook(LivingFallEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) {
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
        if ( !CastingItem.getHeldCastingItem(player).isEmpty() || !CastingItem.getHeldTabletItem(player).isEmpty() ) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onStopChannellingSpell(final LivingEntityUseItemEvent.Stop event) {
        if ( event.getEntity() instanceof Player player ) {
            if ( !player.level().isClientSide ) {
                ItemStack stack = event.getItem();
                Item castingItem = stack.getItem();
                if ( castingItem instanceof SpellPearlItem spellPearlItem ) {
                    if ( stack.getTag() != null && stack.getTag().contains("spell_pearl") ) {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.getTag().getString("spell_pearl")));
                        if ( item instanceof SpellItem spellItem) {
                            player.getCooldowns().addCooldown(spellItem, spellItem.getCooldown());
                            if ( !player.isCreative() ) stack.shrink(1);
                        }
                    }
                }
                else if ( castingItem instanceof CastingItem ) {
                    player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getCurrentSpell()));
                        if ( item instanceof SpellItem spellItem) {
                            player.getCooldowns().addCooldown(spellItem, spellItem.getCooldown());
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        for ( SpellItem spell : AncientMagicks.SPELL_LIST ) {
            if ( spell.spellTier <= 3 ) {
                if ( event.getType() == VillagerProfession.LIBRARIAN ) {
                    Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                    ItemStack stack = new ItemStack(spell, 1);

                    for ( int i = 3; i < 5; i++ ) {
                        trades.get(i).add((trader, rand) -> new MerchantOffer(
                                new ItemStack(Items.EMERALD, 16 * spell.spellTier),
                                stack, 1, 30, 0.05F));
                    }
                }
            }
        }
    }
}
