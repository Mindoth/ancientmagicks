package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CastingValidator {

    public static boolean calculateSpellRecipes(ItemStack scroll, LivingEntity owner, Entity caster) {
        if ( !(scroll.getItem() instanceof ParchmentItem) || !scroll.hasTag() ) return false;
        if ( !scroll.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) return false;
        List<ComponentItem> spellStack = getSpellStackFromScroll(scroll);
        if ( !spellStack.isEmpty() && isValidSpell(spellStack) ) return castSpell(owner, caster, spellStack);
        else return false;
    }

    public static boolean castSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack) {
        if ( spellStack.isEmpty() ) return false;
        SpellFormItem form = null;
        for ( ComponentItem item : spellStack ) {
            if ( item instanceof SpellFormItem formItem ) {
                form = formItem;
                break;
            }
        }
        if ( form != null ) return form.formSpell(owner, caster, spellStack);
        else return false;
    }

    public static boolean isValidSpell(List<ComponentItem> spellStack) {
        SpellFormItem form = null;
        for ( ComponentItem item : spellStack ) {
            if ( item instanceof SpellEffectItem && form == null ) return false;
            if ( item instanceof SpellFormItem formItem ) {
                if ( form == null ) form = formItem;
                else return false;
            }
        }
        return form != null && spellStack.get(spellStack.size() - 1) instanceof SpellEffectItem;
    }

    public static String getStringFromSpellStack(List<ComponentItem> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0; i < list.size(); i++ ) {
            if ( i > 0 ) stringBuilder.append(",");
            stringBuilder.append(ForgeRegistries.ITEMS.getKey(list.get(i)).toString());
        }
        return stringBuilder.toString();
    }

    public static List<ComponentItem> getSpellStackFromString(String string) {
        List<ComponentItem> spellStack = Lists.newArrayList();
        for ( String component : List.of(string.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(component));
            if ( item instanceof ComponentItem componentItem ) spellStack.add(componentItem);
        }
        return spellStack;
    }

    public static List<ComponentItem> getSpellStackFromScroll(ItemStack scroll) {
        String stringList = scroll.getTag().getString(ParchmentItem.NBT_KEY_SPELL_STRING);
        List<ComponentItem> componentList = Lists.newArrayList();
        for ( String string : List.of(stringList.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof ComponentItem component ) componentList.add(component);
        }
        return componentList;
    }
}
