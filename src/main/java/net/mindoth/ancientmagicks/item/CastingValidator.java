package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.revamp.item.rune.RuneItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CastingValidator {

    /*public static boolean calculateSpellRecipes(ItemStack scroll, LivingEntity owner, Entity caster) {
        if ( !(scroll.getItem() instanceof ParchmentItem) || !scroll.hasTag() ) return false;
        if ( !scroll.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) return false;
        List<SpellComponentItem> spellStack = getSpellStackFromScroll(scroll);
        List<String> data = getDataListFromScroll(scroll);
        if ( !spellStack.isEmpty() && isValidSpell(spellStack) ) return castSpell(owner, caster, spellStack, data);
        else return false;
    }

    public static boolean castSpell(LivingEntity owner, Entity caster, List<SpellComponentItem> spellStack, List<String> data) {
        if ( spellStack.isEmpty() ) return false;
        SpellFormItem formItem = null;
        for ( SpellComponentItem item : spellStack ) {
            if ( item instanceof SpellFormItem form ) {
                formItem = form;
                break;
            }
        }
        if ( formItem != null ) return formItem.formSpell(owner, caster, spellStack, data);
        else return false;
    }*/

    public static boolean isValidSpell(List<RuneItem> spellStack) {
        /*SpellFormItem form = null;
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        for ( SpellComponentItem item : spellStack ) {
            if ( item instanceof SpellEffectItem && form == null ) return false;
            if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
            if ( item instanceof SpellFormItem formItem ) {
                if ( form == null ) form = formItem;
                else return false;
            }
            if ( item instanceof SpellFormItem || item instanceof SpellEffectItem ) {
                for ( SpellModifierItem modifier : modifiers ) if ( !SpellModifierItem.canAddModifier(modifier, item) ) return false;
                modifiers = Lists.newArrayList();
            }
        }
        return form != null && spellStack.get(spellStack.size() - 1) instanceof SpellEffectItem;*/
        return true;
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
