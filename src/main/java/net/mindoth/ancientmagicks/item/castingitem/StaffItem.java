package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.SpellValidator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class StaffItem extends CastingItem implements Vanishable {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final Item repairItem;

    public StaffItem(Properties pProperties, Item repairItem, Map<Attribute, AttributeModifier> additionalAttributes) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for ( Map.Entry<Attribute, AttributeModifier> modifierEntry : additionalAttributes.entrySet() ) builder.put(modifierEntry.getKey(), modifierEntry.getValue());
        this.defaultModifiers = builder.build();
        this.repairItem = repairItem;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(hand);
            if ( isValidCastingItem(staff) && !(player.isCrouching() && SpellBookItem.getHeldSpellBook(player) != ItemStack.EMPTY) ) {
                ItemStack book = SpellBookItem.getSpellBookSlot(player);
                if ( !book.isEmpty() ) {
                    CompoundTag tag = book.getTag();
                    List<ItemStack> spellList = SpellBookItem.getScrollListFromBook(tag);
                    //SpellValidator.castSpell(spellList.get(0), player, player);
                }
            }
        }
        return result;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(this.repairItem);
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 25;
    }
}
