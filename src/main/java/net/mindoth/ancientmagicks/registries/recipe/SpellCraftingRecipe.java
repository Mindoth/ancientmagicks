package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
        List<ItemStack> componentStackList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                if ( stack.getItem() instanceof ParchmentItem && !stack.hasTag() ) paperList.add(stack);
                else if ( stack.getItem() instanceof ComponentItem ) componentStackList.add(stack);
                else restList.add(stack);
            }
        }
        if ( paperList.size() == 1 && restList.isEmpty() ) {
            List<ComponentItem> componentList = Lists.newArrayList();
            for ( ItemStack stack : componentStackList ) if ( stack.getItem() instanceof ComponentItem component ) componentList.add(component);
            return CastingValidator.isValidSpell(componentList);
        }
        else return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ItemStack> paperList = Lists.newArrayList();
        List<ItemStack> componentStackList = Lists.newArrayList();
        List<ItemStack> restList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() != Items.AIR ) {
                if ( stack.getItem() instanceof ParchmentItem && !stack.hasTag() ) paperList.add(stack);
                else if ( stack.getItem() instanceof ComponentItem ) componentStackList.add(stack);
                else restList.add(stack);
            }
        }
        if ( paperList.size() == 1 && restList.isEmpty() ) {
            List<ComponentItem> componentList = Lists.newArrayList();
            StringBuilder effectData = new StringBuilder();
            for ( int i = 0; i < componentStackList.size(); i++ ) {
                ItemStack stack = componentStackList.get(i);
                if ( stack.getItem() instanceof ComponentItem component ) {
                    componentList.add(component);
                    if ( i > 0 ) effectData.append(",");
                    effectData.append(component.encodeComponentData(stack));
                }
            }
            if ( CastingValidator.isValidSpell(componentList) ) {
                ItemStack stack = paperList.get(0).copy();
                stack.setCount(1);
                if ( stack.hasCustomHoverName() ) stack.setHoverName(Component.literal(paperList.get(0).getHoverName().getString()));
                CompoundTag tag = stack.getOrCreateTag();

                tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, CastingValidator.getStringFromSpellStack(componentList));
                tag.putString(ParchmentItem.NBT_KEY_DATA_STRING, effectData.toString());

                StringBuilder spellCode = new StringBuilder();
                for ( int i = 0; i < AncientMagicks.comboSizeCalc(); i++ ) {
                    if ( i > 0 ) spellCode.append(",");
                    spellCode.append(ForgeRegistries.ITEMS.getKey(AncientMagicksItems.BLANK_RUNE.get()).toString());
                }
                tag.putString(ParchmentItem.NBT_KEY_CODE_STRING, spellCode.toString());
                return stack;
            }
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