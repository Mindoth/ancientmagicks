package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mindoth.ancientmagicks.capability.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.event.CastingValidator;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
            if ( !player.getCooldowns().isOnCooldown(staff.getItem()) ) {
                if ( !(player.isCrouching() && SpellBookItem.getHeldSpellBook(player) != ItemStack.EMPTY) ) player.startUsingItem(hand);
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity caster, ItemStack staff, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( !(caster instanceof ServerPlayer player) ) return;
        if ( player.getCooldowns().isOnCooldown(staff.getItem()) ) return;
        //int useTime = getUseDuration(staff) - timeLeft;
        ItemStack book = SpellBookItem.getSpellBookSlot(player);
        if ( book.isEmpty() || !book.hasTag() || !book.getTag().contains(SpellBookItem.NBT_KEY_BOOK_SLOT) || SpellBookItem.getActiveScrollFromBook(book) == null ) {
            whiffSpell(caster);
            return;
        }
        ItemStack scroll = SpellBookItem.getActiveScrollFromBook(book);
        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            List<RuneItem> runeList = CastingValidator.getSpellStackFromScroll(scroll);
            int manaCost = 0;
            //for ( SpellComponentItem item : runeList ) manaCost += item.getCost();
            if ( magic.getCurrentMana() >= manaCost || player.isCreative() ) doSpell(player, staff, scroll);
            else whiffSpell(caster);
        });
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
