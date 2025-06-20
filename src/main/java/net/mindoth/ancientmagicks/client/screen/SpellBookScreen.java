package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketRemoveSpellFromBook;
import net.mindoth.ancientmagicks.network.PacketReorderSpellBook;
import net.mindoth.ancientmagicks.network.PacketUpdateBookData;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SpellBookScreen extends ModScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/spell_book_screen.png");

    private final ItemStack book;
    private List<ItemStack> itemList = Lists.newArrayList();
    private final List<ItemStack> scrollList;
    private List<List<ItemStack>> pageList;
    private int spreadNumber;
    private List<Button> slotButtonList = Lists.newArrayList();
    private HashMap<Button, Button> buttonMap = new HashMap<>();
    private List<Button> swapButtonList = Lists.newArrayList();
    private final int leftSwapButtonXOffset = 56;
    private final int rightSwapButtonXOffset = 156;

    private final int arrowYOffset = 68;
    private final int arrowXOffset = 94;
    private Button rightArrow;
    private final int rightArrowXOffset = this.arrowXOffset;
    private Button leftArrow;
    private final int leftArrowXOffset = -18 - this.arrowXOffset;

    private final int maxRows = 5;
    private final int maxColumns = AncientMagicks.comboSizeCalc() + 1;
    private final int squareSpacing = 26;

    protected SpellBookScreen(ItemStack book, int spreadNumber) {
        super(Component.literal(""));
        this.spreadNumber = spreadNumber;
        this.book = book;
        this.scrollList = SpellBookItem.getScrollListFromBook(this.book.getOrCreateTag());
        this.scrollList.removeIf(ItemStack::isEmpty);
        createPages(false);
    }

    public static void open(ItemStack stack, int spreadNumber) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if ( !(MINECRAFT.screen instanceof SpellBookScreen) ) MINECRAFT.setScreen(new SpellBookScreen(stack, spreadNumber));
    }

    private boolean isFirstPage() {
        return this.spreadNumber == 0;
    }

    private boolean isLastPage() {
        return this.spreadNumber == this.pageList.size() - 1 || this.itemList.isEmpty();
    }

    private void createPages(boolean refreshBook) {
        if ( refreshBook ) this.book.setTag(SpellBookItem.constructBook(this.book, this.scrollList).getTag());
        this.itemList = Lists.newArrayList();
        for ( ItemStack stack : this.scrollList ) {
            this.itemList.add(stack);
            String code = stack.getTag().getString(ParchmentItem.NBT_KEY_CODE_STRING);
            List<String> codeList = List.of(code.split(","));
            for ( String string : codeList ) this.itemList.add(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string))));
        }
        this.pageList = Lists.newArrayList();
        List<ItemStack> page = Lists.newArrayList();
        for ( ItemStack stack : this.itemList ) {
            page.add(stack);
            if ( page.size() == this.maxRows * this.maxColumns * 2 || this.itemList.get(this.itemList.size() - 1) == stack ) {
                this.pageList.add(page);
                page = Lists.newArrayList();
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        buildButtons(minecraft.getWindow().getGuiScaledWidth() / 2, minecraft.getWindow().getGuiScaledHeight() / 2);

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }

    private void handleButtonVisibility() {
        handleSlotButtonVisibility();
        handleSwapButtonVisibility();
    }

    private void buildButtons(int x, int y) {
        this.slotButtonList = Lists.newArrayList();
        this.buttonMap = new HashMap<>();
        this.swapButtonList = Lists.newArrayList();
        this.clearWidgets();
        boolean isRightPage = false;
        int row = 0;
        int column = 0;
        for ( int i = 0; i < this.maxRows * this.maxColumns * 2; i++ ) {
            if ( column == this.maxColumns ) {
                row++;
                column = 0;
                if ( row == this.maxRows ) {
                    row = 0;
                    isRightPage = !isRightPage;
                }
            }

            int xPos = isRightPage ? x + 20 + (column * this.squareSpacing) : x - 114 + (column * this.squareSpacing);
            int yPos = y - 74 + (row * this.squareSpacing);

            buildSlotButton(xPos - 1, yPos - 1);

            column++;
        }
        handleButtonVisibility();

        //Page Arrows
        this.rightArrow = addRenderableWidget(Button.builder(Component.literal(""), this::handlePageRight)
                .bounds(x + this.rightArrowXOffset, y + this.arrowYOffset, 18, 10)
                .build());
        if ( isLastPage() && this.rightArrow.visible ) this.rightArrow.visible = false;
        if ( !isLastPage() && !this.rightArrow.visible ) this.rightArrow.visible = true;

        this.leftArrow = addRenderableWidget(Button.builder(Component.literal(""), this::handlePageLeft)
                .bounds(x + this.leftArrowXOffset, y + this.arrowYOffset, 18, 10)
                .build());
        if ( isFirstPage() && this.leftArrow.visible ) this.leftArrow.visible = false;
        if ( !isFirstPage() && !this.leftArrow.visible ) this.leftArrow.visible = true;
    }

    private void buildSlotButton(int xPos, int yPos) {
        Button button = addRenderableWidget(Button.builder(Component.literal(""), this::handleSlotButton)
                .bounds(xPos - 1, yPos - 1, 18, 18)
                .build());
        this.slotButtonList.add(button);
        if ( getStackFromSlot(button).getItem() instanceof ParchmentItem ) buildSwapButton(xPos, yPos, button);
    }

    private void handleSlotButton(Button button) {
        if ( !this.slotButtonList.contains(button) ) return;
        ItemStack stack = getStackFromSlot(button);
        if ( stack.isEmpty() ) return;
        Item item = stack.getItem();
        if ( item instanceof ParchmentItem ) {
            int index = this.scrollList.indexOf(stack);
            ModNetwork.sendToServer(new PacketRemoveSpellFromBook(this.book, this.scrollList, index));
            this.scrollList.remove(index);

            createPages(true);
            this.clearWidgets();
            buildButtons(minecraft.getWindow().getGuiScaledWidth() / 2, minecraft.getWindow().getGuiScaledHeight() / 2);
        }
        else if ( item == ModItems.BLANK_SLATE.get() || item instanceof ColorRuneItem ) changeRune(button, item);
    }

    private void handleSlotButtonVisibility() {
        for ( Button button : this.slotButtonList ) {
            int stackIndex = this.slotButtonList.indexOf(button) + ((2 * this.maxColumns * this.maxRows) * (this.spreadNumber));
            if ( stackIndex >= this.itemList.size() && button.visible ) button.visible = false;
            else if ( !button.visible ) button.visible = true;
        }
    }

    private void buildSwapButton(int xPos, int yPos, Button slotButton) {
        if ( this.swapButtonList.size() >= this.maxRows ) xPos += this.rightSwapButtonXOffset;
        else xPos -= this.leftSwapButtonXOffset;
        Button button = addRenderableWidget(Button.builder(Component.literal(""), this::handleSwapButton)
                .bounds(xPos - 1, yPos - 1, 13, 9)
                .build());
        this.swapButtonList.add(button);
        this.buttonMap.put(button, slotButton);
    }

    private void handleSwapButton(Button button) {
        /*if ( !this.swapButtonList.contains(button) ) return;
        ItemStack stack = this.itemList.get(this.itemList.indexOf(getStackFromSlot(this.buttonMap.get(button))));
        System.out.println("SCROLL: " + stack.getHoverName().getString());*/

        if ( !this.swapButtonList.contains(button) ) return;
        ItemStack stack = this.itemList.get(this.itemList.indexOf(getStackFromSlot(this.buttonMap.get(button))));
        if ( stack.isEmpty() ) return;
        if ( stack.getItem() instanceof ParchmentItem ) {
            int index = this.scrollList.indexOf(stack);
            ModNetwork.sendToServer(new PacketReorderSpellBook(this.book, this.scrollList, index));

            ItemStack first = this.scrollList.get(index - 1).copy();
            ItemStack second = this.scrollList.get(index).copy();

            this.scrollList.set(index - 1, second);
            this.scrollList.set(index, first);

            createPages(true);
            this.clearWidgets();
            buildButtons(minecraft.getWindow().getGuiScaledWidth() / 2, minecraft.getWindow().getGuiScaledHeight() / 2);
        }
    }

    //TODO button visibility on click. Left side disappears sometimes
    private void handleSwapButtonVisibility() {
        for ( Button button : this.swapButtonList ) {
            if ( this.swapButtonList.indexOf(button) == 0 && this.spreadNumber == 0 ) button.visible = false;
            else {
                if ( !this.buttonMap.get(button).visible && button.visible ) button.visible = false;
                else if ( !button.visible ) button.visible = true;
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        //Background
        renderBackground(graphics);
        drawTexture(TEXTURE, x - 140, y - 90, 0, 0, 280, 180, 280, 202, graphics);

        //Arrows
        if ( this.rightArrow.visible ) this.rightArrow.renderTexture(graphics, TEXTURE, x + this.rightArrowXOffset, y + this.arrowYOffset,
                0, 180, 10, 18, 10, 280, 202);
        if ( this.leftArrow.visible ) this.leftArrow.renderTexture(graphics, TEXTURE, x + this.leftArrowXOffset, y + this.arrowYOffset,
                18, 180, 10, 18, 10, 280, 202);

        for ( List<ItemStack> page : this.pageList ) {
            if ( this.spreadNumber == this.pageList.indexOf(page) ) {
                boolean isRightPage = false;
                int row = 0;
                int column = 0;
                for ( int i = 0; i < page.size(); i++ ) {
                    ItemStack stack = page.get(i);

                    //Spot calc
                    if ( column == this.maxColumns ) {
                        row++;
                        column = 0;
                        if ( row == this.maxRows ) {
                            row = 0;
                            isRightPage = !isRightPage;
                        }
                    }

                    int xPos = isRightPage ? x + 20 + (column * this.squareSpacing) : x - 114 + (column * this.squareSpacing);
                    int yPos = y - 74 + (row * this.squareSpacing);

                    if ( stack.getItem() instanceof ParchmentItem ) {
                        drawTexture(TEXTURE, xPos - 3, yPos - 3, 36, 180, 22, 22, 280, 202, graphics);
                    }
                    else if ( stack.getItem() instanceof ColorRuneItem ) {
                        drawTexture(TEXTURE, xPos - 3, yPos - 3, 58, 180, 22, 22, 280, 202, graphics);
                    }

                    renderItemWithDecorations(graphics, stack, xPos, yPos);
                    if ( this.slotButtonList.get(i).isHovered() ) {
                        graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
                        if ( stack.getItem() instanceof ParchmentItem ) graphics.renderTooltip(this.font, stack, mouseX, mouseY);
                    }

                    //Swap Arrow
                    /*if ( stack.getItem() instanceof ParchmentItem ) {
                        int index = i / this.maxColumns;
                        int newX = xPos;
                        if ( index >= this.maxRows ) newX += this.rightSwapButtonXOffset;
                        else newX -= this.leftSwapButtonXOffset;
                        this.swapButtonList.get(index).renderTexture(graphics, TEXTURE, newX - 1, yPos - 1,
                                80, 180, 7, 11, 7, 280, 202);
                    }*/

                    column++;
                }
            }
        }

        //Page number
        for ( int i = 0; i < 2; i++ ) {
            int pageNum = this.spreadNumber * 2 + 1 + i;
            Component pageNumTxt = Component.literal(String.valueOf(pageNum)).setStyle(Style.EMPTY.withBold(true));
            int textX = x - (this.font.width(pageNumTxt) / 2);
            int pageNumXOff = 67;
            int pageNumX = pageNum % 2 == 0 ? textX + pageNumXOff : textX - pageNumXOff;
            graphics.drawString(this.font, pageNumTxt, pageNumX, y + this.arrowYOffset, 0, false);
        }
    }

    private void handlePageLeft(Button button) {
        if ( !isFirstPage() ) {
            this.spreadNumber--;
            handleButtonVisibility();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private void handlePageRight(Button button) {
        if ( !isLastPage() ) {
            this.spreadNumber++;
            handleButtonVisibility();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private ItemStack getStackFromSlot(Button button) {
        int stackIndex = this.slotButtonList.indexOf(button) + ((2 * this.maxColumns * this.maxRows) * (this.spreadNumber));
        if ( stackIndex >= this.itemList.size() ) return ItemStack.EMPTY;
        return this.pageList.get(this.spreadNumber).get(this.slotButtonList.indexOf(button));
    }

    private void changeRune(Button button, Item rune) {
        if ( rune == ModItems.BLANK_SLATE.get() ) rune = ModItems.BLUE_SIGIL.get();
        else if ( rune == ModItems.BLUE_SIGIL.get() ) rune = ModItems.PURPLE_SIGIL.get();
        else if ( rune == ModItems.PURPLE_SIGIL.get() ) rune = ModItems.YELLOW_SIGIL.get();
        else if ( rune == ModItems.YELLOW_SIGIL.get() ) rune = ModItems.GREEN_SIGIL.get();
        else if ( rune == ModItems.GREEN_SIGIL.get() ) rune = ModItems.BLACK_SIGIL.get();
        else if ( rune == ModItems.BLACK_SIGIL.get() ) rune = ModItems.WHITE_SIGIL.get();
        else rune = ModItems.BLANK_SLATE.get();

        final int index = this.itemList.indexOf(getStackFromSlot(button));
        this.itemList.set(index, new ItemStack(rune));
        for ( int i = index; i >= 0; i-- ) {
            ItemStack stack = this.itemList.get(i);
            if ( stack.getItem() instanceof ParchmentItem ) {
                String code = stack.getTag().getString(ParchmentItem.NBT_KEY_CODE_STRING);
                List<String> codeList = List.of(code.split(","));
                StringBuilder stringBuilder = new StringBuilder();
                for ( int j = i + 1; j < i + 1 + codeList.size(); j++ ) {
                    if ( j > i + 1 ) stringBuilder.append(",");
                    String string = ForgeRegistries.ITEMS.getKey(this.itemList.get(j).getItem()).toString();
                    stringBuilder.append(string);
                }
                ItemStack newStack = stack.copy();
                newStack.getOrCreateTag().putString(ParchmentItem.NBT_KEY_CODE_STRING, stringBuilder.toString());
                this.scrollList.set(this.scrollList.indexOf(stack), newStack);
                ModNetwork.sendToServer(new PacketUpdateBookData(this.book, this.scrollList));
                createPages(true);
                break;
            }
        }
    }

    @Override
    public void tick() {
        if ( this.rightArrow.isFocused() ) this.rightArrow.setFocused(false);
        if ( isLastPage() && this.rightArrow.visible ) this.rightArrow.visible = false;
        if ( !isLastPage() && !this.rightArrow.visible ) this.rightArrow.visible = true;
        if ( this.leftArrow.isFocused() ) this.leftArrow.setFocused(false);
        if ( isFirstPage() && this.leftArrow.visible ) this.leftArrow.visible = false;
        if ( !isFirstPage() && !this.leftArrow.visible ) this.leftArrow.visible = true;
        for ( Button button : this.swapButtonList ) if ( button.isFocused() ) button.setFocused(false);
    }
}
