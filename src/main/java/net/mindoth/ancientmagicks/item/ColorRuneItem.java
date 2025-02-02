package net.mindoth.ancientmagicks.item;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ColorRuneItem extends Item {

    public String color;
    public static final String AM_COMBOSTRING = "am_combostring";

    public ColorRuneItem(Properties pProperties, String color) {
        super(pProperties);
        this.color = color;
    }

    //Serverside string to send in packets. It being a CompoundTag is just a workaround...
    public static CompoundTag CURRENT_COMBO_TAG = new CompoundTag();
    //Server- AND Client-sided Map used in GUI checks
    public static HashMap<SpellItem, List<ColorRuneItem>> CURRENT_COMBO_MAP = new HashMap<>();

    @Nullable
    public static SpellItem checkForSpellCombo(List<ColorRuneItem> comboToCheck/*, @Nullable SpellItem secretSpell*/) {
        for ( Map.Entry<SpellItem, List<ColorRuneItem>> entry : CURRENT_COMBO_MAP.entrySet() ) {
            SpellItem key = entry.getKey();
            List<ColorRuneItem> value = entry.getValue();
            if ( AncientMagicks.listsMatch(comboToCheck, value) ) return key;
        }
        return null;
    }

    //This is some REALLY delicate String parsing. I'm no expert...
    public static HashMap<SpellItem, List<ColorRuneItem>> buildComboMap(String comboString) {
        HashMap<SpellItem, List<ColorRuneItem>> returnMap = new HashMap<>();

        Map<String, String> tempMap = Splitter.on(";").withKeyValueSeparator("=").split(comboString);
        for ( Map.Entry<String, String> entry : tempMap.entrySet() ) {
            SpellItem key = (SpellItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getKey()));
            if ( AncientMagicks.isSpellEnabled(key) ) {
                List<ColorRuneItem> tempList = Lists.newArrayList();
                for ( String string : List.of(entry.getValue().replaceAll("[\\[\\]]", "").split(",")) ) {
                    tempList.add((ColorRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(AncientMagicks.MOD_ID, string.replaceAll(" ", ""))));
                }
                returnMap.put(key, tempList);
            }
        }

        return returnMap;
    }

    public static List<ColorRuneItem> stringListToActualList(String comboString) {
        List<ColorRuneItem> tempList = Lists.newArrayList();
        for ( String string : List.of(comboString.replaceAll("[\\[\\]]", "").split(",")) ) {
            tempList.add((ColorRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(AncientMagicks.MOD_ID, string.replaceAll(" ", ""))));
        }
        return tempList;
    }

    public static List<ItemStack> getColorRuneList(Player player) {
        List<ItemStack> runeList = Lists.newArrayList();
        for ( ColorRuneItem rune : AncientMagicks.COLOR_RUNE_LIST ) runeList.add(new ItemStack(rune));
        return runeList;
    }
}
