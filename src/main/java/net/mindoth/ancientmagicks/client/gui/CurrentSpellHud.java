package net.mindoth.ancientmagicks.client.gui;

import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.registries.ForgeRegistries;

public class CurrentSpellHud {

    public static final IGuiOverlay OVERLAY = CurrentSpellHud::renderOverlay;
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    public static final ResourceLocation SLOT_TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    public static boolean shouldDisplaySlot() {
        ItemStack main = MINECRAFT.player.getMainHandItem();
        ItemStack off = MINECRAFT.player.getOffhandItem();

        return !(MINECRAFT.screen instanceof GuiSpellWheel) && CastingItem.isValidCastingItem(main) || CastingItem.isValidCastingItem(off);
    }

    public static ItemStack currentSpell() {
        ItemStack state = new ItemStack(Items.AIR);
        ItemStack main = MINECRAFT.player.getMainHandItem();
        ItemStack off = MINECRAFT.player.getOffhandItem();

        if ( shouldDisplaySlot() ) {
            ItemStack castingItem = null;
            if ( main.getItem() instanceof CastingItem && CastingItem.isValidCastingItem(main) ) castingItem = main;
            else if ( off.getItem() instanceof CastingItem && CastingItem.isValidCastingItem(off) ) castingItem = off;

            if ( castingItem != null && ClientSpellData.getCurrentSpell() != null ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ClientSpellData.getCurrentSpell()));
                if ( item instanceof SpellItem) state = new ItemStack(item);
            }
        }

        return state;
    }

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float pt, int width, int height) {
        if ( !shouldDisplaySlot() ) return;
        int halfX = (MINECRAFT.getWindow().getGuiScaledWidth() / 2);
        int resultSlotX = MINECRAFT.options.mainHand().get() == HumanoidArm.RIGHT ? halfX + 98 : halfX - 120;
        int resultSlotY = MINECRAFT.getWindow().getGuiScaledHeight() - 22;
        GuiSpellWheel.drawSlotTexture(SLOT_TEXTURE, resultSlotX, resultSlotY - 1, 60, 22, 24, 24, 256, 256, graphics);

        if ( currentSpell().getItem() != Items.AIR ) {
            //graphics.renderItem(currentSpell(), resultSlotX + 3, resultSlotY + 3);
            String id = currentSpell().getItem().toString();
            GuiSpellWheel.drawSlotTexture(new ResourceLocation("ancientmagicks", "textures/spell/" + id + ".png"),
                    resultSlotX + 3, resultSlotY + 3, 0, 0, 16, 16, 16, 16, graphics);
            graphics.renderItemDecorations(gui.getFont(), currentSpell(), resultSlotX + 3, resultSlotY + 3);
        }
    }
}
