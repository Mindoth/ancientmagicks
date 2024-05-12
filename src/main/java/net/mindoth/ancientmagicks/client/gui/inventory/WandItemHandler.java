package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class WandItemHandler extends ItemStackHandler {
    public WandItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        WandManager.get().setDirty();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return (stack.getItem() instanceof RuneItem || (stack.getItem() instanceof RuneItem || stack.getItem() instanceof SpellTabletItem));
    }
}
