package net.mindoth.ancientmagicks.client.gui.menu;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.event.CastingValidator;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketCraftSpell;
import net.mindoth.ancientmagicks.network.PacketDumpSpell;
import net.mindoth.ancientmagicks.network.PacketEditColorCode;
import net.mindoth.ancientmagicks.registries.ModBlocks;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.mindoth.ancientmagicks.registries.ModMenus;
import net.mindoth.ancientmagicks.item.RuneItem;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class SpellCraftingMenu extends AbstractContainerMenu {

    private static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 28;
    private static final int INV_SLOT_START = 28;
    private static final int INV_SLOT_END = 55;
    private static final int USE_ROW_SLOT_START = 55;
    private static final int USE_ROW_SLOT_END = 64;

    private static final int TOP_ROW_HEIGHT = 39;
    public int getTopRowHeight() {
        return TOP_ROW_HEIGHT;
    }
    private static final int BOTTOM_ROW_HEIGHT = 52 + 9;
    public int getBottomRowHeight() {
        return BOTTOM_ROW_HEIGHT;
    }

    private final Container craftSlots = new SimpleContainer(1 + 27) {
        @Override
        public void setChanged() {
            super.setChanged();
            SpellCraftingMenu.this.slotsChanged(this);
            SpellCraftingMenu.this.broadcastChanges();
        }
    };
    private final ContainerLevelAccess access;
    private final Player player;

    public List<Item> colorCode = Arrays.asList(ModItems.RUNE_ESSENCE.get(), ModItems.RUNE_ESSENCE.get(), ModItems.RUNE_ESSENCE.get());

    public SpellCraftingMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), buf.readBlockPos()));
    }

    public SpellCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.SPELL_CRAFTING_MENU.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;

        //Parchment Slot
        this.addSlot(new ParchmentSlot(this.craftSlots, 0, 79, TOP_ROW_HEIGHT));

        //Crafting slots
        for ( int i = 0; i < 3; ++i ) {
            for ( int j = 0; j < 9; ++j ) {
                this.addSlot(new RuneSlot(this.craftSlots, j + i * 9 + 1, 26 + (j - 1) * 18, BOTTOM_ROW_HEIGHT + i * 18, !this.craftSlots.getItem(0).isEmpty()));
            }
        }

        //Player inventory
        for ( int i = 0; i < 3; ++i ) {
            for ( int j = 0; j < 9; ++j ) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 68 + BOTTOM_ROW_HEIGHT + i * 18));
            }
        }

        //Player hotbar
        for ( int i = 0; i < 9; ++i ) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 126 + BOTTOM_ROW_HEIGHT));
        }
    }

    public boolean isCleanParchment(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ParchmentItem && (!stack.hasTag() || !stack.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING));
    }

    public int howManyRuneSlotsOpen() {
        int count = 0;
        for ( Slot slot : this.slots ) if ( slot instanceof RuneSlot runeSlot && runeSlot.isOpen ) count++;
        return count;
    }

    @Override
    public void slotsChanged(Container pInventory) {
        this.access.execute((level, pos) -> {
            ItemStack stack = this.craftSlots.getItem(0);
            //Placed clean parchment
            if ( isCleanParchment(stack) ) {
                final int slotsToOpen = ((ParchmentItem)stack.getItem()).getSize();
                for ( Slot slot : this.slots ) {
                    if ( howManyRuneSlotsOpen() >= slotsToOpen ) break;
                    if ( slot instanceof RuneSlot runeSlot && !runeSlot.isOpen ) runeSlot.isOpen = true;
                }
            }
            //Removed scroll
            else {
                for ( Slot slot : this.slots ) {
                    if ( slot instanceof RuneSlot runeSlot ) {
                        if ( !level.isClientSide && !runeSlot.getItem().isEmpty() ) {
                            if ( stack.isEmpty() ) quickMoveStack(this.player, runeSlot.index);
                            else setSlotContent(runeSlot.getSlotIndex(), ItemStack.EMPTY);
                        }
                        if ( runeSlot.isOpen ) runeSlot.isOpen = false;
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
        this.colorCode.set(index, rune);
        ModNetwork.sendToServer(new PacketEditColorCode(index, rune));
    }

    public void processColorCodeEditing(int index, Item rune) {
        this.access.execute((level, pos) -> {
            if ( !level.isClientSide ) this.colorCode.set(index, rune);
        });
    }

    public boolean isReadyToDump() {
        ItemStack stack = this.craftSlots.getItem(0);
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
                    ItemStack stack = this.craftSlots.getItem(0);
                    List<RuneItem> runeList = CastingValidator.getSpellStackFromScroll(stack);
                    List<String> dataList = CastingValidator.getDataListFromScroll(stack);
                    for ( int i = 0; i < this.slots.size(); i++ ) {
                        if ( i == 0 ) cleanScroll(stack);
                        else {
                            Slot slot = this.slots.get(i);
                            if ( slot instanceof RuneSlot) {
                                ItemStack rune = new ItemStack(runeList.get(i - 1));
                                if ( ((RuneItem)rune.getItem()).isEncodeable() ) {
                                    rune.getOrCreateTag().putString(RuneItem.NBT_KEY_COMPONENT_DATA, dataList.get(i - 1));
                                }
                                setSlotContent(i, rune);
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
        return isCleanParchment(this.craftSlots.getItem(0)) && !assemble(this.craftSlots).isEmpty();
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
                    ItemStack stack = assemble(this.craftSlots);
                    if ( name == null || Util.isBlank(name) ) stack.resetHoverName();
                    else stack.setHoverName(Component.literal(name));
                    setSlotContent(0, stack);
                }
            }
        });
    }

    private void setSlotContent(int slot, ItemStack stack) {
        ServerPlayer serverplayer = (ServerPlayer)this.player;
        this.craftSlots.setItem(slot, stack);
        this.setRemoteSlot(slot, stack);
        serverplayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), slot, stack));
    }

    private String getItemName(String string) {
        return SharedConstants.filterText(string).length() <= 50 ? SharedConstants.filterText(string) : null;
    }

    public ItemStack assemble(Container container) {
        ItemStack scroll = this.craftSlots.getItem(0).copy();
        List<ItemStack> runeStackList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 1; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( !stack.isEmpty() ) {
                if ( stack.getItem() instanceof RuneItem rune ) {
                    if ( rune.isEncodeable() && (!stack.hasTag() || !stack.getTag().contains(RuneItem.NBT_KEY_COMPONENT_DATA)) ) return ItemStack.EMPTY;
                    else runeStackList.add(stack);
                }
                else restList.add(stack);
            }
        }
        if ( restList.isEmpty() ) {
            List<RuneItem> runeList = Lists.newArrayList();
            StringBuilder effectData = new StringBuilder();
            for ( int i = 0; i < runeStackList.size(); i++ ) {
                ItemStack stack = runeStackList.get(i);
                if ( stack.getItem() instanceof RuneItem rune ) {
                    runeList.add(rune);
                    if ( i > 0 ) effectData.append(",");
                    effectData.append(rune.encodeComponentData(stack));
                }
            }
            if ( CastingValidator.isValidSpell(runeList) ) {
                CompoundTag tag = scroll.getOrCreateTag();

                tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, CastingValidator.getStringFromSpellStack(runeList));
                tag.putString(ParchmentItem.NBT_KEY_DATA_STRING, effectData.toString());

                StringBuilder spellCode = new StringBuilder();
                for ( int i = 0; i < AncientMagicks.comboSizeCalc(); i++ ) {
                    if ( i > 0 ) spellCode.append(",");
                    spellCode.append(ForgeRegistries.ITEMS.getKey(this.colorCode.get(i)).toString());
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

    //No clue what this is
    public int getSize() {
        return 29;
    }
}
