package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AddSpellToBookRecipe extends CustomRecipe {

    public AddSpellToBookRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> itemList = Lists.newArrayList();
        boolean flag = false;
        boolean flag2 = false;
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack contItem = container.getItem(i);
            if ( contItem.getItem() != Items.AIR ) {
                if ( contItem.getItem() instanceof SpellBookItem ) {
                    itemList.add(contItem);
                    flag = true;
                }
                else if ( contItem.getItem() == AncientMagicksItems.SPELL_SCROLL.get() && SpecialCastingItem.getStoredSpell(contItem) != null ) {
                    itemList.add(contItem);
                    flag2 = true;
                }
            }
        }
        return itemList.size() == 2 && flag && flag2;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ItemStack> itemList = Lists.newArrayList();
        ItemStack bookStack = ItemStack.EMPTY;
        SpellItem spell = null;
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack contItem = container.getItem(i);
            if ( contItem.getItem() != Items.AIR ) {
                if ( contItem.getItem() instanceof SpellBookItem ) {
                    itemList.add(contItem);
                    bookStack = container.getItem(i);
                }
                else if ( contItem.getItem() == AncientMagicksItems.SPELL_SCROLL.get() && SpecialCastingItem.getStoredSpell(contItem) != null ) {
                    itemList.add(contItem);
                    spell = SpecialCastingItem.getStoredSpell(container.getItem(i));
                }
            }
        }
        if ( itemList.size() == 2 && !bookStack.isEmpty() && spell != null ) {
            ItemStack newStack = bookStack.copy();
            CompoundTag newTag = newStack.getOrCreateTag();
            String spellString = ForgeRegistries.ITEMS.getKey(spell).toString();
            if ( newTag.contains(SpellBookItem.NBT_KEY_BOOK_SPELLS) ) {
                List<SpellItem> spellList = ClientMagicData.stringListToSpellList(newTag.getString(SpellBookItem.NBT_KEY_BOOK_SPELLS));
                if ( spellList.contains(spell) ) return ItemStack.EMPTY;
                else {
                    String spells = newTag.getString(SpellBookItem.NBT_KEY_BOOK_SPELLS);
                    newTag.remove(SpellBookItem.NBT_KEY_BOOK_SPELLS);
                    newTag.putString(SpellBookItem.NBT_KEY_BOOK_SPELLS, spells + "," + spellString);
                }
            }
            else newTag.putString(SpellBookItem.NBT_KEY_BOOK_SPELLS, spellString);
            return newStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.ADD_SPELL_TO_BOOK_RECIPE.get();
    }
}
