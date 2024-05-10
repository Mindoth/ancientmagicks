package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AncientMagicksGroup extends ItemGroup {

    public static final AncientMagicksGroup RUNIC_ITEMS_TAB = new AncientMagicksGroup(ItemGroup.TABS.length, "ancientmagicks_tab");

    public AncientMagicksGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(AncientMagicksItems.BEGINNER_STAFF.get());
    }
}
