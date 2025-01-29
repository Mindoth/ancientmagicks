package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class AncientTabletScreen extends AncientMagicksScreen {

    private final List<ItemStack> itemList;
    private final List<Button> slotList = Lists.newArrayList();
    private final ItemStack arcaneDust = new ItemStack(AncientMagicksItems.ARCANE_DUST.get());
    private Button outputSlot;

    public AncientTabletScreen(List<ItemStack> stackList) {
        super(Component.literal(""));
        if ( stackList.isEmpty() ) this.itemList = List.of(ItemStack.EMPTY);
        else this.itemList = stackList;
    }

    public static void open(List<ItemStack> itemList) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if ( MINECRAFT.screen == null ) MINECRAFT.setScreen(new AncientTabletScreen(itemList));
    }

    @Override
    protected void init() {
        super.init();

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        for ( int i = 0; i < this.itemList.size(); i++ ) {
            int xPos = x - 57;
            if ( i == 1 || i == 4 || i == 7 ) xPos += 18;
            if ( i == 2 || i == 5 || i == 8 ) xPos += 36;
            int yPos = y - 26;
            if ( i == 3 || i == 4 || i == 5 ) yPos += 18;
            if ( i == 6 || i == 7 || i == 8 ) yPos += 36;
            buildSlothButton(xPos, yPos);
        }
        int xPos = x + 37;
        int yPos = y - 8;
        this.outputSlot = buildSlothButton(xPos, yPos);
    }

    private Button buildSlothButton(int xPos, int yPos) {
        Button button = addRenderableOnly(Button.builder(Component.literal(""), this::handleSlotButton)
                .bounds(xPos - 1, yPos - 1, 18, 18)
                .build());
        if ( this.slotList.size() < 9 ) this.slotList.add(button);
        return button;
    }

    private void handleSlotButton(Button button) {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/ancient_tablet_screen.png"),
                x - 73, y - 90, 0, 0, 146, 180, 146, 180, graphics);

        for ( int i = 0; i < this.itemList.size(); i++ ) {
            int xPos = x - 57;
            if ( i == 1 || i == 4 || i == 7 ) xPos += 18;
            if ( i == 2 || i == 5 || i == 8 ) xPos += 36;
            int yPos = y - 26;
            if ( i == 3 || i == 4 || i == 5 ) yPos += 18;
            if ( i == 6 || i == 7 || i == 8 ) yPos += 36;
            graphics.renderItem(this.itemList.get(i), xPos, yPos);
            graphics.renderItemDecorations(this.font, this.itemList.get(i), xPos, yPos);
            if ( this.slotList.get(i).isHovered() ) {
                graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
                graphics.renderTooltip(this.font, this.itemList.get(i), mouseX, mouseY);
            }
        }

        int xPos = x + 37;
        int yPos = y - 8;
        graphics.renderItem(this.arcaneDust, xPos, yPos);
        graphics.renderItemDecorations(this.font, this.arcaneDust, xPos, yPos);
        if ( this.outputSlot.isHovered() ) {
            graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
            graphics.renderTooltip(this.font, this.arcaneDust, mouseX, mouseY);
        }
    }
}
