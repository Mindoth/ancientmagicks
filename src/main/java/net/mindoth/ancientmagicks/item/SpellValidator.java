package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SpellValidator {

    public static boolean castSpell(ItemStack spellScroll, LivingEntity owner, Entity caster) {
        if ( !(spellScroll.getItem() instanceof ParchmentItem) || !spellScroll.hasTag() ) return false;
        CompoundTag tag = spellScroll.getTag();
        if ( !tag.contains(ParchmentItem.NBT_KEY_PAPER_SPELL) ) return false;
        String stringList = tag.getString(ParchmentItem.NBT_KEY_PAPER_SPELL);
        List<Item> runeList = Lists.newArrayList();
        for ( String string : List.of(stringList.split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            runeList.add(item);
        }
        SpellFormItem form = null;
        SpellItem spell = null;
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        for ( Item item : runeList ) {
            if ( item instanceof SpellFormItem formItem ) form = formItem;
            if ( item instanceof SpellItem spellItem ) spell = spellItem;
            if ( item instanceof SpellModifierItem modifierItem ) modifiers.add(modifierItem);
        }
        if ( form != null && spell != null ) return form.castSpell(spell, owner, caster, modifiers);

        return false;
    }
}
