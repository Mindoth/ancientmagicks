package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CastingValidator {

    public static boolean calculateSpellRecipes(ItemStack scroll, LivingEntity owner, Entity caster, int chainLength) {
        if ( !(scroll.getItem() instanceof ParchmentItem) || !scroll.hasTag() ) return false;
        if ( !scroll.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) return false;
        List<ComponentItem> componentList = getComponentListFromScroll(scroll);
        List<List<ComponentItem>> spellStack = getSpellStackFromComponentList(componentList, chainLength);

        if ( !spellStack.isEmpty() ) return castSpell(owner, caster, spellStack);
        else return false;
    }

    public static boolean castSpell(LivingEntity owner, Entity caster, List<List<ComponentItem>> spellStack) {
        if ( spellStack.isEmpty() ) return false;
        SpellFormItem form = null;
        for ( ComponentItem item : spellStack.get(0) ) {
            if ( item instanceof SpellFormItem formItem ) {
                form = formItem;
                break;
            }
        }
        if ( form != null ) return form.formSpell(owner, caster, spellStack);
        else return false;
    }

    //TODO: Maybe implement some kind of modifier to do this instead of parchment quality?
    public static int getChainLength(Item parchment) {
        if ( parchment == AncientMagicksItems.INFERNAL_PARCHMENT.get() ) return 1;
        if ( parchment == AncientMagicksItems.ARCANE_PARCHMENT.get() ) return 2;
        else return 0;
    }

    public static String getStringFromComponentList(List<ComponentItem> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0; i < list.size(); i++ ) {
            if ( i > 0 ) stringBuilder.append(",");
            stringBuilder.append(ForgeRegistries.ITEMS.getKey(list.get(i)).toString());
        }
        return stringBuilder.toString();
    }

    public static String getStringFromSpellStack(List<List<ComponentItem>> spellStack) {
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0; i < spellStack.size(); i++ ) {
            if ( i > 0 ) stringBuilder.append(";");
            for ( int j = 0; j < spellStack.get(i).size(); j++ ) {
                if ( j > 0 ) stringBuilder.append(",");
                stringBuilder.append(ForgeRegistries.ITEMS.getKey(spellStack.get(i).get(j)).toString());
            }
        }
        return stringBuilder.toString();
    }

    public static List<List<ComponentItem>> getSpellStackFromString(String string) {
        List<List<ComponentItem>> spellStack = Lists.newArrayList();
        List<ComponentItem> componentList = Lists.newArrayList();
        for ( String list : List.of(string.split(";")) ) {
            for ( String component : List.of(list.split(",")) ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(component));
                if ( item instanceof ComponentItem componentItem ) componentList.add(componentItem);
            }
            spellStack.add(componentList);
            componentList = Lists.newArrayList();
        }
        return spellStack;
    }

    public static List<List<ComponentItem>> getSpellStackFromComponentList(List<ComponentItem> componentList, int chainLength) {
        List<ComponentItem> spellRecipe = Lists.newArrayList();
        List<List<ComponentItem>> spellStack = Lists.newArrayList();
        for ( ComponentItem item : componentList ) {
            if ( item instanceof SpellItem ) {
                spellRecipe.add(item);
                if ( isValidSpell(spellRecipe) ) {
                    spellStack.add(spellRecipe);
                    spellRecipe = Lists.newArrayList();
                }
                else break;
            }
            else spellRecipe.add(item);
            if ( spellStack.size() > chainLength ) break;
        }
        return spellStack;
    }

    public static List<ComponentItem> getComponentListFromSpellStack(List<List<ComponentItem>> spellStack) {
        List<ComponentItem> componentList = Lists.newArrayList();
        for ( List<ComponentItem> list : spellStack ) componentList.addAll(list);
        return componentList;
    }

    public static boolean isValidSpell(List<ComponentItem> list) {
        List<SpellFormItem> forms = Lists.newArrayList();
        List<SpellItem> spells = Lists.newArrayList();
        for ( ComponentItem item : list ) {
            if ( item instanceof SpellFormItem formItem ) forms.add(formItem);
            else if ( item instanceof SpellItem spellItem ) spells.add(spellItem);
        }
        return forms.size() == 1 && spells.size() == 1 && list.indexOf(forms.get(0)) == 0 && list.indexOf(spells.get(0)) == list.size() - 1;
    }

    public static boolean isValidSpellStack(List<List<ComponentItem>> spellStack) {
        boolean validSpellStack = !spellStack.isEmpty();
        for ( List<ComponentItem> list : spellStack ) {
            if ( !CastingValidator.isValidSpell(list) ) {
                validSpellStack = false;
                break;
            }
        }
        return validSpellStack;
    }

    public static List<ComponentItem> getComponentListFromScroll(ItemStack scroll) {
        String stringList = scroll.getTag().getString(ParchmentItem.NBT_KEY_SPELL_STRING);
        List<ComponentItem> componentList = Lists.newArrayList();
        for ( String string : List.of(stringList.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof ComponentItem component ) componentList.add(component);
        }
        return componentList;
    }
}
