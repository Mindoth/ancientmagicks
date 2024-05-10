package net.mindoth.ancientmagicks.client.gui.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class LockableSlot extends Slot {
    private final boolean locked;

    public LockableSlot(PlayerInventory inventory, int slotIndex, int x, int y, boolean lock) {
        super(inventory, slotIndex, x, y);
        this.locked = lock;
    }

    @Override
    public boolean mayPickup(@Nonnull PlayerEntity p_82869_1_) {
        return !this.locked;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack p_75214_1_) {
        return !this.locked;
    }
}
