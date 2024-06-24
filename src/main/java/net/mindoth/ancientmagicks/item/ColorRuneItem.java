package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorRuneItem extends RuneItem {

    public ColorRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public static List<ColorRuneItem> COLOR_RUNES = Lists.newArrayList();
    public static HashMap<SpellRuneItem, List<ColorRuneItem>> comboMap = new HashMap<>();

    public static void init() {
        for ( Item item : AncientMagicks.ITEM_LIST ) if ( item instanceof ColorRuneItem ) COLOR_RUNES.add((ColorRuneItem)item);

        List<List<ColorRuneItem>> comboList = Lists.newArrayList();
        for ( int i = 0; i < COLOR_RUNES.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNES.size(); j++ ) {
                List<ColorRuneItem> tempList = Lists.newArrayList();
                tempList.add(COLOR_RUNES.get(i));
                tempList.add(COLOR_RUNES.get(j));
                comboList.add(tempList);
            }
        }
        if ( comboList.size() < SpellRuneItem.SPELL_RUNES.size() ) {
            System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY SPELL. CONSIDER TAKING A LOOK AT YOUR MODLIST");
        }
        else {
            Collections.shuffle(comboList);
            for ( int i = 0; i < comboList.size(); i++ ) {
                if ( i < SpellRuneItem.SPELL_RUNES.size() ) {
                    comboMap.put(SpellRuneItem.SPELL_RUNES.get(i), comboList.get(i));
                }
            }

            for ( int i = 0; i < SpellRuneItem.SPELL_RUNES.size(); i++ ) {
                System.out.println(SpellRuneItem.SPELL_RUNES.get(i) + " -> " + comboMap.get(SpellRuneItem.SPELL_RUNES.get(i)));
            }
        }
    }

    public static SpellRuneItem checkForSpellCombo(List<ColorRuneItem> comboToCheck) {
        SpellRuneItem spell = null;
        for ( Map.Entry<SpellRuneItem, List<ColorRuneItem>> entry : comboMap.entrySet() ) {
            SpellRuneItem key = entry.getKey();
            List<ColorRuneItem> value = entry.getValue();
            if ( comboToCheck.equals(value) ) {
                spell = key;
            }
        }
        return spell;
    }
}
