package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SpellBookScreen extends AncientMagicksScreen {

    private final List<ItemStack> itemList;
    private final List<List<ItemStack>> pageList;
    private int spreadNumber;
    private final List<Button> slotList = Lists.newArrayList();

    private final int arrowYOffset = 68;
    private final int arrowXOffset = 94;
    private Button rightArrow;
    private final int rightArrowXOffset = this.arrowXOffset;
    private Button leftArrow;
    private final int leftArrowXOffset = -18 - this.arrowXOffset;

    private final int maxRows = 5;
    private final int maxColumns = 4;
    private final int squareSpacing = 26;

    protected SpellBookScreen(List<ItemStack> stackList, int spreadNumber) {
        super(Component.literal(""));
        this.pageList = Lists.newArrayList();
        this.spreadNumber = spreadNumber;
        if ( stackList.isEmpty() ) this.itemList = List.of(ItemStack.EMPTY);
        else {
            this.itemList = stackList;
            List<ItemStack> page = Lists.newArrayList();
            for ( ItemStack stack : this.itemList ) {
                page.add(stack);
                if ( page.size() == this.maxRows * this.maxColumns * 2 || this.itemList.get(this.itemList.size() - 1) == stack ) {
                    this.pageList.add(page);
                    page = Lists.newArrayList();
                }
            }
        }
    }

    public static void open(List<ItemStack> itemList, int spreadNumber) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if ( !(MINECRAFT.screen instanceof SpellBookScreen) ) MINECRAFT.setScreen(new SpellBookScreen(itemList, spreadNumber));
    }

    private boolean isFirstPage() {
        return this.spreadNumber == 0;
    }

    private boolean isLastPage() {
        return this.spreadNumber == this.pageList.size() - 1;
    }

    @Override
    protected void init() {
        super.init();

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        //Slot Widgets
        buildButtons(x, y);

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

    private void buildButtons(int x, int y) {
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
        handleSlotButtonVisibility();
    }

    private void handlePageLeft(Button button) {
        if ( !isFirstPage() ) {
            this.spreadNumber--;
            handleSlotButtonVisibility();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private void handlePageRight(Button button) {
        if ( !isLastPage() ) {
            this.spreadNumber++;
            handleSlotButtonVisibility();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private void handleSlotButtonVisibility() {
        for ( Button button : this.slotList ) {
            int stackIndex = this.slotList.indexOf(button) + ((2 * this.maxColumns * this.maxRows) * (this.spreadNumber));
            if ( stackIndex >= this.itemList.size() && button.visible ) button.visible = false;
            else if ( !button.visible ) button.visible = true;
        }
    }

    private void buildSlotButton(int xPos, int yPos) {
        Button button = addRenderableWidget(Button.builder(Component.literal(""), this::handleSlotButton)
                .bounds(xPos - 1, yPos - 1, 18, 18)
                .build());
        this.slotList.add(button);
    }

    private void handleSlotButton(Button button) {
        if ( !this.slotList.contains(button) ) return;
        int stackIndex = this.slotList.indexOf(button) + ((2 * this.maxColumns * this.maxRows) * (this.spreadNumber));
        if ( stackIndex >= this.itemList.size() ) return;
        ItemStack stack = this.pageList.get(this.spreadNumber).get(this.slotList.indexOf(button));
        ItemStack spell = getPossibleContainedSpell(stack);
        if ( !(spell.getItem() instanceof SpellItem spellItem) ) return;
        if ( !ClientMagicData.isSpellKnown(spellItem) && !minecraft.player.isCreative() ) return;
        SpellScreen.open(spell, this.itemList, this.spreadNumber);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        //Background
        renderBackground(graphics);
        drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/spell_book_screen.png"),
                x - 140, y - 90, 0, 0, 280, 180, 280, 180, graphics);

        //Arrows
        if ( this.rightArrow.visible ) this.rightArrow.renderTexture(graphics, new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/page_arrows.png"),
                x + this.rightArrowXOffset, y + this.arrowYOffset, 0, 0, 10, 18, 10, 36, 20);
        if ( this.leftArrow.visible ) this.leftArrow.renderTexture(graphics, new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/page_arrows.png"),
                x + this.leftArrowXOffset, y + this.arrowYOffset, 18, 0, 10, 18, 10, 36, 20);

        for ( List<ItemStack> page : this.pageList ) {
            if ( this.spreadNumber == this.pageList.indexOf(page) ) {
                boolean isRightPage = false;
                int row = 0;
                int column = 0;
                for ( int i = 0; i < page.size(); i++ ) {
                    ItemStack stack = page.get(i);
                    ItemStack spell = getPossibleContainedSpell(stack);

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

                    drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/square.png"),
                            xPos - 3, yPos - 3, 0, 0, 22, 22, 22, 22, graphics);

                    if ( spell.getItem() instanceof SpellItem spellItem ) {
                        if ( ClientMagicData.isSpellKnown(spellItem) || minecraft.player.isCreative() ) renderItemWithDecorations(graphics, spell, xPos, yPos);
                        else renderItemWithDecorations(graphics, stack, xPos, yPos);
                        if ( this.slotList.get(i).isHovered() ) {
                            graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
                            graphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        }
                    }

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

    @Override
    public void tick() {
        //Arrow button visibility
        if ( this.rightArrow.isFocused() ) this.rightArrow.setFocused(false);
        if ( isLastPage() && this.rightArrow.visible ) this.rightArrow.visible = false;
        if ( !isLastPage() && !this.rightArrow.visible ) this.rightArrow.visible = true;
        if ( this.leftArrow.isFocused() ) this.leftArrow.setFocused(false);
        if ( isFirstPage() && this.leftArrow.visible ) this.leftArrow.visible = false;
        if ( !isFirstPage() && !this.leftArrow.visible ) this.leftArrow.visible = true;
    }
}
