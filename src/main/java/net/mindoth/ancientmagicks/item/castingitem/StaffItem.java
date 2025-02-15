package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagic;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Map;

public class StaffItem extends CastingItem implements Vanishable {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final Item repairItem;

    public StaffItem(Properties pProperties,/* double attackDamage, double attackSpeed,*/ Item repairItem, Map<Attribute, AttributeModifier> additionalAttributes) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        //builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        //builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        for ( Map.Entry<Attribute, AttributeModifier> modifierEntry : additionalAttributes.entrySet() ) builder.put(modifierEntry.getKey(), modifierEntry.getValue());
        this.defaultModifiers = builder.build();
        this.repairItem = repairItem;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
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

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(hand);
            if ( isValidCastingItem(staff) && !(player.isCrouching() && SpellBookItem.getHeldSpellBook(player) != ItemStack.EMPTY) ) {
                player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                    if ( item instanceof SpellItem spell && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(spell) ) {
                        if ( canCast(player, magic) ) player.startUsingItem(hand);
                        else CastingItem.whiffSpell(player, spell);
                    }
                });
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack staff, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            if ( isValidCastingItem(staff) ) {
                player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                    if ( canCast(player, magic) ) {
                        Item spell = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                        doSpell(player, player, staff, (SpellItem)spell, getUseDuration(staff) - timeLeft);
                    }
                    else living.stopUsingItem();
                });
            }
        }
    }

    public boolean canCast(Player player, PlayerMagic magic) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
        return item instanceof SpellItem spell && (magic.getCurrentMana() >= spell.getManaCost() || player.isCreative());
    }
}
