package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.SpellStorageItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SpellBookScreen extends AncientMagicksScreen {

    private final List<ItemStack> itemList;
    private final List<List<ItemStack>> pageList;
    private int pageNumber;
    private final List<Button> slotList = Lists.newArrayList();

    private final int arrowYOffset = 77;
    private Button rightArrow;
    private final int rightArrowXOffset = 120;
    private Button leftArrow;
    private final int leftArrowXOffset = -18 - 120;

    private final int maxRows = 6;
    private final int maxColumns = 4;
    private final int squareSpacing = 26;

    protected SpellBookScreen(List<ItemStack> stackList) {
        super(Component.literal(""));
        this.pageList = Lists.newArrayList();
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

    public static void open(List<ItemStack> itemList) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if ( MINECRAFT.screen == null ) MINECRAFT.setScreen(new SpellBookScreen(itemList));
    }

    private boolean isFirstPage() {
        return this.pageNumber == 0;
    }

    private boolean isLastPage() {
        return this.pageNumber == this.pageList.size() - 1;
    }

    @Override
    protected void init() {
        super.init();
        this.pageNumber = 0;

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        //Slot Widgets
        boolean isRightPage = false;
        int row = 0;
        int column = 0;
        for ( int i = 0; i < this.maxRows * this.maxColumns * 2; i++ ) {
            if (column == this.maxColumns) {
                row++;
                column = 0;
                if (row == this.maxRows) {
                    row = 0;
                    isRightPage = !isRightPage;
                }
            }

            int xPos = isRightPage ? x + 20 + (column * this.squareSpacing) : x - 114 + (column * this.squareSpacing);
            int yPos = y - 74 + (row * this.squareSpacing);

            buildSlotButton(xPos - 1, yPos - 1);

            column++;
        }

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

    private void handlePageLeft(Button button) {
        if ( !isFirstPage() ) {
            this.pageNumber--;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private void handlePageRight(Button button) {
        if ( !isLastPage() ) {
            this.pageNumber++;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private Button buildSlotButton(int xPos, int yPos) {
        Button button = addRenderableOnly(Button.builder(Component.literal(""), this::handleSlotButton)
                .bounds(xPos - 1, yPos - 1, 18, 18)
                .build());
        this.slotList.add(button);
        return button;
    }

    private void handleSlotButton(Button button) {
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
            if ( this.pageNumber == this.pageList.indexOf(page) ) {
                boolean isRightPage = false;
                int row = 0;
                int column = 0;
                for ( int i = 0; i < page.size(); i++ ) {
                    ItemStack stack = page.get(i);
                    ItemStack spell;
                    SpellItem vesselSpell = SpecialCastingItem.getStoredSpell(stack);
                    if ( stack.getItem() instanceof SpellStorageItem && vesselSpell != null ) spell = new ItemStack(vesselSpell);
                    else spell = stack;
                    if (column == this.maxColumns) {
                        row++;
                        column = 0;
                        if (row == this.maxRows) {
                            row = 0;
                            isRightPage = !isRightPage;
                        }
                    }

                    int xPos = isRightPage ? x + 20 + (column * this.squareSpacing) : x - 114 + (column * this.squareSpacing);
                    int yPos = y - 74 + (row * this.squareSpacing);

                    drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/square.png"),
                            xPos - 3, yPos - 3, 0, 0, 22, 22, 22, 22, graphics);

                    if ( spell.getItem() instanceof SpellItem spellItem ) {
                        if ( ClientMagicData.isSpellKnown(spellItem) || minecraft.player.isCreative() ) renderItem(graphics, spell, stack, xPos, yPos);
                        else renderItem(graphics, stack, stack, xPos, yPos);
                        if ( this.slotList.get(i).isHovered() ) {
                            graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
                            graphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        }
                    }

                    column++;
                }
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
    }
}
