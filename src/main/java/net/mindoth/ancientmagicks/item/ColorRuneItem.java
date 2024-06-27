package net.mindoth.ancientmagicks.item;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ColorRuneItem extends RuneItem {

    public ColorRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    //Serverside string to send in packets. It being a CompoundTag is just a workaround...
    public static CompoundTag CURRENT_COMBO_TAG = new CompoundTag();
    //Server- AND Client-sided Map used in GUI checks
    public static HashMap<SpellRuneItem, List<ColorRuneItem>> CURRENT_COMBO_MAP = new HashMap<>();

    public static SpellRuneItem checkForSpellCombo(List<ColorRuneItem> comboToCheck) {
        SpellRuneItem spell = null;
        for ( Map.Entry<SpellRuneItem, List<ColorRuneItem>> entry : CURRENT_COMBO_MAP.entrySet() ) {
            SpellRuneItem key = entry.getKey();
            List<ColorRuneItem> value = entry.getValue();

            //TODO FIND A BETTER WAY TO DO THIS SINCE THIS METHOD IS SLOW
            if ( comboToCheck.containsAll(value) && value.containsAll(comboToCheck) && comboToCheck.size() == value.size() ) spell = key;
        }
        return spell;
    }

    //This is some REALLY delicate String parsing. I'm no expert...
    public static HashMap<SpellRuneItem, List<ColorRuneItem>> buildComboMap(String comboString) {
        HashMap<SpellRuneItem, List<ColorRuneItem>> returnMap = new HashMap<>();

        Map<String, String> tempMap = Splitter.on(";").withKeyValueSeparator("=").split(comboString);
        for ( Map.Entry<String, String> entry : tempMap.entrySet() ) {
            SpellRuneItem key = (SpellRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getKey()));
            List<ColorRuneItem> tempList = Lists.newArrayList();
            for ( String string : List.of(entry.getValue().replaceAll("[\\[\\]]", "").split(",")) ) {
                tempList.add((ColorRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(AncientMagicks.MOD_ID, string.replaceAll(" ", ""))));
            }
            returnMap.put(key, tempList);
        }

        return returnMap;
    }
}
