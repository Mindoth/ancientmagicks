package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RuneItem extends Item {
    public int cooldown;

    public RuneItem(Properties pProperties, int cooldown) {
        super(pProperties);
        this.cooldown = cooldown;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if ( this.cooldown >= 0 ) tooltip.add(Component.literal("+" + this.cooldown + " ").withStyle(ChatFormatting.BLUE)
                .append(Component.translatable("tooltip.ancientmagicks.rune_cooldown")));
        else tooltip.add(Component.literal(this.cooldown + " ").withStyle(ChatFormatting.BLUE)
                .append(Component.translatable("tooltip.ancientmagicks.rune_cooldown")));

        super.appendHoverText(stack, level, tooltip, flagIn);
    }

    public void shootMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
    }

    public static void playMagicSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playMagicSummonSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.25F, 2.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25F, 2.0F);
    }

    public static void playFireShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.25F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.25F, 1.0F);
    }

    public static void playWaterShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.DOLPHIN_SPLASH, SoundSource.PLAYERS, 0.25F, 0.85F);
    }

    public static void playStormShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 1.0F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.35F, 2.0F);
    }

    public static void playEvilShootSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    public static void playWindSound(Level level, Vec3 center) {
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2.0F, 0.02F);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.HORSE_BREATHE, SoundSource.PLAYERS, 2.0F, 0.03F);
    }
}
