package net.mindoth.ancientmagicks.client.menu;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ComponentSlot extends Slot {

    public boolean isOpen;

    public ComponentSlot(Container pContainer, int pSlot, int pX, int pY, boolean isOpen) {
        super(pContainer, pSlot, pX, pY);
        this.isOpen = isOpen;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ComponentItem && this.isOpen;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
