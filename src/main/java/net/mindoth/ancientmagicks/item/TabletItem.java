package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TabletItem extends RuneItem {

    public TabletItem(Properties pProperties, int tier) {
        super(pProperties, tier);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( ColorRuneItem.CURRENT_COMBO_MAP.containsKey(this) ) {
            StringBuilder tooltipString = new StringBuilder();
            List<ColorRuneItem> list = ColorRuneItem.stringListToActualyList(ColorRuneItem.CURRENT_COMBO_MAP.get(this).toString());
            for ( ColorRuneItem rune : list ) {
                String color = rune.color + "0" + "\u00A7r";
                tooltipString.append(color);
            }
            tooltip.add(Component.literal(String.valueOf(tooltipString)));
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    public static boolean isAlly(LivingEntity owner, LivingEntity target) {
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == owner || !target.canAttack(owner) || target.isAlliedTo(owner) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(owner));
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity );
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player && tablet.getItem() instanceof TabletItem tabletItem ) {
            CastingItem.doSpell(player, player, tablet, tabletItem, getUseDuration(tablet) - timeLeft);
            tablet.shrink(1);
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack tablet = player.getItemInHand(handIn);
            if ( tablet.getItem() instanceof TabletItem tabletItem && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(tabletItem) ) {
                if ( player.totalExperience >= tabletItem.tier || player.isCreative() ) player.startUsingItem(handIn);
                else {
                    CastingItem.addCastingCooldown(player, tabletItem, 20);
                    RuneItem.playWhiffSound(level, ShadowEvents.getEntityCenter(player));
                }
            }
        }
        return result;
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
