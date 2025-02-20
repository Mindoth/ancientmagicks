package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CastingValidator {

    public static boolean castSpell(ItemStack scroll, LivingEntity owner, Entity caster) {
        if ( !(scroll.getItem() instanceof ParchmentItem) || !scroll.hasTag() ) return false;
        if ( !scroll.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING) ) return false;
        List<ComponentItem> componentList = getComponentListFromScroll(scroll);
        SpellFormItem form = null;
        SpellItem spell = null;
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        for ( ComponentItem item : componentList ) {
            if ( item instanceof SpellFormItem formItem ) form = formItem;
            if ( item instanceof SpellItem spellItem ) spell = spellItem;
            if ( item instanceof SpellModifierItem modifierItem ) modifiers.add(modifierItem);
        }

        //Successful cast
        if ( form != null && spell != null ) return form.formSpell(spell, owner, caster, modifiers);

        return false;
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
