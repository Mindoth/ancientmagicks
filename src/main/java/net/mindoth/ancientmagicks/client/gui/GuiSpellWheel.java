package net.mindoth.ancientmagicks.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.network.PacketOpenWandGui;
import net.mindoth.ancientmagicks.network.PacketSetStaffSlot;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

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
    public CompoundNBT nbt;
    private int selectedItem;
    private final List<ItemStack> itemList;

    public GuiSpellWheel(List<ItemStack> stackList, CompoundNBT nbt) {
        super(new StringTextComponent(""));
        this.nbt = nbt;
        this.closing = false;
        this.minecraft = Minecraft.getInstance();
        this.selectedItem = -1;
        this.itemList = stackList;
        this.itemList.add(0, ItemStack.EMPTY);
    }

    public static void open(List<ItemStack> itemList, CompoundNBT nbt) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        PlayerEntity player = MINECRAFT.player;
        if ( MINECRAFT.screen instanceof GuiSpellWheel ) {
            player.closeContainer();
            return;
        }
        if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem && MINECRAFT.screen == null ) {
            Minecraft.getInstance().setScreen(new GuiSpellWheel(itemList, nbt));
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if ( this.selectedItem != -1 ) {
            ItemStack clickedItem = this.itemList.get(this.selectedItem);
            PlayerEntity player = MINECRAFT.player;
            if ( CastingItem.getHeldCastingItem(player).getItem() instanceof StaffItem ) {
                ItemStack staff = CastingItem.getHeldCastingItem(player);
                staff.setTag(this.nbt);
                if ( CastingItem.isValidCastingItem(staff) ) {
                    if ( clickedItem.isEmpty() ) AncientMagicksNetwork.sendToServer(new PacketOpenWandGui() );
                    else {
                        WandData data = CastingItem.getData(staff);
                        List<CompoundNBT> list = Lists.newArrayList();
                        for ( int i = 0; i < data.getHandler().getSlots(); i++ ) list.add(data.getHandler().getStackInSlot(i).getOrCreateTag());
                        AncientMagicksNetwork.sendToServer(new PacketSetStaffSlot(clickedItem.getTag()));
                        player.closeContainer();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        super.render(ms, mouseX, mouseY, partialTicks);
        float openAnimation = closing ? 1.0F - totalTime / OPEN_ANIMATION_LENGTH : totalTime / OPEN_ANIMATION_LENGTH;
        float currTick = minecraft.getFrameTime();
        totalTime += (currTick + extraTick - prevTick) / 20F;
        extraTick = 0;
        prevTick = currTick;

        float animProgress = MathHelper.clamp(openAnimation, 0, 1);
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

        RenderSystem.pushMatrix();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
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
            else drawSlice(buffer, x, y, 10, radiusIn, radiusOut, s, e, 55, 55, 55, 93);
        }

        tessellator.end();
        RenderSystem.enableTexture();

        int comboX = (this.minecraft.getWindow().getGuiScaledWidth() / 2) - 91;
        int comboY = this.minecraft.getWindow().getGuiScaledHeight() - 22;


        if ( hasMouseOver && mousedOverSlot != -1 ) {

            //Show SpellTablet's rune list on new Hotbar
            GuiSpellWheel.drawItemTexture(new ResourceLocation("minecraft", "textures/gui/widgets.png"),
                    comboX, comboY, 0, 0, 182, 22, 256, 256, ms);
            if ( this.itemList.get(mousedOverSlot).getItem() instanceof SpellTabletItem
                    && this.itemList.get(mousedOverSlot).hasTag()
                    && this.itemList.get(mousedOverSlot).getTag().contains("UUID") ) {
                List<ItemStack> tabletList = CastingItem.getWandList(this.itemList.get(mousedOverSlot));
                for ( int i = 0; i < tabletList.size(); ++i ) {
                    int ingX = comboX + 3 + (i * 20);
                    int ingY = comboY + 3;
                    ItemStack tabletSlot = tabletList.get(i);
                    this.itemRenderer.renderAndDecorateFakeItem(tabletSlot, ingX, ingY);
                    this.itemRenderer.renderGuiItemDecorations(this.font, tabletSlot, ingX, ingY);
                }
            }

            //Show SpellTablet's name
            String stackName = new TranslationTextComponent("tooltip.ancientmagicks.empty").getString();
            int color = 16777215;
            if ( !this.itemList.get(mousedOverSlot).isEmpty() ) {
                stackName = this.itemList.get(mousedOverSlot).getHoverName().getString();
                color = 5635925;
            }
            drawCenteredString(ms, font, stackName, width / 2, ((height - font.lineHeight) / 16), color);
        }

        RenderHelper.turnBackOn();
        RenderSystem.popMatrix();

        for ( int i = 0; i < numberOfSlices; i++ ) {
            int magnifier = 24;
            float middle = ((i / (float)numberOfSlices) + 0.25F) * 2 * (float)Math.PI;
            float posX = x - ((float)magnifier / 2) + itemRadius * (float)Math.cos(middle);
            float posY = y - ((float)magnifier / 2) + itemRadius * (float)Math.sin(middle);

            RenderSystem.disableRescaleNormal();
            RenderHelper.turnOff();
            RenderSystem.disableLighting();
            RenderSystem.disableDepthTest();

            //Spell Item icons and slot borders on radial wheel
            int slotX = (int)posX + 4;
            int slotY = (int)posY + 4;
            ItemStack slot = this.itemList.get(i);
             if ( !slot.isEmpty() ) {
                 GuiSpellWheel.drawItemTexture(new ResourceLocation("minecraft", "textures/gui/widgets.png"),
                         slotX - 3, slotY - 3, 24, 23, 22, 22, 256, 256, ms);
             }
            this.itemRenderer.renderAndDecorateFakeItem(slot, slotX, slotY);
            this.itemRenderer.renderGuiItemDecorations(this.font, slot, slotX, slotY);
        }
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
        int sections = Math.max(1, MathHelper.ceil(angle / this.PRECISION));

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
    public static void overlayEvent(RenderGameOverlayEvent.Pre event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellWheel) {
            if ( event.getType() == RenderGameOverlayEvent.ElementType.ALL ) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void updateInputEvent(InputUpdateEvent event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellWheel) {
            GameSettings settings = Minecraft.getInstance().options;
            MovementInput eInput = event.getMovementInput();
            eInput.up = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyUp.getKey().getValue());
            eInput.down = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyDown.getKey().getValue());
            eInput.left = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyLeft.getKey().getValue());
            eInput.right = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyRight.getKey().getValue());

            eInput.forwardImpulse = eInput.up == eInput.down ? 0.0F : (eInput.up ? 1.0F : -1.0F);
            eInput.leftImpulse = eInput.left == eInput.right ? 0.0F : (eInput.left ? 1.0F : -1.0F);
            eInput.jumping = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyJump.getKey().getValue());
            eInput.shiftKeyDown = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyShift.getKey().getValue());
            if ( Minecraft.getInstance().player.isMovingSlowly() ) {
                eInput.leftImpulse = (float)((double)eInput.leftImpulse * 0.3D);
                eInput.forwardImpulse = (float)((double)eInput.forwardImpulse * 0.3D);
            }
        }
    }

    public static void drawItemTexture(ResourceLocation resourceLocation, int x, int y, int u, int v, int w, int h, int fileWidth, int fileHeight, MatrixStack stack) {
        Minecraft.getInstance().textureManager.bind(resourceLocation);
        blit(stack,x, y, u, v, w, h, fileWidth, fileHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
