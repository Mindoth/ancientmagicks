package net.mindoth.ancientmagicks.client.menu;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksBlocks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.ancientmagicks.registries.AncientMagicksMenus;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SpellCraftingMenu extends AbstractContainerMenu {

    private static final int TOP_ROW_HEIGHT = 23;
    private static final int BOTTOM_ROW_HEIGHT = 52 + 9;
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 9, 1);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;
    @Nullable
    private String itemName;

    public SpellCraftingMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), buf.readBlockPos()));
    }

    public SpellCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(AncientMagicksMenus.SPELL_CRAFTING_MENU.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 111, TOP_ROW_HEIGHT));

        //Crafting slots
        for ( int i = 0; i < 9; ++i ) {
            if ( i == 0 ) this.addSlot(new ParchmentSlot(this.craftSlots, i, 49, TOP_ROW_HEIGHT));
            else this.addSlot(new ComponentSlot(this.craftSlots, i, 17 + (i - 1) * 18, BOTTOM_ROW_HEIGHT, !craftSlots.getItem(0).isEmpty()));
        }

        //Player inventory
        for ( int i = 0; i < 3; ++i ) {
            for ( int j = 0; j < 9; ++j ) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 32 + BOTTOM_ROW_HEIGHT + i * 18));
            }
        }

        //Player hotbar
        for ( int i = 0; i < 9; ++i ) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 90 + BOTTOM_ROW_HEIGHT));
        }
    }

    /*public boolean matches(CraftingContainer container) {
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> componentStackList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                if ( stack.getItem() instanceof ParchmentItem && !stack.hasTag() ) paperList.add(stack);
                else if ( stack.getItem() instanceof ComponentItem ) componentStackList.add(stack);
                else restList.add(stack);
            }
        }
        if ( paperList.size() == 1 && restList.isEmpty() ) {
            List<ComponentItem> componentList = Lists.newArrayList();
            for ( ItemStack stack : componentStackList ) if ( stack.getItem() instanceof ComponentItem component ) componentList.add(component);
            return CastingValidator.isValidSpell(componentList);
        }
        else return false;
    }*/

    public ItemStack assemble(CraftingContainer container) {
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> componentStackList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                if ( stack.getItem() instanceof ParchmentItem && !stack.hasTag() ) paperList.add(stack);
                else if ( stack.getItem() instanceof ComponentItem ) componentStackList.add(stack);
                else restList.add(stack);
            }
        }
        if ( paperList.size() == 1 && restList.isEmpty() ) {
            List<ComponentItem> componentList = Lists.newArrayList();
            StringBuilder effectData = new StringBuilder();
            for ( int i = 0; i < componentStackList.size(); i++ ) {
                ItemStack stack = componentStackList.get(i);
                if ( stack.getItem() instanceof ComponentItem component ) {
                    componentList.add(component);
                    if ( i > 0 ) effectData.append(",");
                    effectData.append(component.encodeComponentData(stack));
                }
            }
            if ( CastingValidator.isValidSpell(componentList) ) {
                ItemStack stack = paperList.get(0).copy();
                stack.setCount(1);
                if ( stack.hasCustomHoverName() ) stack.setHoverName(Component.literal(paperList.get(0).getHoverName().getString()));
                CompoundTag tag = stack.getOrCreateTag();

                tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, CastingValidator.getStringFromSpellStack(componentList));
                tag.putString(ParchmentItem.NBT_KEY_DATA_STRING, effectData.toString());

                StringBuilder spellCode = new StringBuilder();
                for (int i = 0; i < AncientMagicks.comboSizeCalc(); i++ ) {
                    if ( i > 0 ) spellCode.append(",");
                    spellCode.append(ForgeRegistries.ITEMS.getKey(AncientMagicksItems.BLANK_RUNE.get()).toString());
                }
                tag.putString(ParchmentItem.NBT_KEY_CODE_STRING, spellCode.toString());
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    //TODO Fix renaming
    //TODO Fix component lock when no paper
    protected void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player) {
        if ( !level.isClientSide ) {
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack resultStack = ItemStack.EMPTY;
            if ( !assemble(craftSlots).isEmpty() ) {
                resultStack = assemble(craftSlots);
                if ( this.itemName != null && !Util.isBlank(this.itemName) ) resultStack.setHoverName(Component.literal(this.itemName));
            }
            System.out.println("NAME: " + resultStack.getHoverName().getString());
            resultSlots.setItem(0, resultStack);
            menu.setRemoteSlot(0, resultStack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, resultStack));
        }
        ItemStack scroll = craftSlots.getItem(0);
        if ( !scroll.isEmpty() ) {
            for ( Slot slot : this.slots ) {
                if ( slot instanceof ComponentSlot componentSlot ) {
                    if ( !componentSlot.hasPaper ) componentSlot.hasPaper = true;
                }
            }
        }
        else {
            for ( Slot slot : this.slots ) {
                if ( slot instanceof ComponentSlot componentSlot ) {
                    //if ( !slot.getItem().isEmpty() ) quickMoveStack(player, slot.index);
                    if ( componentSlot.hasPaper ) componentSlot.hasPaper = false;
                }
            }
        }
    }

    public boolean setItemName(String string) {
        String name = validateName(string);
        if ( name != null && !name.equals(this.itemName) ) {
            this.itemName = name;
            if ( this.getSlot(0).hasItem() ) {
                ItemStack itemstack = this.getSlot(0).getItem();
                if ( Util.isBlank(name) ) itemstack.resetHoverName();
                else itemstack.setHoverName(Component.literal(name));
            }
            this.broadcastChanges();
            return true;
        }
        else return false;
    }

    @Nullable
    private static String validateName(String name) {
        String string = SharedConstants.filterText(name);
        return string.length() <= 50 ? string : null;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if ( slot != null && slot.hasItem() ) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            if ( pIndex == 0 ) {
                this.access.execute((p_39378_, p_39379_) -> stack.getItem().onCraftedBy(stack, p_39378_, pPlayer));
                if ( !this.moveItemStackTo(stack, 10, 46, true) ) return ItemStack.EMPTY;
                slot.onQuickCraft(stack, itemStack);
            }
            else if ( pIndex >= 10 && pIndex < 46 ) {
                if ( !this.moveItemStackTo(stack, 1, 10, false) ) {
                    if ( pIndex < 37 ) if ( !this.moveItemStackTo(stack, 37, 46, false) ) return ItemStack.EMPTY;
                    else if ( !this.moveItemStackTo(stack, 10, 37, false) ) return ItemStack.EMPTY;
                }
            }
            else if ( !this.moveItemStackTo(stack, 10, 46, false) ) return ItemStack.EMPTY;

            if ( stack.isEmpty() ) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            if ( stack.getCount() == itemStack.getCount() ) return ItemStack.EMPTY;

            slot.onTake(pPlayer, stack);
            if ( pIndex == 0 ) pPlayer.drop(stack, false);
        }
        return itemStack;
    }

    @Override
    public void slotsChanged(Container pInventory) {
        this.access.execute((level, pos) -> slotChangedCraftingGrid(this, level, this.player));
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(pPlayer, this.craftSlots));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, AncientMagicksBlocks.SPELL_CRAFTING_TABLE.get());
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public int getSize() {
        return 11;
    }
}
