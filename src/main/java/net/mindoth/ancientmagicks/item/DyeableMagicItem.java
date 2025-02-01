package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.armor.AncientMagicksArmorMaterials;
import net.mindoth.ancientmagicks.item.armor.ColorableMagicArmorItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public interface DyeableMagicItem extends DyeableLeatherItem {
    String TAG_COLOR = "color";
    String TAG_DISPLAY = "display";
    int WHITE = 16777215;
    int BROWN = 10511680;
    int RED = 12667459;

    @Override
    default boolean hasCustomColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement(TAG_DISPLAY);
        return compoundtag != null && compoundtag.contains(TAG_COLOR, 99);
    }

    @Override
    default int getColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement(TAG_DISPLAY);
        if ( compoundtag != null && compoundtag.contains(TAG_COLOR, 99) ) return compoundtag.getInt(TAG_COLOR);
        else if ( pStack.getItem() instanceof ColorableMagicArmorItem item && item.getMaterial() == AncientMagicksArmorMaterials.SIMPLE ) return BROWN;
        /*else if ( (pStack.getItem() instanceof ColorableMagicArmorItem item && item.getMaterial() == AncientMagicksArmorMaterials.TRIMMED)
                || pStack.getItem() == AncientMagicksItems.GOLDEN_STAFF.get() ) return RED;*/
        else return WHITE;
    }

    @Override
    default void clearColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement(TAG_DISPLAY);
        if ( compoundtag != null && compoundtag.contains(TAG_COLOR) ) compoundtag.remove(TAG_COLOR);
    }

    @Override
    default void setColor(ItemStack pStack, int pColor) {
        pStack.getOrCreateTagElement(TAG_DISPLAY).putInt(TAG_COLOR, pColor);
    }
}
