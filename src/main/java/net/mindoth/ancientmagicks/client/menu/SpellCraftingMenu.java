package net.mindoth.ancientmagicks.client.menu;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketCraftSpell;
import net.mindoth.ancientmagicks.network.PacketDumpSpell;
import net.mindoth.ancientmagicks.network.PacketEditColorCode;
import net.mindoth.ancientmagicks.registries.ModBlocks;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.mindoth.ancientmagicks.registries.ModMenus;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class SpellCraftingMenu extends AbstractContainerMenu {

    private static final int TOP_ROW_HEIGHT = 39;
    public int getTopRowHeight() {
        return TOP_ROW_HEIGHT;
    }
    private static final int BOTTOM_ROW_HEIGHT = 52 + 9;
    public int getBottomRowHeight() {
        return BOTTOM_ROW_HEIGHT;
    }
    private final Container craftSlots = new SimpleContainer(1 + 9) {
        @Override
        public void setChanged() {
            super.setChanged();
            SpellCraftingMenu.this.slotsChanged(this);
            SpellCraftingMenu.this.broadcastChanges();
        }
    };
    private final ContainerLevelAccess access;
    private final Player player;

    public List<Item> colorCode = Arrays.asList(ModItems.BLANK_SLATE.get(), ModItems.BLANK_SLATE.get(), ModItems.BLANK_SLATE.get());

    public SpellCraftingMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), buf.readBlockPos()));
    }

    public SpellCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.SPELL_CRAFTING_MENU.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new ParchmentSlot(this.craftSlots, 0, 79, TOP_ROW_HEIGHT));

        //Crafting slots
        for ( int i = 0; i < 9; ++i ) {
            this.addSlot(new ComponentSlot(this.craftSlots, 1 + i, 26 + (i - 1) * 18, BOTTOM_ROW_HEIGHT, !craftSlots.getItem(0).isEmpty()));
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

    public boolean isCleanParchment(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ParchmentItem && (!stack.hasTag() || !stack.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING));
    }

    public int howManyComponentSlotsOpen() {
        int count = 0;
        for ( Slot slot : this.slots ) if ( slot instanceof ComponentSlot componentSlot && componentSlot.isOpen ) count++;
        return count;
    }

    @Override
    public void slotsChanged(Container pInventory) {
        this.access.execute((level, pos) -> {
            ItemStack stack = craftSlots.getItem(0);
            //Placed clean parchment
            if ( isCleanParchment(stack) ) {
                final int slotsToOpen = ((ParchmentItem)stack.getItem()).getSize();
                for ( Slot slot : this.slots ) {
                    if ( howManyComponentSlotsOpen() >= slotsToOpen ) break;
                    if ( slot instanceof ComponentSlot componentSlot && !componentSlot.isOpen ) componentSlot.isOpen = true;
                }
            }
            //Removed scroll
            else {
                for ( Slot slot : this.slots ) {
                    if ( slot instanceof ComponentSlot componentSlot ) {
                        if ( !level.isClientSide && !componentSlot.getItem().isEmpty() ) {
                            if ( stack.isEmpty() ) quickMoveStack(this.player, componentSlot.index);
                            else setSlotContent(componentSlot.getSlotIndex(), ItemStack.EMPTY);
                        }
                        if ( componentSlot.isOpen ) componentSlot.isOpen = false;
                    }
                }
            }
            if ( level.isClientSide && isReadyToDump() ) {
                List<Item> tempList = ParchmentItem.getScrollComboList(stack);
                for ( int i = 0; i < tempList.size(); i++ ) editColorCode(i, tempList.get(i));
            }
        });
    }

    public void editColorCode(int index, Item rune) {
        colorCode.set(index, rune);
        ModNetwork.sendToServer(new PacketEditColorCode(index, rune));
    }

    public void processColorCodeEditing(int index, Item rune) {
        this.access.execute((level, pos) -> {
            if ( !level.isClientSide ) colorCode.set(index, rune);
        });
    }

    public boolean isReadyToDump() {
        ItemStack stack = craftSlots.getItem(0);
        return !stack.isEmpty() && stack.getItem() instanceof ParchmentItem && stack.hasTag() && stack.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING);
    }

    public boolean dumpSpell() {
        if ( isReadyToDump() ) {
            ModNetwork.sendToServer(new PacketDumpSpell());
            return true;
        }
        else return false;
    }

    public void processDumping() {
        this.access.execute((level, pos) -> {
            if ( !level.isClientSide ) {
                if ( isReadyToDump() ) {
                    ItemStack stack = craftSlots.getItem(0);
                    List<SpellComponentItem> componentList = CastingValidator.getSpellStackFromScroll(stack);
                    List<String> dataList = CastingValidator.getDataListFromScroll(stack);
                    for ( int i = 0; i < this.slots.size(); i++ ) {
                        if ( i == 0 ) cleanScroll(stack);
                        else {
                            Slot slot = this.slots.get(i);
                            if ( slot instanceof ComponentSlot ) {
                                ItemStack component = new ItemStack(componentList.get(i - 1));
                                if ( ((SpellComponentItem)component.getItem()).isEncodeable() ) {
                                    component.getOrCreateTag().putString(SpellEffectItem.NBT_KEY_COMPONENT_DATA, dataList.get(i - 1));
                                }
                                setSlotContent(i, component);
                            }
                        }
                    }
                }
            }
        });
    }

    private void cleanScroll(ItemStack scroll) {
        ItemStack stack = scroll.copy();
        if ( stack.hasCustomHoverName() ) stack.resetHoverName();
        if ( stack.hasTag() ) {
            CompoundTag tag = stack.getTag();
            if ( tag.contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) tag.remove(ParchmentItem.NBT_KEY_SPELL_STRING);
            if ( tag.contains(ParchmentItem.NBT_KEY_DATA_STRING) ) tag.remove(ParchmentItem.NBT_KEY_DATA_STRING);
            if ( tag.contains(ParchmentItem.NBT_KEY_CODE_STRING) ) tag.remove(ParchmentItem.NBT_KEY_CODE_STRING);
            if ( stack.getTag().isEmpty() ) stack.setTag(null);
        }
        setSlotContent(0, stack);
    }

    public boolean isReadyToCraft() {
        return isCleanParchment(craftSlots.getItem(0)) && assemble(craftSlots) != ItemStack.EMPTY;
    }

    public boolean craftSpell(String string) {
        if ( isReadyToCraft() ) {
            ModNetwork.sendToServer(new PacketCraftSpell(getItemName(string)));
            return true;
        }
        else return false;
    }

    public void processCrafting(String name) {
        this.access.execute((level, pos) -> {
            if ( !level.isClientSide ) {
                if ( isReadyToCraft() ) {
                    ItemStack stack = assemble(craftSlots);
                    if ( name == null || Util.isBlank(name) ) stack.resetHoverName();
                    else stack.setHoverName(Component.literal(name));
                    setSlotContent(0, stack);
                }
            }
        });
    }

    private void setSlotContent(int slot, ItemStack stack) {
        ServerPlayer serverplayer = (ServerPlayer)player;
        craftSlots.setItem(slot, stack);
        this.setRemoteSlot(slot, stack);
        serverplayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), slot, stack));
    }

    private String getItemName(String string) {
        return SharedConstants.filterText(string).length() <= 50 ? SharedConstants.filterText(string) : null;
    }

    public ItemStack assemble(Container container) {
        ItemStack scroll = craftSlots.getItem(0).copy();
        List<ItemStack> componentStackList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 1; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                if ( stack.getItem() instanceof SpellComponentItem) {
                    if ( ((SpellComponentItem)stack.getItem()).isEncodeable() && (!stack.hasTag() || !stack.getTag().contains(SpellComponentItem.NBT_KEY_COMPONENT_DATA)) ) return ItemStack.EMPTY;
                    else componentStackList.add(stack);
                }
                else restList.add(stack);
            }
        }
        if ( restList.isEmpty() ) {
            List<SpellComponentItem> componentList = Lists.newArrayList();
            StringBuilder effectData = new StringBuilder();
            for ( int i = 0; i < componentStackList.size(); i++ ) {
                ItemStack stack = componentStackList.get(i);
                if ( stack.getItem() instanceof SpellComponentItem component ) {
                    componentList.add(component);
                    if ( i > 0 ) effectData.append(",");
                    effectData.append(component.encodeComponentData(stack));
                }
            }
            if ( CastingValidator.isValidSpell(componentList) ) {
                CompoundTag tag = scroll.getOrCreateTag();

                tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, CastingValidator.getStringFromSpellStack(componentList));
                tag.putString(ParchmentItem.NBT_KEY_DATA_STRING, effectData.toString());

                StringBuilder spellCode = new StringBuilder();
                for ( int i = 0; i < AncientMagicks.comboSizeCalc(); i++ ) {
                    if ( i > 0 ) spellCode.append(",");
                    spellCode.append(ForgeRegistries.ITEMS.getKey(colorCode.get(i)).toString());
                }
                tag.putString(ParchmentItem.NBT_KEY_CODE_STRING, spellCode.toString());
                return scroll;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if ( slot != null && slot.hasItem() ) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            /*if ( pIndex == 0 ) {
                this.access.execute((p_39378_, p_39379_) -> stack.getItem().onCraftedBy(stack, p_39378_, pPlayer));
                if ( !this.moveItemStackTo(stack, 10, 46, true) ) return ItemStack.EMPTY;
                slot.onQuickCraft(stack, itemStack);
            }
            else */if ( pIndex >= 10 && pIndex < 46 ) {
                if ( !this.moveItemStackTo(stack, 0, 10, false) ) {
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
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((level, pos) -> this.clearContainer(pPlayer, this.craftSlots));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.SPELL_CRAFTING_TABLE.get());
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return !(pSlot instanceof ParchmentSlot) && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public int getSize() {
        return 11;
    }
}
