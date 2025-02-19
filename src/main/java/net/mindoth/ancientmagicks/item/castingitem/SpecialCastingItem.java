package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class SpecialCastingItem extends CastingItem {

    public SpecialCastingItem(Properties pProperties) {
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
            SpellItem spell = SpecialCastingItem.getStoredSpell(stack) != null ? SpecialCastingItem.getStoredSpell(stack) : null;
            Item vessel = stack.getItem();
            if ( spell != null && !player.getCooldowns().isOnCooldown(vessel) ) {
                //This needs to be double-layered if-statements so that the else statement below works properly
                if ( vessel instanceof WandItem ) {
                    if ( !player.getCooldowns().isOnCooldown(spell) ) player.startUsingItem(hand);
                }
                else player.startUsingItem(hand);
            }
        }
        return result;
    }

    @Nullable
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
