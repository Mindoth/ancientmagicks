package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class SpellStorageItem extends CastingItem {

    public SpellStorageItem(Properties pProperties) {
        super(pProperties);
    }

    public static final String TAG_STORED_SPELL = ("stored_spell");

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.getTag() != null && stack.getTag().contains(TAG_STORED_SPELL) ) {
            String itemId = stack.getTag().getString(TAG_STORED_SPELL);
            String modId = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().split(":")[0];
            String itemName = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)).toString();
            tooltip.add(Component.translatable("item." + modId + "." + itemName).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        ItemStack stack = player.getItemInHand(hand);
        if ( !level.isClientSide ) {
            SpellItem spell = SpellStorageItem.getStoredSpell(stack) != null ? SpellStorageItem.getStoredSpell(stack) : null;
            Item vessel = stack.getItem();
            if ( spell != null && !player.getCooldowns().isOnCooldown(vessel) ) {
                if ( vessel == AncientMagicksItems.SPELL_PEARL.get() ) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    Vec3 center = player.getEyePosition();
                    SpellPearlEntity spellPearl = new SpellPearlEntity(level, player, player, spell);
                    spellPearl.setPos(center.x, center.y - 0.2F, center.z);
                    spellPearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, Math.max(0, spellPearl.getSpeed()), 1.0F);
                    level.addFreshEntity(spellPearl);
                    stack.shrink(1);
                    player.getCooldowns().addCooldown(AncientMagicksItems.SPELL_PEARL.get(), 120);
                    result = InteractionResultHolder.success(player.getItemInHand(hand));
                }
                else if ( vessel == AncientMagicksItems.ANCIENT_TABLET.get() && player instanceof ServerPlayer serverPlayer ) SpellItem.learnSpell(serverPlayer, stack);
                else if ( vessel instanceof WandItem ) {
                    if ( !player.getCooldowns().isOnCooldown(spell) ) player.startUsingItem(hand);
                }
                else player.startUsingItem(hand);
            }
        }
        return result;
    }

    public static SpellItem getStoredSpell(ItemStack vessel) {
        if ( vessel.getTag() != null && vessel.getTag().contains(TAG_STORED_SPELL) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(vessel.getTag().getString(TAG_STORED_SPELL)));
            if ( item instanceof SpellItem spell ) return spell;
        }
        return null;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack castingItem, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player && getStoredSpell(castingItem) != null ) {
            SpellItem spell = getStoredSpell(castingItem);
            doSpell(player, player, castingItem, spell, getUseDuration(castingItem) - timeLeft);
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.isEnchanted() || (pStack.getTag() != null && pStack.getTag().contains(TAG_STORED_SPELL));
    }
}
