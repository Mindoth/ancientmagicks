package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagic;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSetSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiSpellWheel extends AncientMagicksScreen {

    private static final float PRECISION = 5.0F;
    private boolean closing;
    final float OPEN_ANIMATION_LENGTH = 0.5F;
    private float totalTime;
    private float prevTick;
    private float extraTick;
    private int selectedItem;
    private final List<ItemStack> itemList;
    private final List<ColorRuneItem> comboList = Lists.newArrayList();
    private SpellItem comboResult;
    private HashMap<Integer, ItemStack> possibleResults;
    private final InteractionHand hand;
    private final String hotbar;

    public GuiSpellWheel(List<ItemStack> stackList, boolean isOffhand) {
        super(Component.literal(""));
        this.closing = false;
        minecraft = Minecraft.getInstance();
        this.selectedItem = -1;
        if ( stackList.isEmpty() ) this.itemList = List.of(ItemStack.EMPTY);
        else this.itemList = stackList;
        this.hand = isOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        int size = AncientMagicks.comboSizeCalc();
        if ( size == 4 ) this.hotbar = "hotbar4.png";
        else if ( size == 5 ) this.hotbar = "hotbar5.png";
        else if ( size == 6 ) this.hotbar = "hotbar6.png";
        else this.hotbar = "hotbar3.png";
        this.possibleResults = new HashMap<>();
    }

    public static void open(List<ItemStack> itemList, boolean isOffHand) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        Player player = MINECRAFT.player;
        if ( MINECRAFT.screen == null ) MINECRAFT.setScreen(new GuiSpellWheel(itemList, isOffHand));
        else if ( MINECRAFT.screen instanceof GuiSpellWheel ) player.closeContainer();
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if ( this.selectedItem != -1 ) {
            this.possibleResults = new HashMap<>();
            ItemStack clickedItem = this.itemList.get(this.selectedItem);
            if ( clickedItem.getItem() instanceof ColorRuneItem ) {
                if ( this.comboList.size() < AncientMagicks.comboSizeCalc() ) this.comboList.add((ColorRuneItem)clickedItem.getItem());
                else {
                    this.comboList.remove(0);
                    this.comboList.add((ColorRuneItem)clickedItem.getItem());
                }
            }
            if ( getComboResult(this.comboList) != null ) {
                this.comboResult = getComboResult(this.comboList);
                String spellString = String.valueOf(ForgeRegistries.ITEMS.getKey(this.comboResult));
                CompoundTag tag = new CompoundTag();
                tag.putString(PlayerMagic.AM_SPELL, spellString);
                AncientMagicksNetwork.sendToServer(new PacketSetSpell(tag));
                ClientMagicData.setCurrentSpell(spellString);
            }
            else this.comboResult = null;
        }
        return true;
    }

    private SpellItem getComboResult(List<ColorRuneItem> comboList) {
        SpellItem spell = ColorRuneItem.checkForSpellCombo(comboList);
        if ( spell != null && (ClientMagicData.isSpellKnown(spell) || minecraft.player.isCreative()) ) {
            return spell;
        }
        else return null;
    }

    @Override
    public void tick() {
        if ( this.totalTime != this.OPEN_ANIMATION_LENGTH ) this.extraTick++;
        Player player = minecraft.player;
        Item handItem = player.getItemInHand(this.hand).getItem();
        if ( !(handItem instanceof StaffItem) ) player.closeContainer();
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGuiOverlayEvent.Pre event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellWheel ) {
            if ( event.getOverlay() == VanillaGuiOverlay.HOTBAR.type() || event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type() ) event.setCanceled(true);
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

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        PoseStack ms = graphics.pose();
        float openAnimation = this.closing ? 1.0F - this.totalTime / this.OPEN_ANIMATION_LENGTH : this.totalTime / this.OPEN_ANIMATION_LENGTH;
        float currTick = minecraft.getFrameTime();
        this.totalTime += (currTick + this.extraTick - this.prevTick) / 20F;
        this.extraTick = 0;
        this.prevTick = currTick;

        float animProgress = Mth.clamp(openAnimation, 0, 1);
        animProgress = (float) (1 - Math.pow(1 - animProgress, 3));
        float radiusIn = Math.max(0.1F, 45 * animProgress) + 7;
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

        //Show comboList on new Hotbar
        int comboX = (minecraft.getWindow().getGuiScaledWidth() / 2) - 31 - (10 * (AncientMagicks.comboSizeCalc() - 3));
        int comboY = minecraft.getWindow().getGuiScaledHeight() - 22;
        int fileWidth = 62 + (20 * (AncientMagicks.comboSizeCalc() - 3));

        drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/" + this.hotbar),
                comboX, comboY, 0, 0, fileWidth, 22, fileWidth, 22, graphics);
        if ( !this.comboList.isEmpty() ) {
            for ( int i = 0; i < this.comboList.size(); ++i ) {
                int ingX = comboX + 3 + (i * 20);
                int ingY = comboY + 3;
                ItemStack comboRune = new ItemStack(this.comboList.get(i));
                renderItemWithDecorations(graphics, comboRune, ingX, ingY);
            }
        }

        ms.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        boolean hasMouseOver = false;
        int mousedOverSlot = -1;

        if ( !this.closing ) {
            this.selectedItem = -1;
            for ( int i = 0; i < numberOfSlices; i++ ) {
                float s = (((i - 0.5F) / (float)numberOfSlices) + 0.25F) * 360;
                float e = (((i + 0.5F) / (float)numberOfSlices) + 0.25F) * 360;
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

        tesselator.end();
        RenderSystem.disableBlend();

        //Show comboResult square in the middle
        int resultSlotX = minecraft.getWindow().getGuiScaledWidth() / 2 - 11;
        int resultSlotY = (minecraft.getWindow().getGuiScaledHeight() / 2 - 11) + 10;
        if ( this.comboResult != null ) {
            int posX = resultSlotX + 3;
            int posY = resultSlotY + 3;
            ItemStack slot = new ItemStack(this.comboResult);

            //Spell name
            String name = I18n.get(slot.getDescriptionId());
            List<Component> componentList = Lists.newArrayList();
            int rowLimit = 100;
            if ( this.font.width(name) < rowLimit ) componentList.add(Component.literal(name));
            else {
                List<String> lines = putTextToLines(name, this.font, rowLimit);
                lines.forEach(s -> componentList.add(Component.literal(s)));
            }
            int xInfo = width / 2;
            int yInfo = (height - this.font.lineHeight) / 2 - 20;
            for ( int i = 0; i < componentList.size(); i++ ) {
                Component component = componentList.get(i);
                int height = yInfo - (componentList.size() - 1) * 4;
                graphics.drawCenteredString(this.font, component, xInfo, height + this.font.lineHeight * (i + 1), 16777215);
                //graphics.drawCenteredString(this.font, name, width / 2, (height - this.font.lineHeight) / 2 - 8, 16777215);
            }

            //Square slot
            drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/square.png"),
                    resultSlotX, resultSlotY, 0, 0, 22, 22, 22, 22, graphics);

            //Item and its decorations
            renderItemWithDecorations(graphics, slot, posX, posY);
        }

        ms.popPose();

        //Spell item and hover tooltip on radial wheel
        for ( int i = 0; i < numberOfSlices; i++ ) {
            int magnifier = 24;
            float middle = ((i / (float)numberOfSlices) + 0.25F) * 2 * (float)Math.PI;
            int posX = (int)(x - ((float)magnifier * 0.5F) + itemRadius * (float)Math.cos(middle)) + 4;
            int posY = (int)(y - ((float)magnifier * 0.5F) + itemRadius * (float)Math.sin(middle)) + 4;

            ItemStack slot = this.itemList.get(i);

            RenderSystem.disableDepthTest();

            if ( !slot.isEmpty() ) {
                drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/square.png"),
                        posX - 3, posY - 3, 0, 0, 22, 22, 22, 22, graphics);
            }

            ms.pushPose();
            ms.popPose();

            renderItemWithDecorations(graphics, slot, posX, posY);

            //Possibility vision
            Player player = minecraft.player;
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            if ( player.isCreative() || main.getItem() instanceof SpellBookItem || off.getItem() instanceof SpellBookItem ) {
                float dimItemRadius = (radiusIn + radiusOut) * 0.75F;
                int dimPosX = (int)(x - ((float)magnifier * 0.5F) + dimItemRadius * (float)Math.cos(middle)) + 4;
                int dimPosY = (int)(y - ((float)magnifier * 0.5F) + dimItemRadius * (float)Math.sin(middle)) + 4;
                List<ColorRuneItem> tempList = Lists.newArrayList();
                tempList.addAll(this.comboList);
                if ( this.comboList.size() == AncientMagicks.comboSizeCalc() ) tempList.remove(0);
                if ( this.comboList.size() == AncientMagicks.comboSizeCalc() || this.comboList.size() == AncientMagicks.comboSizeCalc() - 1 ) {
                    if ( slot.getItem() instanceof ColorRuneItem colorRuneItem ) tempList.add(colorRuneItem);
                    if ( getComboResult(tempList) != null ) {
                        ItemStack possibleResult = new ItemStack(getComboResult(tempList));
                        this.possibleResults.put(i, possibleResult);
                        renderItemWithDecorations(graphics, possibleResult, dimPosX, dimPosY);
                    }
                }
            }
        }

        //Hover tooltip
        for ( ItemStack slot : this.itemList ) {
            RenderSystem.disableDepthTest();
            ms.pushPose();

            if ( hasMouseOver && mousedOverSlot != -1 ) {
                if ( !this.itemList.get(mousedOverSlot).isEmpty() && slot.equals(this.itemList.get(mousedOverSlot)) ) {
                    ItemStack tooltipItem = this.possibleResults.containsKey(mousedOverSlot) ? this.possibleResults.get(mousedOverSlot) : this.itemList.get(mousedOverSlot);
                    graphics.renderTooltip(this.font, tooltipItem, mouseX, mouseY);
                }
            }

            ms.popPose();
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
}
