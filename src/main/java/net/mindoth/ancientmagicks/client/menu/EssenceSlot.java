package net.mindoth.ancientmagicks.client.menu;

import net.mindoth.ancientmagicks.registries.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EssenceSlot extends Slot {

    public EssenceSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() == ModItems.RUNE_ESSENCE.get();
    }
}
