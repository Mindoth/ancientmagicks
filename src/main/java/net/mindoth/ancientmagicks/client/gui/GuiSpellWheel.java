package net.mindoth.ancientmagicks.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSetSpell;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiSpellWheel extends Screen {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final float PRECISION = 5.0F;
    private boolean closing;
    final float OPEN_ANIMATION_LENGTH = 0.5F;
    private float totalTime;
    private float prevTick;
    private float extraTick;
    private int selectedItem;
    private final List<ItemStack> itemList;
    private final List<ColorRuneItem> comboList = Lists.newArrayList();
    private TabletItem comboResult;

    public GuiSpellWheel(List<ItemStack> stackList) {
        super(Component.literal(""));
        this.closing = false;
        this.minecraft = Minecraft.getInstance();
        this.selectedItem = -1;
        if ( !stackList.isEmpty() ) this.itemList = stackList;
        else this.itemList = List.of(new ItemStack(Items.AIR));
    }

    public static void open(List<ItemStack> itemList) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        Player player = MINECRAFT.player;
        if ( MINECRAFT.screen instanceof GuiSpellWheel ) {
            player.closeContainer();
            return;
        }
        if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem && MINECRAFT.screen == null ) {
            Minecraft.getInstance().setScreen(new GuiSpellWheel(itemList));
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if ( this.selectedItem != -1 ) {
            ItemStack clickedItem = this.itemList.get(this.selectedItem);
            Player player = MINECRAFT.player;
            if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem ) {
                if ( clickedItem.getItem() instanceof ColorRuneItem ) {
                    if ( this.comboList.size() < 9 ) this.comboList.add((ColorRuneItem)clickedItem.getItem());
                    else {
                        this.comboList.remove(0);
                        this.comboList.add((ColorRuneItem)clickedItem.getItem());
                    }
                }
                if ( ColorRuneItem.checkForSpellCombo(this.comboList) != null ) {
                    this.comboResult = ColorRuneItem.checkForSpellCombo(this.comboList);
                    String spellString = String.valueOf(ForgeRegistries.ITEMS.getKey(this.comboResult));
                    CompoundTag tag = new CompoundTag();
                    tag.putString("am_spell", spellString);
                    AncientMagicksNetwork.sendToServer(new PacketSetSpell(tag));
                    ClientSpellData.set(spellString);
                }
                else this.comboResult = null;
            }
        }
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        PoseStack ms = graphics.pose();
        float openAnimation = closing ? 1.0F - totalTime / OPEN_ANIMATION_LENGTH : totalTime / OPEN_ANIMATION_LENGTH;
        float currTick = minecraft.getFrameTime();
        totalTime += (currTick + extraTick - prevTick) / 20F;
        extraTick = 0;
        prevTick = currTick;

        float animProgress = Mth.clamp(openAnimation, 0, 1);
        animProgress = (float) (1 - Math.pow(1 - animProgress, 3));
        float radiusIn = Math.max(0.1F, 45 * animProgress);
        float radiusOut = radiusIn * 2;
        float itemRadius = (radiusIn + radiusOut) * 0.5F;
        int x = width / 2;
        int y = height / 2;

        int numberOfSlices = this.itemList.size();

        double a = Math.toDegrees(Math.atan2(mouseY - y, mouseX - x));
        double d = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));
        float s0 = ((-0.5F / (float)numberOfSlices) + 0.25F) * 360;
        if ( a < s0 ) {
            a += 360;
        }

        ms.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        boolean hasMouseOver = false;
        int mousedOverSlot = -1;

        if ( !closing ) {
            this.selectedItem = -1;
            for ( int i = 0; i < numberOfSlices; i++ ) {
                float s = (((i - 0.5F) / (float)numberOfSlices) + 0.25F) * 360;
                float e = (((i + 0.5F) / (float)numberOfSlices) + 0.25F) * 360;
                //if ( a >= s && a < e && d >= radiusIn && d < radiusOut) {
                if ( a >= s && a < e && d >= radiusIn && d < radiusOut * 10 ) {
                    this.selectedItem = i;
                    break;
                }
            }
        }

        for ( int i = 0; i < numberOfSlices; i++ ) {
            float s = (((i - 0.5F) / (float)numberOfSlices) + 0.25F) * 360;
            float e = (((i + 0.5F) / (float)numberOfSlices) + 0.25F) * 360;
            if ( this.selectedItem == i ) {
                drawSlice(buffer, x, y, numberOfSlices, radiusIn, radiusOut * 1.05F, s, e, 198, 198, 198, 100);
                hasMouseOver = true;
                mousedOverSlot = this.selectedItem;
            }
            else drawSlice(buffer, x, y, 10, radiusIn, radiusOut, s, e, 55, 55, 55, 120);
        }

        tessellator.end();
        RenderSystem.disableBlend();

        //Show comboList on new Hotbar
        int comboX = (this.minecraft.getWindow().getGuiScaledWidth() / 2) - 91;
        int comboY = this.minecraft.getWindow().getGuiScaledHeight() - 22;

        GuiSpellWheel.drawSlotTexture(new ResourceLocation("ancientmagicks", "textures/gui/hotbar.png"),
                comboX, comboY, 0, 0, 182, 22, 182, 22, graphics);
        if ( !this.comboList.isEmpty() ) {
            for ( int i = 0; i < this.comboList.size(); ++i ) {
                int ingX = comboX + 3 + (i * 20);
                int ingY = comboY + 3;
                ItemStack comboRune = new ItemStack(this.comboList.get(i));
                graphics.renderItem(comboRune, ingX, ingY);
                graphics.renderItemDecorations(this.font, comboRune, ingX, ingY);
            }
        }

        //Show comboResult square in the middle
        int resultSlotX = this.minecraft.getWindow().getGuiScaledWidth() / 2 - 11;
        int resultSlotY = (this.minecraft.getWindow().getGuiScaledHeight() / 2 - 11) + 10;
        if ( this.comboResult != null ) {
            int posX = resultSlotX + 3;
            int posY = resultSlotY + 3;
            ItemStack slot = new ItemStack(this.comboResult);

            //Item and its decorations
            graphics.renderItem(slot, posX, posY);
            graphics.renderItemDecorations(this.font, slot, posX, posY);

            //Square slot
            GuiSpellWheel.drawSlotTexture(new ResourceLocation("ancientmagicks", "textures/gui/square.png"),
                    resultSlotX, resultSlotY, 0, 0, 22, 22, 22, 22, graphics);

            //Spell name
            String tabletComponent = I18n.get("key.ancientmagicks.tablet_component");
            String name = slot.getHoverName().getString().replace(tabletComponent, "");
            graphics.drawCenteredString(this.font, name, width / 2, (height - this.font.lineHeight) / 2 - 8, 16777215);
        }

        //Spell item and hover tooltip on radial wheel
        for ( int i = 0; i < numberOfSlices; i++ ) {
            int magnifier = 24;
            float middle = ((i / (float)numberOfSlices) + 0.25F) * 2 * (float)Math.PI;
            int posX = (int)(x - ((float)magnifier / 2) + itemRadius * (float)Math.cos(middle)) + 4;
            int posY = (int)(y - ((float)magnifier / 2) + itemRadius * (float)Math.sin(middle)) + 4;
            ItemStack slot = this.itemList.get(i);

            if ( hasMouseOver && mousedOverSlot != -1 ) {
                if ( !this.itemList.get(mousedOverSlot).isEmpty() && slot.equals(this.itemList.get(mousedOverSlot)) ) {
                    graphics.renderTooltip(this.font, this.itemList.get(mousedOverSlot), mouseX, mouseY);
                }
            }

            if ( !slot.isEmpty() ) {
                GuiSpellWheel.drawSlotTexture(new ResourceLocation("ancientmagicks", "textures/gui/square.png"),
                        posX - 3, posY - 3, 0, 0, 22, 22, 22, 22, graphics);
            }

            graphics.renderItem(slot, posX, posY);
            graphics.renderItemDecorations(this.font, slot, posX, posY);
        }

        //RenderSystem.disableDepthTest();
        //ms.popPose();
    }

    @Override
    public void tick() {
        if ( this.totalTime != this.OPEN_ANIMATION_LENGTH ) {
            this.extraTick++;
        }
    }

    private void drawSlice(BufferBuilder buffer, float x, float y, float z, float radiusIn, float radiusOut, float startAngle, float endAngle,
                           int r, int g, int b, int a) {
        float angle = endAngle - startAngle;
        int sections = Math.max(1, Mth.ceil(angle / this.PRECISION));

        startAngle = (float)Math.toRadians(startAngle);
        endAngle = (float)Math.toRadians(endAngle);
        angle = endAngle - startAngle;

        for ( int i = 0; i < sections; i++ ) {
            float middle = startAngle + (i / (float)sections) * angle;
            float middle2 = startAngle + ((i + 1) / (float)sections) * angle;

            float pos1InX = x + radiusIn * (float)Math.cos(middle);
            float pos1InY = y + radiusIn * (float)Math.sin(middle);
            float pos1OutX = x + radiusOut * (float)Math.cos(middle);
            float pos1OutY = y + radiusOut * (float)Math.sin(middle);
            float pos2OutX = x + radiusOut * (float)Math.cos(middle2);
            float pos2OutY = y + radiusOut * (float)Math.sin(middle2);
            float pos2InX = x + radiusIn * (float)Math.cos(middle2);
            float pos2InY = y + radiusIn * (float)Math.sin(middle2);

            buffer.vertex(pos1OutX, pos1OutY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos1InX, pos1InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2InX, pos2InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2OutX, pos2OutY, z).color(r, g, b, a).endVertex();
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        int adjustedKey = key - 48;
        if ( adjustedKey >= 0 && adjustedKey < 10 ) {
            this.selectedItem = adjustedKey == 0 ? 10 : adjustedKey;
            mouseClicked(0,0,0);
            return true;
        }
        return super.keyPressed(key, scanCode, modifiers);
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGuiOverlayEvent.Pre event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellWheel ) {
            if ( event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()
                    || event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type()
                    || event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()
                    || event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type()
                    || event.getOverlay() == VanillaGuiOverlay.RECORD_OVERLAY.type()
                    || event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()
                    || event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()
                    || event.getOverlay() == VanillaGuiOverlay.MOUNT_HEALTH.type()
                    || event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()
                    || event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type() ) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void updateInputEvent(MovementInputUpdateEvent event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellWheel) {
            Options settings = Minecraft.getInstance().options;
            Input eInput = event.getInput();
            eInput.up = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyUp.getKey().getValue());
            eInput.down = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyDown.getKey().getValue());
            eInput.left = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyLeft.getKey().getValue());
            eInput.right = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyRight.getKey().getValue());

            eInput.forwardImpulse = eInput.up == eInput.down ? 0.0F : (eInput.up ? 1.0F : -1.0F);
            eInput.leftImpulse = eInput.left == eInput.right ? 0.0F : (eInput.left ? 1.0F : -1.0F);
            eInput.jumping = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyJump.getKey().getValue());
            eInput.shiftKeyDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyShift.getKey().getValue());
            if ( Minecraft.getInstance().player.isMovingSlowly() ) {
                eInput.leftImpulse = (float)((double)eInput.leftImpulse * 0.3D);
                eInput.forwardImpulse = (float)((double)eInput.forwardImpulse * 0.3D);
            }
        }
    }

    public static void drawSlotTexture(ResourceLocation resourceLocation, int x, int y, int u, int v, int w, int h, int fileWidth, int fileHeight, GuiGraphics graphics) {
        Minecraft.getInstance().textureManager.bindForSetup(resourceLocation);
        graphics.blit(resourceLocation, x, y, u, v, w, h, fileWidth, fileHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
