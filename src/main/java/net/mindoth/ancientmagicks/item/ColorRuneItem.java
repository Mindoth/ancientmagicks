package net.mindoth.ancientmagicks.item;

import com.google.common.base.Splitter;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorRuneItem extends RuneItem {

    public ColorRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public static SpellRuneItem checkForSpellCombo(List<ColorRuneItem> comboToCheck) {
        String stringCombo = comboToCheck.toString();
        SpellRuneItem spell = null;
        for ( Map.Entry<String, String> entry : CURRENT_COMBO_MAP.entrySet() ) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ( stringCombo.equals(value) ) {
                //TODO MAKE IT WORK WITH ADDON MODS (MODID NEEDS TO BE INCLUDED IN KEY)
                spell = (SpellRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation("ancientmagicks:" + key));
            }
        }
        return spell;
    }

    //Serverside string to send in packets. It being a CompoundTag is just a workaround...
    public static CompoundTag CURRENT_COMBO_TAG = new CompoundTag();

    //Clientside map to be used in GUI checks
    public static Map<String, String> CURRENT_COMBO_MAP = new HashMap<>();

    public static void buildClientComboMap(String comboString) {
        CURRENT_COMBO_MAP = Splitter.on(";").withKeyValueSeparator("=").split(comboString);
    }
}
