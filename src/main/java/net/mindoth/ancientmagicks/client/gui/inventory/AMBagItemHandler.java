package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class AMBagItemHandler extends ItemStackHandler {
    public AMBagItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        AMBagManager.get().setDirty();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof TabletItem;
    }
}
