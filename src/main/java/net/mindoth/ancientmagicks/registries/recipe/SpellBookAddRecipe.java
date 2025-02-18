package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
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

public class SpellBookAddRecipe extends CustomRecipe {

    public SpellBookAddRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> bookList = Lists.newArrayList();
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                boolean paperHasSpell = stack.hasTag() && stack.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING);
                if ( stack.getItem() instanceof SpellBookItem ) bookList.add(stack);
                else if ( stack.getItem() instanceof ParchmentItem && paperHasSpell ) paperList.add(stack);
                else restList.add(stack);
            }
        }
        return bookList.size() == 1 && paperList.size() == 1 && restList.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ItemStack> bookList = Lists.newArrayList();
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                boolean paperHasSpell = stack.hasTag() && stack.getTag().contains(ParchmentItem.NBT_KEY_SPELL_STRING);
                if ( stack.getItem() instanceof SpellBookItem ) bookList.add(stack);
                else if ( stack.getItem() instanceof ParchmentItem && paperHasSpell ) paperList.add(stack);
                else restList.add(stack);
            }
        }
        if ( bookList.size() == 1 && paperList.size() == 1 && restList.isEmpty() ) {
            ItemStack book = bookList.get(0).copy();
            ItemStack scroll = paperList.get(0);
            CompoundTag bookTag = book.getOrCreateTag();

            String spellString = scroll.getTag().getString(ParchmentItem.NBT_KEY_SPELL_STRING);
            SpellBookItem.addSpellTagsToBook(bookTag, spellString, SpellBookItem.NBT_KEY_SPELLS);

            String name = scroll.getHoverName().getString();
            SpellBookItem.addSpellTagsToBook(bookTag, name, ParchmentItem.NBT_KEY_SPELL_NAME);

            String item = ForgeRegistries.ITEMS.getKey(scroll.getItem()).toString();
            SpellBookItem.addSpellTagsToBook(bookTag, item, ParchmentItem.NBT_KEY_PAPER_TIER);
            return book;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    /*@Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(AncientMagicksItems.SPELL_BOOK.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(Ingredient.of(AncientMagicksItems.SPELL_BOOK.get()));
        return list;
    }*/

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.SPELL_BOOK_ADD_RECIPE.get();
    }
}
