package net.mindoth.ancientmagicks.capabilities.playermagic;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ClientMagicData {

    private static String currentSpell;
    public static String getCurrentSpell() {
        return currentSpell;
    }
    public static void setCurrentSpell(String spell) {
        ClientMagicData.currentSpell = spell;
    }

    private static String knownSpells;
    public static String getKnownSpells() {
        return knownSpells;
    }
    public static void setKnownSpells(String knownSpells) {
        ClientMagicData.knownSpells = knownSpells;
    }

    private static double currentMana;
    public static double getCurrentMana() {
        return currentMana;
    }
    public static void setCurrentMana(double currentMana) {
        ClientMagicData.currentMana = currentMana;
    }


    public static boolean isSpellKnown(SpellItem spellToCheck) {
        boolean state = false;

        if ( getKnownSpells() != null ) {
            List<SpellItem> spellList = stringListToSpellList(ClientMagicData.getKnownSpells());
            if ( spellList.contains(spellToCheck) ) state = true;
        }

        return state;
    }

    public static List<SpellItem> stringListToSpellList(String list) {
        List<SpellItem> spellList = Lists.newArrayList();
        for ( String string : List.of(list.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof SpellItem spellItem ) spellList.add(spellItem);
        }
        return spellList;
    }
}
