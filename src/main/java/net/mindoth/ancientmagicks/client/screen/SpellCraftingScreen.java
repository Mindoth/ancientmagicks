package net.mindoth.ancientmagicks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.menu.ComponentSlot;
import net.mindoth.ancientmagicks.client.menu.SpellCraftingMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpellCraftingScreen extends AbstractContainerScreen<SpellCraftingMenu> implements ContainerListener {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/spell_crafting_screen.png");
    private EditBox name;

    public SpellCraftingScreen(SpellCraftingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight += 9;
        inventoryLabelY += 9;
    }

    protected void subInit() {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, x + 17, y + 44, 142, 12, Component.translatable("container.ancientmagicks.name"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(true);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(true);
    }

    private void onNameChanged(String string) {
        Slot slot = this.menu.getSlot(0);
        if ( slot.hasItem() ) {
            String name = string;
            if ( !slot.getItem().hasCustomHoverName() && string.equals(slot.getItem().getHoverName().getString()) ) name = "";
            if ( this.menu.setItemName(name) ) this.minecraft.player.connection.send(new ServerboundRenameItemPacket(name));
        }
    }

    @Override
    public void slotChanged(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack) {
        if ( pSlotInd == 0 ) {
            this.name.setEditable(!pStack.isEmpty());
            this.setFocused(this.name);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            this.minecraft.player.closeContainer();
        }
        return !this.name.keyPressed(pKeyCode, pScanCode, pModifiers) && !this.name.canConsumeInput() ? super.keyPressed(pKeyCode, pScanCode, pModifiers) : true;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    @Override
    protected void init() {
        super.init();
        subInit();
        this.menu.addSlotListener(this);
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.name.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, this.imageHeight);
    }
}
