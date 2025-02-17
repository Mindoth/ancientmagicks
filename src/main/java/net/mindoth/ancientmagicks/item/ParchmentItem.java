package net.mindoth.ancientmagicks.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ParchmentItem extends Item {

    public static final String NBT_KEY_PAPER_SPELL = "am_paper_spell";

    public ParchmentItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {

        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_PAPER_SPELL) ) {
            CompoundTag tag = stack.getTag();
            for ( String string : List.of(tag.getString(NBT_KEY_PAPER_SPELL).split(",")) ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
                tooltip.add(Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.GRAY));
            }
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
