package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.castingitem.WandItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMagic;
import net.mindoth.ancientmagicks.network.PacketSyncClientMana;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class MagickEvents {

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
                else changeMana(serverPlayer, serverPlayer.getAttributeValue(AncientMagicksAttributes.MP_MAX.get()));
            });
        }

        //KEEP THIS LAST
        if ( !data.getBoolean(TAG_NOT_FIRST_LOGIN) ) {
            data.putBoolean(TAG_NOT_FIRST_LOGIN, true);
            playerData.put(Player.PERSISTED_NBT_TAG, data);
        }
    }

    @SubscribeEvent
    public static void baseManaRegen(final TickEvent.LevelTickEvent event) {
        if ( event.phase != TickEvent.Phase.END || event.level.isClientSide ) return;
        event.level.players().stream().toList().forEach(player -> {
            final double manaRegen = player.getAttributeValue(AncientMagicksAttributes.MP_REG.get());
            if ( !(player instanceof ServerPlayer serverPlayer ) || player.isRemoved() ) return;
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                final double maxMana = serverPlayer.getAttributeValue(AncientMagicksAttributes.MP_MAX.get());
                final double currentMana = magic.getCurrentMana();
                boolean isCasting = serverPlayer.isUsingItem() && serverPlayer.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof StaffItem;
                if ( currentMana < maxMana ) {
                    if ( player.tickCount % 10 == 0 && !isCasting ) changeMana(player, manaRegen);
                }
                else if ( currentMana > maxMana ) changeMana(player, maxMana - currentMana);
            });
        });
    }

    //ANY CHANGES IN A PLAYER'S MANA SHOULD BE DONE HERE
    public static void changeMana(Player player, double addition) {
        if ( !(player instanceof ServerPlayer serverPlayer ) || player.isRemoved() || (player.isCreative() && addition < 0) ) return;
        serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            final double maxMana = serverPlayer.getAttributeValue(AncientMagicksAttributes.MP_MAX.get());
            final double currentMana = magic.getCurrentMana();
            magic.setCurrentMana(Math.max(0.0D, Math.min(maxMana, currentMana + addition)));
            AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMana(magic.getCurrentMana()), serverPlayer);
        });
    }

    @SubscribeEvent
    public static void onStopChannellingSpell(final LivingEntityUseItemEvent.Stop event) {
        if ( event.getEntity() instanceof Player player ) {
            if ( !player.level().isClientSide ) {
                ItemStack stack = event.getItem();
                Item castingItem = stack.getItem();
                if ( castingItem instanceof StaffItem || castingItem instanceof WandItem ) {
                    player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                        if ( item instanceof SpellItem spell ) {
                            boolean hasAlacrity = player.hasEffect(AncientMagicksEffects.ALACRITY.get());
                            float alacrityBonus = hasAlacrity ? 0.5F : 1.0F;
                            int spellCooldown = (int)(spell.cooldown * alacrityBonus);
                            player.getCooldowns().addCooldown(spell, spellCooldown);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void hideWithGhostwalk(final LivingEvent.LivingVisibilityEvent event) {
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) event.modifyVisibility(0);
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get()) ) event.modifyVisibility(0);
    }

    @SubscribeEvent
    public static void onLivingFallSpook(final LivingFallEvent event) {
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
        if ( !CastingItem.getHeldCastingItem(player).isEmpty() ) event.setCanceled(true);
    }
}
