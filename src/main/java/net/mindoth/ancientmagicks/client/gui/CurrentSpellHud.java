package net.mindoth.ancientmagicks.client.gui;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.registries.ForgeRegistries;

public class CurrentSpellHud {

    public static final IGuiOverlay OVERLAY = CurrentSpellHud::renderOverlay;
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    public static final ResourceLocation SLOT_TEXTURE = new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/square.png");

    public static boolean shouldDisplaySlot() {
        ItemStack mainHandItem = MINECRAFT.player.getMainHandItem();
        ItemStack offHandItem = MINECRAFT.player.getOffhandItem();

        return !(MINECRAFT.screen instanceof GuiSpellWheel) && (mainHandItem.getItem() instanceof CastingItem || offHandItem.getItem() instanceof CastingItem);
    }

    public static ItemStack currentSpell() {
        ItemStack state = new ItemStack(Items.AIR);
        ItemStack mainHandItem = MINECRAFT.player.getMainHandItem();
        ItemStack offHandItem = MINECRAFT.player.getOffhandItem();

        if ( shouldDisplaySlot() ) {
            ItemStack castingItem = null;
            if ( mainHandItem.getItem() instanceof CastingItem && CastingItem.isValidCastingItem(mainHandItem) && mainHandItem.getTag().contains("am_spellrune") ) castingItem = mainHandItem;
            else if ( offHandItem.getItem() instanceof CastingItem && CastingItem.isValidCastingItem(offHandItem) && offHandItem.getTag().contains("am_spellrune") ) castingItem = offHandItem;

            if ( castingItem != null ) {
                Item spellRune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(castingItem.getTag().getString("am_spellrune")));
                if ( spellRune instanceof SpellRuneItem ) state = new ItemStack(spellRune);
            }
        }

        return state;
    }

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float pt, int width, int height) {
        if ( !shouldDisplaySlot() ) return;

        int resultSlotX = (MINECRAFT.getWindow().getGuiScaledWidth() / 2) + 98;
        int resultSlotY = MINECRAFT.getWindow().getGuiScaledHeight() - 22;
        GuiSpellWheel.drawSlotTexture(SLOT_TEXTURE, resultSlotX, resultSlotY, 0, 0, 22, 22, 22, 22, graphics);

        if ( currentSpell() != null ) {
            graphics.renderItem(currentSpell(), resultSlotX + 3, resultSlotY + 3);
            graphics.renderItemDecorations(gui.getFont(), currentSpell(), resultSlotX + 3, resultSlotY + 3);
        }
    }
}
