package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ParchmentItem extends Item {

    public static final String NBT_KEY_SPELL_STRING = "am_spell_string";
    public static final String NBT_KEY_CODE_STRING = "am_code_string";
    public static final String NBT_KEY_SPELL_NAME = "am_spell_name";
    public static final String NBT_KEY_PAPER_TIER = "am_paper_tier";

    public ParchmentItem(Properties pProperties) {
        super(pProperties);
    }

    //Temp for testing
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = player.getItemInHand(handIn);
            if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_SPELL_STRING) ) {
                SpellValidator.castSpell(stack, serverPlayer, serverPlayer);
            }
        }
        return result;
    }

    public static List<ColorRuneItem> getScrollComboList(ItemStack stack) {
        if ( !(stack.getItem() instanceof ParchmentItem) ) return null;
        if ( !stack.hasTag() || !stack.getTag().contains(NBT_KEY_CODE_STRING) ) return null;
        CompoundTag tag = stack.getTag();
        List<String> codeList = List.of(tag.getString(NBT_KEY_CODE_STRING).split(","));
        List<ColorRuneItem> runes = Lists.newArrayList();
        for ( String string : codeList ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof ColorRuneItem colorRuneItem ) runes.add(colorRuneItem);
        }
        if ( codeList.size() == AncientMagicks.comboSizeCalc() && codeList.size() == codeList.size() ) return runes;
        else return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_CODE_STRING) ) {
            CompoundTag tag = stack.getTag();
            List<String> codeString = List.of(tag.getString(NBT_KEY_CODE_STRING).split(","));
            List<ColorRuneItem> runes = getScrollComboList(stack);
            if ( runes != null && runes.size() == AncientMagicks.comboSizeCalc() && runes.size() == codeString.size() ) {
                StringBuilder stringBuilder = new StringBuilder();
                for ( ColorRuneItem rune : runes ) {
                    String color = rune.color + "0" + "\u00A7r";
                    stringBuilder.append(color);
                }
                tooltip.add(Component.literal(stringBuilder.toString()));
            }
        }
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_SPELL_STRING) ) {
            CompoundTag tag = stack.getTag();
            List<Item> runes = Lists.newArrayList();
            for ( String string : List.of(tag.getString(NBT_KEY_SPELL_STRING).split(",")) ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
                runes.add(item);
            }
            for ( Item item : runes ) {
                if ( item instanceof SpellFormItem) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.form")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
            for ( Item item : runes ) {
                if ( item instanceof SpellItem ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.spell")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
            for ( Item item : runes ) {
                if ( item instanceof SpellModifierItem ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.modifier")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
