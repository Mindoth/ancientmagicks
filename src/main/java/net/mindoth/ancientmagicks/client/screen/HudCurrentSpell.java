package net.mindoth.ancientmagicks.client.screen;

import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.registries.ForgeRegistries;

public class HudCurrentSpell {

    public static final IGuiOverlay OVERLAY = HudCurrentSpell::renderOverlay;
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    public static final ResourceLocation SLOT_TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float pt, int width, int height) {
        if (shouldHideSlot()) return;
        int halfX = (MINECRAFT.getWindow().getGuiScaledWidth() / 2);
        int resultSlotX = MINECRAFT.options.mainHand().get() == HumanoidArm.RIGHT ? halfX + 98 : halfX - 120;
        int resultSlotY = MINECRAFT.getWindow().getGuiScaledHeight() - 22;
        AncientMagicksScreen.drawTexture(SLOT_TEXTURE, resultSlotX, resultSlotY - 1, 60, 22, 24, 24, 256, 256, graphics);

        if ( currentSpell().isEmpty() ) return;
        String id = currentSpell().getItem().toString();
        String modid = ForgeRegistries.ITEMS.getKey(currentSpell().getItem()).toString().split(":")[0];
        AncientMagicksScreen.drawTexture(new ResourceLocation(modid, "textures/item/spell/" + id + ".png"),
                resultSlotX + 3, resultSlotY + 3, 0, 0, 16, 16, 16, 16, graphics);
        graphics.renderItemDecorations(gui.getFont(), currentSpell(), resultSlotX + 3, resultSlotY + 3);
    }

    public static ItemStack currentSpell() {
        ItemStack state = ItemStack.EMPTY;
        ItemStack main = MINECRAFT.player.getMainHandItem();
        ItemStack off = MINECRAFT.player.getOffhandItem();

        if ( shouldHideSlot() ) return state;
        if ( main.getItem() instanceof CastingItem || off.getItem() instanceof CastingItem ) {
            ItemStack castingItem = main.getItem() instanceof CastingItem ? main : off;
            if ( ClientMagicData.getCurrentSpell() == null ) return state;
            Item spell;
            if ( castingItem.getItem() instanceof StaffItem ) spell = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ClientMagicData.getCurrentSpell()));
            else spell = SpecialCastingItem.getStoredSpell(castingItem);
            if ( spell instanceof SpellItem ) state = new ItemStack(spell);
        }

        return state;
    }

    public static boolean shouldHideSlot() {
        Player player = MINECRAFT.player;
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        return (MINECRAFT.screen instanceof GuiSpellWheel || player.isSpectator()) || (!CastingItem.isValidCastingItem(main) && !CastingItem.isValidCastingItem(off));
    }
}
