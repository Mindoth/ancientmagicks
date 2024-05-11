package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RuneItem extends Item {
    public int cooldown;

    public RuneItem(Properties pProperties, int cooldown) {
        super(pProperties.tab(AncientMagicksGroup.RUNIC_ITEMS_TAB));
        this.cooldown = cooldown;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if ( this.cooldown >= 0 ) tooltip.add(new TranslationTextComponent("+" + this.cooldown + " ").withStyle(TextFormatting.BLUE)
                .append(new TranslationTextComponent("tooltip.ancientmagicks.rune_cooldown")));
        else tooltip.add(new TranslationTextComponent(this.cooldown + " ").withStyle(TextFormatting.BLUE)
                .append(new TranslationTextComponent("tooltip.ancientmagicks.rune_cooldown")));

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
    }

    public static void playMagicSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicSummonSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.25F, 2.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.25F, 2.0F);
    }

    public static void playFireShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.25F, 1.0F);
    }

    public static void playWaterShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.DOLPHIN_SPLASH, SoundCategory.PLAYERS, 0.25F, 0.85F);
    }

    public static void playStormShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 0.5F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.35F, 2.0F);
    }

    public static void playEvilShootSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

    public static void playWindSound(World level, Vector3d center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2.0F, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundCategory.PLAYERS, 2.0F, 0.03F);
    }
}
