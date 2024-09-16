package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketItemActivationAnimation;
import net.mindoth.ancientmagicks.network.PacketUpdateKnownSpells;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SpellTabletItem extends RuneItem {
    public boolean isChannel;
    public int cooldown;

    public SpellTabletItem(Properties pProperties, boolean isChannel, int cooldown) {
        super(pProperties);
        this.isChannel = isChannel;
        this.cooldown = cooldown;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( ColorRuneItem.CURRENT_COMBO_MAP.containsKey(this) ) {
            if ( ClientSpellData.isSpellKnown(this) || Minecraft.getInstance().player.isCreative() ) {
                StringBuilder tooltipString = new StringBuilder();
                List<ColorRuneItem> list = ColorRuneItem.stringListToActualList(ColorRuneItem.CURRENT_COMBO_MAP.get(this).toString());
                for ( ColorRuneItem rune : list ) {
                    String color = rune.color + "0" + "\u00A7r";
                    tooltipString.append(color);
                }

                tooltip.add(Component.literal(String.valueOf(tooltipString)));
            }
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public void onDestroyed(ItemEntity entity, DamageSource damageSource) {
        Level level = entity.level();
        if ( level.isClientSide ) return;
        if ( damageSource.type() != entity.damageSources().lava().type() && damageSource.type() != entity.damageSources().onFire().type() ) return;
        int amount = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        float randX = ThreadLocalRandom.current().nextFloat(-0.3F, 0.3F);
        float randZ = ThreadLocalRandom.current().nextFloat(-0.3F, 0.3F);
        ItemStack newStack = new ItemStack(AncientMagicksItems.SPELL_FRAGMENT.get(), amount);
        ItemEntity drop = new ItemEntity(level, entity.position().x, entity.position().y, entity.position().z, newStack);
        drop.setDeltaMovement(randX, 0.3F, randZ);
        drop.setPickUpDelay(40);
        level.addFreshEntity(drop);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack tablet = player.getItemInHand(handIn);
            if ( tablet.getItem() instanceof SpellTabletItem spellTablet && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(spellTablet) ) {
                //player.startUsingItem(handIn);
                if ( player instanceof ServerPlayer serverPlayer && tablet.getItem() instanceof SpellTabletItem ) learnSpell(serverPlayer, tablet);
            }
        }
        return result;
    }

    public static void learnSpell(ServerPlayer serverPlayer, ItemStack stack) {
        SpellTabletItem spellTablet = (SpellTabletItem)stack.getItem();
        final String spellString = ForgeRegistries.ITEMS.getKey(spellTablet).toString();
        serverPlayer.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("am_secretspell", spellString);
            if ( Objects.equals(spell.getKnownSpells(), "") ) {
                spell.setKnownSpells(spellString);
                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), serverPlayer);
                playLearnEffects(serverPlayer, stack);
            }
            else if ( !ClientSpellData.stringListToSpellList(spell.getKnownSpells()).contains(spellTablet) ) {
                spell.setKnownSpells(spell.getKnownSpells() + "," + spellString);
                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), serverPlayer);
                playLearnEffects(serverPlayer, stack);
            }
        });
    }

    public static void playLearnEffects(Player player, ItemStack stack) {
        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 0.75F);
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 1.0F, 0.75F);
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR, SoundSource.PLAYERS, 1.0F, 0.75F);
        if ( player instanceof ServerPlayer ) AncientMagicksNetwork.sendToPlayer(new PacketItemActivationAnimation(stack, player), (ServerPlayer)player);
        stack.shrink(1);
    }

    public static void playItemActivationAnimation(ItemStack itemStack, Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        if ( entity == mc.player ) mc.gameRenderer.displayItemActivation(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player && tablet.getItem() instanceof SpellTabletItem spellTabletItem) {
            CastingItem.doSpell(player, player, tablet, spellTabletItem, getUseDuration(tablet) - timeLeft);
        }
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( owner == null || target == null ) return false;
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == owner || !owner.canAttack(target) || owner.isAlliedTo(target) || (target instanceof TamableAnimal pet && pet.isOwnedBy(owner))
                || (target instanceof Mob mob && isMinionsOwner(owner, mob));
    }

    public static boolean isMinionsOwner(LivingEntity owner, Mob mob) {
        return mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY).equals(owner.getUUID()) && mob.getTarget() != owner;
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity );
    }

    public void summonMinion(Mob minion, LivingEntity owner, Level level) {
        minion.getPersistentData().putUUID(MindControlEffect.NBT_KEY, owner.getUUID());
        minion.getPersistentData().putBoolean("am_is_minion", true);
        minion.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), 1200));
        ForgeEventFactory.onFinalizeSpawn(minion, (ServerLevel)level, level.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
}
