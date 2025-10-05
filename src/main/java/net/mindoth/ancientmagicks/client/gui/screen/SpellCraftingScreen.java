package net.mindoth.ancientmagicks.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.gui.menu.RuneSlot;
import net.mindoth.ancientmagicks.client.gui.menu.SpellCraftingMenu;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpellCraftingScreen extends AbstractContainerScreen<SpellCraftingMenu> implements ContainerListener {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/spell_crafting_screen.png");
    private EditBox name;
    private Button craftButton;
    private final int CRAFT_BUTTON_X_OFFSET = 39;
    private Button dumpButton;
    private final int DUMP_BUTTON_X_OFFSET = CRAFT_BUTTON_X_OFFSET + 18;

    private Button runeButtonL;
    private final int RUNE_BUTTON_X_OFFSET_L = 101;
    private Button runeButtonM;
    private final int RUNE_BUTTON_X_OFFSET_M = 119;
    private Button runeButtonR;
    private final int RUNE_BUTTON_X_OFFSET_R = 137;
    private List<Button> runeButtonList = Lists.newArrayList();

    public SpellCraftingScreen(SpellCraftingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight += 45;
        inventoryLabelY += 45;
    }

    @Override
    protected void init() {
        super.init();
        subInit();
        this.menu.addSlotListener(this);
    }

    protected void subInit() {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, x + 17, y + 20, 142, 12, Component.translatable("container.ancientmagicks.name"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(true);
        this.name.setMaxLength(50);
        //this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(false);
        //Widgets
        buildButtons(x, y);
    }

    private void buildButtons(int x, int y) {
        this.runeButtonList = Lists.newArrayList();
        this.craftButton = addRenderableWidget(Button.builder(Component.literal(""), this::handleCraftButton)
                .bounds(x + this.CRAFT_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(), 16, 16)
                .build());
        this.dumpButton = addRenderableWidget(Button.builder(Component.literal(""), this::handleDumpButton)
                .bounds(x + this.DUMP_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(), 16, 16)
                .build());

        this.runeButtonL = addWidget(Button.builder(Component.literal(""), this::handleRuneButton)
                .bounds(x + this.RUNE_BUTTON_X_OFFSET_L, y + this.menu.getTopRowHeight(), 16, 16)
                .build());
        this.runeButtonList.add(this.runeButtonL);
        this.runeButtonM = addWidget(Button.builder(Component.literal(""), this::handleRuneButton)
                .bounds(x + this.RUNE_BUTTON_X_OFFSET_M, y + this.menu.getTopRowHeight(), 16, 16)
                .build());
        this.runeButtonList.add(this.runeButtonM);
        this.runeButtonR = addWidget(Button.builder(Component.literal(""), this::handleRuneButton)
                .bounds(x + this.RUNE_BUTTON_X_OFFSET_R, y + this.menu.getTopRowHeight(), 16, 16)
                .build());
        this.runeButtonList.add(this.runeButtonR);
    }

    private void handleCraftButton(Button button) {
        Slot slot = this.menu.getSlot(0);
        if ( slot.hasItem() && this.menu.isCleanParchment(slot.getItem()) ) {
            String string = this.name.getValue();
            if ( !slot.getItem().hasCustomHoverName() && string.equals(slot.getItem().getHoverName().getString()) ) string = "";
            if ( this.menu.craftSpell(string) ) this.name.setValue("");
        }
    }

    private void handleDumpButton(Button button) {
        Slot slot = this.menu.getSlot(0);
        if ( slot.hasItem() && !this.menu.isCleanParchment(slot.getItem()) ) {
            if ( this.menu.dumpSpell() && slot.getItem().hasCustomHoverName() ) this.name.setValue(slot.getItem().getHoverName().getString());
        }
    }

    private void handleRuneButton(Button button) {
        if ( !this.menu.isReadyToCraft() ) return;
        int index = this.runeButtonList.indexOf(button);
        Item rune = this.menu.colorCode.get(index);
        if ( rune == ModItems.RUNE_ESSENCE.get() ) rune = ModItems.BLUE_SIGIL.get();
        else if ( rune == ModItems.BLUE_SIGIL.get() ) rune = ModItems.PURPLE_SIGIL.get();
        else if ( rune == ModItems.PURPLE_SIGIL.get() ) rune = ModItems.YELLOW_SIGIL.get();
        else if ( rune == ModItems.YELLOW_SIGIL.get() ) rune = ModItems.GREEN_SIGIL.get();
        else if ( rune == ModItems.GREEN_SIGIL.get() ) rune = ModItems.BLACK_SIGIL.get();
        else if ( rune == ModItems.BLACK_SIGIL.get() ) rune = ModItems.WHITE_SIGIL.get();
        else rune = ModItems.RUNE_ESSENCE.get();
        this.menu.editColorCode(index, rune);
    }

    @Override
    public void slotChanged(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack) {
        if ( pSlotInd == 0 ) {
            boolean isEditable = this.menu.isCleanParchment(pStack);
            this.name.setEditable(isEditable);
            this.setFocused(this.name);
            if ( !isEditable ) this.name.setValue("");
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if ( pKeyCode == 256 ) this.minecraft.player.closeContainer();
        return !this.name.keyPressed(pKeyCode, pScanCode, pModifiers) && !this.name.canConsumeInput() ? super.keyPressed(pKeyCode, pScanCode, pModifiers) : true;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.name.tick();
        if ( this.craftButton.isFocused() ) this.craftButton.setFocused(false);
        if ( this.dumpButton.isFocused() ) this.dumpButton.setFocused(false);
        if ( this.runeButtonL.isFocused() ) this.runeButtonL.setFocused(false);
        if ( this.runeButtonM.isFocused() ) this.runeButtonM.setFocused(false);
        if ( this.runeButtonR.isFocused() ) this.runeButtonR.setFocused(false);
        if ( this.menu.isReadyToCraft() ) for ( Button button : this.runeButtonList ) if ( !button.visible ) button.visible = true;
        else for ( Button button1 : this.runeButtonList ) if ( button1.visible ) button1.visible = false;
    }

    @Override
    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String string = this.name.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.name.setValue(string);
    }

    @Override
    public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.name.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //Action buttons
        if ( this.menu.isReadyToCraft() ) {
            this.craftButton.renderTexture(graphics, TEXTURE, x + CRAFT_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(),
                    176, 16, 16, 16, 16, 256, 256);
            this.dumpButton.renderTexture(graphics, TEXTURE, x + DUMP_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(),
                    176 + 16, 48, 0, 16, 16, 256, 256);
        }
        else {
            this.craftButton.renderTexture(graphics, TEXTURE, x + CRAFT_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(),
                    176, 48, 0, 16, 16, 256, 256);
            if ( this.menu.isReadyToDump() ) {
                this.dumpButton.renderTexture(graphics, TEXTURE, x + DUMP_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(),
                        176 + 16, 16, 16, 16, 16, 256, 256);
            }
            else {
                this.dumpButton.renderTexture(graphics, TEXTURE, x + DUMP_BUTTON_X_OFFSET, y + this.menu.getTopRowHeight(),
                        176 + 16, 48, 0, 16, 16, 256, 256);
            }
        }

        //Rune buttons
        for ( int i = 0; i < this.runeButtonList.size(); i++ ) {
            Button button = this.runeButtonList.get(i);
            renderItemWithDecorations(graphics, button, x + RUNE_BUTTON_X_OFFSET_L + i * 18, y + this.menu.getTopRowHeight(), mouseX, mouseY);
        }

        //Locked slots
        for ( int i = 0; i < this.menu.slots.size(); i++ ) {
            if ( this.menu.getSlot(i) instanceof RuneSlot slot && !slot.isOpen ) {
                int row = 0;
                if ( i > 9 ) row += 18;
                if ( i > 18 ) row += 18;
                int xPos = x + 26 + (i - 2) * 18 - row * 9;
                int yPos = y + this.menu.getBottomRowHeight() + row;
                ModScreen.drawTexture(TEXTURE, xPos, yPos, 176, 0, 16, 16, 256, 256, graphics);
            }
        }
    }

    protected void renderItemWithDecorations(GuiGraphics graphics, Button button, int xPos, int yPos, int mouseX, int mouseY) {
        if ( this.menu.isReadyToCraft() ) {
            ItemStack stack = new ItemStack(this.menu.colorCode.get(this.runeButtonList.indexOf(button)));
            graphics.renderItem(stack, xPos, yPos);
            graphics.renderItemDecorations(this.font, stack, xPos, yPos);
            if ( mouseX >= button.getX() && mouseY >= button.getY() && mouseX < button.getX() + button.getWidth() && mouseY < button.getY() + button.getHeight() ) {
                graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
            }
        }
        else ModScreen.drawTexture(TEXTURE, xPos, yPos, 176, 0, 16, 16, 256, 256, graphics);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }
}
