package net.mindoth.ancientmagicks.item.castingitem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;

import javax.annotation.Nonnull;

public class WandItem extends SpellStorageItem implements Vanishable {

    public WandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }
}
