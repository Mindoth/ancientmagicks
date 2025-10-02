package net.mindoth.ancientmagicks.client.menu;

import net.mindoth.ancientmagicks.registries.ModBlocks;
import net.mindoth.ancientmagicks.registries.ModMenus;
import net.mindoth.ancientmagicks.registries.recipe.RuneCraftingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RuneCraftingMenu extends AbstractContainerMenu {

    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 8;
    private static final int INV_SLOT_START = 8;
    private static final int INV_SLOT_END = 35;
    private static final int USE_ROW_SLOT_START = 35;
    private static final int USE_ROW_SLOT_END = 44;
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final Container resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public RuneCraftingMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), buf.readBlockPos()));
    }

    public RuneCraftingMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(ModMenus.RUNE_CRAFTING_MENU.get(), pContainerId);
        this.access = pAccess;
        this.player = pPlayerInventory.player;
        this.addSlot(new ModCraftResultSlot(pPlayerInventory.player, this, this.craftSlots, this.resultSlots, 0, 124, 35));
        this.addSlot(new EssenceSlot(this.craftSlots, 0, 48, 35));

        for ( int i = 0; i < 2; ++i ) {
            for ( int j = 0; j < 3; ++j ) {
                int extra0 = i == 1 && j == 1 ? 18 : 0;
                int extra1 = j != 1 ? 9 : 0;
                this.addSlot(new Slot(this.craftSlots, 1 + (j + i * 3), 30 + j * 18, 17 + i * 18 + extra0 + extra1));
            }
        }

        for ( int k = 0; k < 3; ++k ) {
            for ( int i1 = 0; i1 < 9; ++i1 ) {
                this.addSlot(new Slot(pPlayerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for ( int l = 0; l < 9; ++l ) {
            this.addSlot(new Slot(pPlayerInventory, l, 8 + l * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if ( slot != null && slot.hasItem() ) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            if ( pIndex >= CRAFT_SLOT_END && pIndex < USE_ROW_SLOT_END ) {
                if ( !this.moveItemStackTo(stack, RESULT_SLOT, CRAFT_SLOT_END, false) ) {
                    if ( pIndex < INV_SLOT_END ) if ( !this.moveItemStackTo(stack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false) ) return ItemStack.EMPTY;
                    else if ( !this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false) ) return ItemStack.EMPTY;
                }
            }
            else if ( !this.moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, false) ) return ItemStack.EMPTY;

            if ( stack.isEmpty() ) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            if ( stack.getCount() == itemStack.getCount() ) return ItemStack.EMPTY;

            slot.onTake(pPlayer, stack);
            if ( pIndex == RESULT_SLOT ) pPlayer.drop(stack, false);
        }
        return itemStack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(pPlayer, this.craftSlots));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.RUNE_CRAFTING_TABLE.get());
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public int getSize() {
        return 7;
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.access.execute((p_39386_, p_39387_) -> slotChangedCraftingGrid(this, p_39386_, this.player, this.craftSlots, this.resultSlots));
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, Container result) {
        if ( !level.isClientSide ) {
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<RuneCraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RuneCraftingRecipe.Type.INSTANCE, container, level);
            if ( optional.isPresent() ) {
                RuneCraftingRecipe craftingRecipe = optional.get();
                ItemStack itemStack1 = craftingRecipe.assemble(container, level.registryAccess());
                if ( itemStack1.isItemEnabled(level.enabledFeatures()) ) itemStack = itemStack1;
            }
            result.setItem(0, itemStack);
            menu.setRemoteSlot(0, itemStack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemStack));
        }
    }
}
