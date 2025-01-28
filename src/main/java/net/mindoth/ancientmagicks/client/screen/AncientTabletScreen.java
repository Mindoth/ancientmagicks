package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class AncientTabletScreen extends AncientMagicksScreen {

    private final List<ItemStack> itemList;
    private final List<Button> slotList = Lists.newArrayList();
    private Button outputSlot;

    public AncientTabletScreen(List<ItemStack> stackList) {
        super(Component.literal(""));
        if ( !stackList.isEmpty() ) this.itemList = stackList;
        else this.itemList = List.of(new ItemStack(Items.AIR));
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
            Item item = this.itemList.get(i).getItem();
            buildButton(xPos, yPos, item);
        }
        int xPos = x + 37;
        int yPos = y - 8;
        this.outputSlot = buildButton(xPos, yPos, AncientMagicksItems.ARCANE_DUST.get());
    }

    private Button buildButton(int xPos, int yPos, Item item) {
        Button button = addRenderableOnly(Button.builder(Component.literal(""), this::handleSlotButton)
                .bounds(xPos, yPos, 16, 16)
                .tooltip(Tooltip.create(Component.translatable(item.getDescriptionId())))
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

        AncientTabletScreen.drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/ancient_tablet_screen.png"),
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
            if ( this.slotList.get(i).isHovered() ) graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
        }

        ItemStack arcaneDust = new ItemStack(AncientMagicksItems.ARCANE_DUST.get());
        int xPos = x + 37;
        int yPos = y - 8;
        graphics.renderItem(arcaneDust, xPos, yPos);
        graphics.renderItemDecorations(this.font, arcaneDust, xPos, yPos);
        if ( this.outputSlot.isHovered() ) graphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, Integer.MAX_VALUE);
    }

    public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int u, int v, int w, int h, int fileWidth, int fileHeight, GuiGraphics graphics) {
        graphics.blit(resourceLocation, x, y, u, v, w, h, fileWidth, fileHeight);
    }
}
