package net.mindoth.ancientmagicks.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellStorageItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMana;
import net.mindoth.ancientmagicks.network.PacketSyncClientMagic;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.mindoth.ancientmagicks.network.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
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

    public static final String TAG_NOT_FIRST_LOGIN = ("notFirstLogIn");

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        Player player = event.getEntity();
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
        if ( event.getEntity() instanceof ServerPlayer serverPlayer ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), serverPlayer);
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                CompoundTag tag = new CompoundTag();
                if ( magic.getCurrentSpell() != null ) tag.putString("am_spell", magic.getCurrentSpell());
                else magic.setCurrentSpell("minecraft:air");
                if ( magic.getKnownSpells() != null ) tag.putString("am_known_spells", magic.getKnownSpells());
                else magic.setKnownSpells("");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMagic(tag), serverPlayer);
                if ( data.getBoolean(TAG_NOT_FIRST_LOGIN) ) AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMana(magic.getCurrentMana()), serverPlayer);
                else ManaEvents.changeMana(serverPlayer, serverPlayer.getAttributeValue(AncientMagicksAttributes.MAX_MANA.get()));
            });
        }

        //KEEP THIS LAST
        if ( !data.getBoolean(TAG_NOT_FIRST_LOGIN) ) {
            data.putBoolean(TAG_NOT_FIRST_LOGIN, true);
            playerData.put(Player.PERSISTED_NBT_TAG, data);
        }
    }

    @SubscribeEvent
    public static void hideWithGhostwalk(LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) event.modifyVisibility(0);
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get()) ) event.modifyVisibility(0);
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
                if ( castingItem instanceof SpellStorageItem ) {
                    if ( stack.getTag() != null && stack.getTag().contains(SpellStorageItem.TAG_STORED_SPELL) ) {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.getTag().getString(SpellStorageItem.TAG_STORED_SPELL)));
                        if ( item instanceof SpellItem spellItem ) {
                            player.getCooldowns().addCooldown(spellItem, spellItem.cooldown);
                            if ( !player.isCreative() ) stack.shrink(1);
                        }
                    }
                }
                else if ( castingItem instanceof CastingItem ) {
                    player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                        if ( item instanceof SpellItem spellItem ) player.getCooldowns().addCooldown(spellItem, spellItem.cooldown);
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        for ( SpellItem spell : AncientMagicks.SPELL_LIST ) {
            if ( spell.spellTier < 4 ) {
                if ( event.getType() == VillagerProfession.LIBRARIAN ) {
                    Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                    ItemStack stack = new ItemStack(spell, 1);

                    for ( int i = 1; i < 4; i++ ) {
                        if ( spell.spellTier == i ) {
                            trades.get(i + 1).add((trader, rand) -> new MerchantOffer(
                                    new ItemStack(Items.EMERALD, 16 * spell.spellTier),
                                    stack, 1, 16 * spell.spellTier, 0.05F));
                        }
                    }
                }
            }
        }
    }
}
