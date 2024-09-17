package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.SpellTabletItem;
import net.mindoth.ancientmagicks.item.spell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

public class SpellPearlItem extends CastingItem {

    public SpellPearlItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.getTag() != null && stack.getTag().contains("spell_pearl") ) {
            String itemId = stack.getTag().getString("spell_pearl");
            String modid = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().split(":")[0];
            String itemName = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)).toString();
            tooltip.add(Component.translatable("item." + modid + "." + itemName).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.isEnchanted() || (pStack.getTag() != null && pStack.getTag().contains("spell_pearl"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        ItemStack pearl = player.getItemInHand(hand);
        if ( !level.isClientSide ) {
            if ( pearl.getTag() != null && pearl.getTag().contains("spell_pearl") ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(pearl.getTag().getString("spell_pearl")));
                if ( item instanceof SpellTabletItem spell ) {
                    if ( !player.getCooldowns().isOnCooldown(pearl.getItem()) ) {
                        if ( !player.isCrouching() ) {
                            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                            Vec3 center = player.getEyePosition();
                            SpellPearlEntity spellPearl = new SpellPearlEntity(level, player, player, spell);
                            spellPearl.setPos(center.x, center.y - 0.2F, center.z);
                            spellPearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, Math.max(0, spellPearl.speed), 1.0F);
                            level.addFreshEntity(spellPearl);
                            pearl.shrink(1);
                            player.getCooldowns().addCooldown(AncientMagicksItems.SPELL_PEARL.get(), 120);

                            result = InteractionResultHolder.success(player.getItemInHand(hand));
                        }
                        else player.startUsingItem(hand);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack castingItem, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player && castingItem.getTag() != null && castingItem.getTag().contains("spell_pearl") ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(castingItem.getTag().getString("spell_pearl")));
            if ( item instanceof SpellTabletItem spellTabletItem ) {
                doSpell(player, player, castingItem, spellTabletItem, getUseDuration(castingItem) - timeLeft);
            }
        }
    }
}
