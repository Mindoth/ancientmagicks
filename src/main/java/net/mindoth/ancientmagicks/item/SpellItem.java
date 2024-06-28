package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SpellItem extends RuneItem {

    public SpellItem(Properties pProperties, int tier) {
        super(pProperties, tier);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( !Screen.hasShiftDown() ) tooltip.add(Component.translatable("tooltip.ancientmagicks.shift"));
        if ( Screen.hasShiftDown() && ColorRuneItem.CURRENT_COMBO_MAP.containsKey(this) ) {
            tooltip.add(Component.literal(ColorRuneItem.CURRENT_COMBO_MAP.get(this).toString()).withStyle(ChatFormatting.GRAY));
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
}
