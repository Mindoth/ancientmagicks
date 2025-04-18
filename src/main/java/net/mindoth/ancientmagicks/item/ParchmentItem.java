package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ParchmentItem extends Item {

    public static final String NBT_KEY_SPELL_STRING = "am_spell_string";
    public static final String NBT_KEY_DATA_STRING = "am_encoding_string";
    public static final String NBT_KEY_CODE_STRING = "am_code_string";
    public static final String NBT_KEY_SPELL_NAME = "am_spell_name";
    public static final String NBT_KEY_PAPER_TIER = "am_paper_tier";

    private final int size;
    public int getSize() {
        return this.size;
    }

    public ParchmentItem(Properties pProperties, int size) {
        super(pProperties);
        this.size = size;
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
                    String color = rune.getColor() + "0" + "\u00A7r";
                    stringBuilder.append(color);
                }
                tooltip.add(Component.literal(stringBuilder.toString()));
            }
        }
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_SPELL_STRING) ) {
            CompoundTag tag = stack.getTag();
            List<Item> componentList = Lists.newArrayList();
            for ( String string : List.of(tag.getString(NBT_KEY_SPELL_STRING).split(",")) ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
                componentList.add(item);
            }
            List<String> dataList = List.of(tag.getString(NBT_KEY_DATA_STRING).split(","));
            for ( int i = 0; i < componentList.size(); i++ ) {
                Item item = componentList.get(i);
                String key = "";
                if ( item instanceof SpellFormItem ) key = "tooltip.ancientmagicks.form";
                if ( item instanceof SpellModifierItem ) key = "tooltip.ancientmagicks.modifier";
                if ( item instanceof SpellEffectItem ) key = "tooltip.ancientmagicks.effect";

                if ( item instanceof ComponentItem component && component.isEncodeable() ) {
                    if ( Objects.equals(dataList.get(i), ComponentItem.NBT_KEY_EMPTY) ) {
                        tooltip.add(Component.translatable(key)
                                .append(Component.translatable(item.getDescriptionId()))
                                .append(Component.literal(": "))
                                .append(Component.translatable("tooltip.ancientmagicks.empty"))
                                .withStyle(ChatFormatting.GRAY));
                    }
                    else component.decodeTooltipData(tooltip, dataList.get(i), key, item);
                }
                else tooltip.add(Component.translatable(key).append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
            }
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
