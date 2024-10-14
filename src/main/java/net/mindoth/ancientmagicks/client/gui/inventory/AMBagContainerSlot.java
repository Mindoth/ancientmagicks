package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AMBagContainerSlot extends SlotItemHandler {
    public AMBagContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return super.mayPlace(stack) && stack.getItem() instanceof SpellItem || stack.getItem() instanceof AncientTabletItem;
    }
}
