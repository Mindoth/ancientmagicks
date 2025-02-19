package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
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

public class SpellCraftingRecipe extends CustomRecipe {

    public SpellCraftingRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> formList = Lists.newArrayList();
        List<ItemStack> spellList = Lists.newArrayList();
        List<ItemStack> modifierList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                boolean emptyPaper = !stack.hasTag();
                if ( stack.getItem() instanceof ParchmentItem && emptyPaper ) paperList.add(stack);
                else if ( stack.getItem() instanceof SpellFormItem ) formList.add(stack);
                else if ( stack.getItem() instanceof SpellItem ) spellList.add(stack);
                else if ( stack.getItem() instanceof SpellModifierItem ) modifierList.add(stack);
                else restList.add(stack);
            }
        }
        return paperList.size() == 1 && spellList.size() == 1 && formList.size() == 1 && restList.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> formList = Lists.newArrayList();
        List<ItemStack> spellList = Lists.newArrayList();
        List<ItemStack> modifierList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                boolean emptyPaper = !stack.hasTag();
                if ( stack.getItem() instanceof ParchmentItem && emptyPaper ) paperList.add(stack);
                else if ( stack.getItem() instanceof SpellFormItem ) formList.add(stack);
                else if ( stack.getItem() instanceof SpellItem ) spellList.add(stack);
                else if ( stack.getItem() instanceof SpellModifierItem ) modifierList.add(stack);
                else restList.add(stack);
            }
        }
        if ( paperList.size() == 1 && spellList.size() == 1 && formList.size() == 1 && restList.isEmpty() ) {
            ItemStack stack = paperList.get(0).copy();
            stack.setCount(1);
            stack.setHoverName(paperList.get(0).getHoverName());
            CompoundTag tag = stack.getOrCreateTag();
            List<ItemStack> runeList = Lists.newArrayList();
            runeList.addAll(formList);
            runeList.addAll(spellList);
            runeList.addAll(modifierList);

            StringBuilder spellString = new StringBuilder();
            for ( int i = 0; i < runeList.size(); i++ ) {
                if ( i > 0 ) spellString.append(",");
                spellString.append(ForgeRegistries.ITEMS.getKey(runeList.get(i).getItem()).toString());
            }
            tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, spellString.toString());

            StringBuilder spellCode = new StringBuilder();
            for ( int i = 0; i < AncientMagicks.comboSizeCalc(); i++ ) {
                if ( i > 0 ) spellCode.append(",");
                spellCode.append(ForgeRegistries.ITEMS.getKey(AncientMagicksItems.BLANK_RUNE.get()).toString());
            }
            tag.putString(ParchmentItem.NBT_KEY_CODE_STRING, spellCode.toString());
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.SPELL_CRAFTING_RECIPE.get();
    }
}
