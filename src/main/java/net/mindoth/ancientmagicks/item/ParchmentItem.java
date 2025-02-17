package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketOpenSpellBook;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ParchmentItem extends Item {

    public static final String NBT_KEY_PAPER_SPELL = "am_paper_spell";

    public ParchmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = player.getItemInHand(handIn);
            if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_PAPER_SPELL) ) {
                SpellValidator.castSpell(stack, serverPlayer, serverPlayer);
            }
        }
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_PAPER_SPELL) ) {
            CompoundTag tag = stack.getTag();
            List<Item> runes = Lists.newArrayList();
            for ( String string : List.of(tag.getString(NBT_KEY_PAPER_SPELL).split(",")) ) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
                runes.add(item);
            }
            for ( Item item : runes ) {
                if ( item instanceof SpellFormItem) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.form")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
            for ( Item item : runes ) {
                if ( item instanceof SpellItem ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.spell")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
            for ( Item item : runes ) {
                if ( item instanceof SpellModifierItem ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.modifier")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
