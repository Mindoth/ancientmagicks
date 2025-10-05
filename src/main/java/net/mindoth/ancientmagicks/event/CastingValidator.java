package net.mindoth.ancientmagicks.event;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CastingValidator {

    public static boolean castMagick(Entity caster, ItemStack scroll) {
        if ( !(scroll.getItem() instanceof ParchmentItem) || !scroll.hasTag() ) return false;
        if ( !scroll.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) return false;
        List<RuneItem> runeList = getSpellStackFromScroll(scroll);
        List<String> data = getDataListFromScroll(scroll);
        if ( !runeList.isEmpty() && isValidSpell(runeList) ) {
            SpellData spellData = new SpellData();
            for ( int i = 0; i < runeList.size(); i++ ) {
                RuneItem rune = runeList.get(i);
                spellData.addRune(rune);
                spellData.addData(data.get(i));
            }
            return resolveSpell(caster, spellData, runeList);
        }
        else return false;
    }

    public static boolean resolveSpell(Entity caster, SpellData spellData, List<RuneItem> runeList) {
        for ( RuneItem rune : runeList ) {
            if ( !spellData.getRuneList().isEmpty() ) spellData.getRuneList().remove(0);
            if ( !spellData.getDataList().isEmpty() ) spellData.getDataList().remove(0);
            spellData = rune.resolve(caster, spellData);
            if ( rune == ModItems.PROJECTILE_RUNE_ITEM.get() ) break;
            if ( !spellData.isValid() ) {
                if ( caster instanceof Player player ) player.displayClientMessage(Component.literal("FAILED SPELL ON: " + rune), false);
                break;
            }
        }
        return spellData.isValid();
    }

    public static boolean isValidSpell(List<RuneItem> spellStack) {
        return !spellStack.isEmpty();
    }

    public static String getStringFromSpellStack(List<RuneItem> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0; i < list.size(); i++ ) {
            if ( i > 0 ) stringBuilder.append(",");
            stringBuilder.append(ForgeRegistries.ITEMS.getKey(list.get(i)).toString());
        }
        return stringBuilder.toString();
    }

    public static List<RuneItem> getSpellStackFromString(String string) {
        List<RuneItem> spellStack = Lists.newArrayList();
        for ( String rune : List.of(string.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(rune));
            if ( item instanceof RuneItem runeItem ) spellStack.add(runeItem);
        }
        return spellStack;
    }

    public static List<RuneItem> getSpellStackFromScroll(ItemStack scroll) {
        String stringList = scroll.getTag().getString(ParchmentItem.NBT_KEY_SPELL_STRING);
        List<RuneItem> runeList = Lists.newArrayList();
        for ( String string : List.of(stringList.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof RuneItem runeItem ) runeList.add(runeItem);
        }
        return runeList;
    }

    public static List<String> getDataListFromScroll(ItemStack scroll) {
        String stringList = scroll.getTag().getString(ParchmentItem.NBT_KEY_DATA_STRING);
        return List.of(stringList.split(","));
    }

    public static String getStringFromDataList(List<String> stringList) {
        StringBuilder effectData = new StringBuilder();
        for ( int i = 0; i < stringList.size(); i++ ) {
            if ( i > 0 ) effectData.append(",");
            effectData.append(stringList.get(i));
        }
        return effectData.toString();
    }
}
