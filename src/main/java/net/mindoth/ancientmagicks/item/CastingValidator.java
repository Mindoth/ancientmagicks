package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CastingValidator {

    public static boolean castSpell(LivingEntity owner, Entity caster, List<List<ComponentItem>> recipeList) {
        SpellFormItem form = null;
        for ( ComponentItem item : recipeList.get(0) ) {
            if ( item instanceof SpellFormItem formItem ) {
                form = formItem;
                break;
            }
        }
        if ( form != null ) return form.formSpell(owner, caster, recipeList);
        else return false;
    }

    public static boolean calculateSpellRecipes(ItemStack scroll, LivingEntity owner, Entity caster, int chainLength) {
        if ( !(scroll.getItem() instanceof ParchmentItem) || !scroll.hasTag() ) return false;
        if ( !scroll.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) return false;
        List<ComponentItem> componentList = getComponentListFromScroll(scroll);
        List<List<ComponentItem>> recipeList = getSpellStackFromComponentList(componentList, chainLength);

        if ( !recipeList.isEmpty() ) return castSpell(owner, caster, recipeList);
        else return false;
    }

    public static List<List<ComponentItem>> getSpellStackFromComponentList(List<ComponentItem> componentList, int chainLength) {
        List<ComponentItem> spellRecipe = Lists.newArrayList();
        List<List<ComponentItem>> spellStack = Lists.newArrayList();
        for ( ComponentItem item : componentList ) {
            if ( item instanceof SpellItem ) {
                spellRecipe.add(item);
                if ( isSpellValid(spellRecipe) ) {
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

    public static boolean isSpellValid(List<ComponentItem> list) {
        List<SpellFormItem> forms = Lists.newArrayList();
        List<SpellItem> spells = Lists.newArrayList();
        for ( ComponentItem item : list ) {
            if ( item instanceof SpellFormItem formItem ) forms.add(formItem);
            else if ( item instanceof SpellItem spellItem ) spells.add(spellItem);
        }
        return forms.size() == 1 && spells.size() == 1 && list.indexOf(forms.get(0)) < list.indexOf(spells.get(0));
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
