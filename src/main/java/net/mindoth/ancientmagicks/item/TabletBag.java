package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.client.gui.inventory.AMBagContainer;
import net.mindoth.ancientmagicks.client.gui.inventory.AMBagData;
import net.mindoth.ancientmagicks.client.gui.inventory.AMBagManager;
import net.mindoth.ancientmagicks.item.castingitem.AMBagType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class TabletBag extends Item {
    public AMBagType tier;

    public TabletBag(AMBagType tier) {
        super(new Item.Properties().stacksTo(1));
        this.tier = tier;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack bag = serverPlayer.getItemInHand(handIn);
            AMBagData data = getData(bag);
            if ( isValidBagItem(bag) ) {
                serverPlayer.stopUsingItem();
                UUID uuid = data.getUuid();
                data.updateAccessRecords(serverPlayer.getName().getString(), System.currentTimeMillis());
                NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new AMBagContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()),
                        bag.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
            }
        }
        return result;
    }

    public static AMBagData getData(ItemStack stack) {
        if ( !(stack.getItem() instanceof TabletBag) ) return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return AMBagManager.get().getOrCreateAMBag(uuid, ((TabletBag)stack.getItem()).tier);
    }

    public static boolean isValidBagItem(ItemStack bagItem) {
        return bagItem.hasTag() && bagItem.getTag().contains("UUID") && getData(bagItem).getUuid() != null;
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return tier.rarity;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new TabletBag.AMBagCaps(stack);
    }

    static class AMBagCaps implements ICapabilityProvider {
        private final ItemStack stack;

        public AMBagCaps(ItemStack stack) {
            this.stack = stack;
        }

        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if ( cap == ForgeCapabilities.ITEM_HANDLER ) {
                if ( !optional.isPresent() ) optional = AMBagManager.get().getCapability(stack);
                return optional.cast();
            }
            else return LazyOptional.empty();
        }
    }
}
