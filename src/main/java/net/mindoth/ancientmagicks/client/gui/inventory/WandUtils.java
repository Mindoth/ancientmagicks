package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.item.weapon.WandItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class WandUtils {

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public static Optional<UUID> getUUID(@Nonnull ItemStack stack) {
        if ( stack.getItem() instanceof WandItem && stack.hasTag() && stack.getTag().contains("UUID") ) {
            return Optional.of(stack.getTag().getUUID("UUID"));
        }
        else return Optional.empty();
    }
}
