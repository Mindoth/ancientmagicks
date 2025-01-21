package net.mindoth.ancientmagicks.item.armor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public interface DyeableMagicItem extends DyeableLeatherItem {
    String TAG_COLOR = "color";
    String TAG_DISPLAY = "display";
    int DEFAULT_LEATHER_COLOR = 16185078;

    @Override
    default boolean hasCustomColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement(TAG_DISPLAY);
        return compoundtag != null && compoundtag.contains(TAG_COLOR, 99);
    }

    @Override
    default int getColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement(TAG_DISPLAY);
        return compoundtag != null && compoundtag.contains(TAG_COLOR, 99) ? compoundtag.getInt(TAG_COLOR) : DEFAULT_LEATHER_COLOR;
    }

    @Override
    default void clearColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement(TAG_DISPLAY);
        if (compoundtag != null && compoundtag.contains(TAG_COLOR)) {
            compoundtag.remove(TAG_COLOR);
        }
    }

    @Override
    default void setColor(ItemStack pStack, int pColor) {
        pStack.getOrCreateTagElement(TAG_DISPLAY).putInt(TAG_COLOR, pColor);
    }
}
