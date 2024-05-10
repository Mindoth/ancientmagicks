package net.mindoth.ancientmagicks.item.weapon;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public enum WandType {
    BEGINNER_WAND("beginner_wand", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, AncientMagicksItems.BEGINNER_WAND),
    BEGINNER_STAFF("beginner_staff", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, AncientMagicksItems.BEGINNER_STAFF),
    SPELL_TABLET("spell_tablet", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, AncientMagicksItems.SPELL_TABLET)/*,
    SPELLBOOK("beginner_wand", Rarity.COMMON, 3, 61, 1, 3, "tier1_gui.png", 176, 132, 7, 49, RunicItemsItems.SPELLBOOK),
    SPELLBOOK("spell_book", Rarity.COMMON, 6, 34, 1, 6, "tier2_gui.png", 176, 132, 7, 49, RunicItemsItems.SPELLBOOK),
    SPELLBOOK("spell_book", Rarity.COMMON, 9, 7, 1, 9, "tier3_gui.png", 176, 132, 7, 49, RunicItemsItems.SPELLBOOK),
    SPELLBOOK("spell_book", Rarity.COMMON, 14, 25, 2, 7, "tier4_gui.png", 176, 150, 7, 67, RunicItemsItems.SPELLBOOK),
    SPELLBOOK("spell_book", Rarity.COMMON, 18, 7, 2, 9, "tier5_gui.png", 176, 150, 7, 67, RunicItemsItems.SPELLBOOK),
    SPELLBOOK("spell_book", Rarity.COMMON, 24, 16, 3, 8, "tier6_gui.png", 176, 168, 7, 85, RunicItemsItems.SPELLBOOK),
    SPELLBOOK("spell_book", Rarity.COMMON, 27, 7, 3, 9, "tier7_gui.png", 176, 168, 7, 85, RunicItemsItems.SPELLBOOK)*/;

    public final Rarity rarity;
    public final int slots;
    public final int mySlotXOffset;
    public final ResourceLocation texture;
    public final int xSize;
    public final int ySize;
    public final int slotXOffset;
    public final int slotYOffset;
    public final int slotRows;
    public final int slotCols;
    public final String name;
    public final RegistryObject<Item> item;

    WandType(String name, Rarity rarity, int slots, int mySlotXOffset, int rows, int cols, String location, int xSize, int ySize, int slotXOffset, int slotYOffset, RegistryObject<Item> itemIn) {
        this.name = name;
        this.rarity = rarity;
        this.slots = slots;
        this.mySlotXOffset = mySlotXOffset;
        this.slotRows = rows;
        this.slotCols = cols;
        this.texture = new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/" + location);
        this.xSize = xSize;
        this.ySize = ySize;
        this.slotXOffset = slotXOffset;
        this.slotYOffset = slotYOffset;
        this.item = itemIn;
    }
}
