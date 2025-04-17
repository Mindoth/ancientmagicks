package net.mindoth.ancientmagicks.client.menu;

import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ParchmentSlot extends Slot {

    public ParchmentSlot(Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pContainer, pSlot, pXPosition, pYPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ParchmentItem && (!stack.hasTag() || !stack.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
