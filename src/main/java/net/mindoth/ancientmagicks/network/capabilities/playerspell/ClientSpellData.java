package net.mindoth.ancientmagicks.network.capabilities.playerspell;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ClientSpellData {

    private static String CURRENT_SPELL;
    public static void setCurrentSpell(String spell) {
        ClientSpellData.CURRENT_SPELL = spell;
    }
    public static String getCurrentSpell() {
        return CURRENT_SPELL;
    }

    private static String KNOWN_SPELLS;
    public static void setKnownSpells(String knownSpells) {
        ClientSpellData.KNOWN_SPELLS = knownSpells;
    }
    public static String getKnownSpells() {
        return KNOWN_SPELLS;
    }


    public static boolean isSpellKnown(SpellTabletItem spellToCheck) {
        boolean state = false;

        if ( getKnownSpells() != null ) {
            List<SpellTabletItem> spellList = stringListToSpellList(ClientSpellData.getKnownSpells());
            if ( spellList.contains(spellToCheck) ) state = true;
        }

        return state;
    }

    public static List<SpellTabletItem> stringListToSpellList(String list) {
        List<SpellTabletItem> spellList = Lists.newArrayList();
        for ( String string : List.of(list.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof SpellTabletItem spellTabletItem ) spellList.add(spellTabletItem);
        }
        return spellList;
    }
}
