package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.client.gui.inventory.WandManager;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CastingItem extends Item {
    public WandType tier;
    public int cooldown;

    public CastingItem(WandType tier) {
        super(new Item.Properties().stacksTo(1));
        this.tier = tier;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            if ( wand.hasTag() && wand.getTag().contains("am_spellrune") ) {
                Item spellRune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(wand.getTag().getString("am_spellrune")));
                if ( spellRune instanceof SpellRuneItem ) {
                    if ( timeLeft % 2 == 0 ) doSpell(player, player, wand, (SpellRuneItem)spellRune, getUseDuration(wand) - timeLeft);
                }
            }
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack wand = player.getItemInHand(handIn);
            if ( isValidCastingItem(wand) ) {
                if ( wand.hasTag() && wand.getTag().contains("spellRune") ) {
                    Item spellRune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(wand.getTag().getString("am_spellrune")));
                    if ( spellRune instanceof SpellRuneItem spellRuneItem && !player.getCooldowns().isOnCooldown(spellRuneItem) ) {
                        player.startUsingItem(handIn);
                    }
                }
            }
        }
        return result;
    }

    public static void doSpell(Player owner, Entity caster, ItemStack wand, SpellRuneItem spell, int useTime) {
        if ( caster == owner && owner.getCooldowns().isOnCooldown(spell) ) return;

        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center;
        float distance = 0;

        if ( distance > 0 ) {

            //Adjusters are there to flip rotation if the caster is not a LivingEntity, don't ask why this works like this...
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vec3 direction = ShadowEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition(1).add(direction);
        }
        else center = caster.getEyePosition(1.0F);

        //This actually casts the given Spell
        spell.castMagic(owner, caster, center, xRot, yRot, useTime);

        //These are cooldown and channeling related handling
        if ( caster == owner ) addCastingCooldown(owner, spell);
        owner.stopUsingItem();
        //if ( !spell.isChannel ) owner.stopUsingItem();
    }

    public static void addCastingCooldown(Player player, SpellRuneItem spell) {
        player.getCooldowns().addCooldown(spell, spell.tier * 20);
    }

    public static List<ItemStack> getStaffList(ItemStack staff) {
        List<ItemStack> staffList = Lists.newArrayList();
        if ( isValidCastingItem(staff) ) {
            WandData data = CastingItem.getData(staff);
            for ( int i = 0; i < data.getHandler().getSlots(); i++ ) {
                ItemStack rune = data.getHandler().getStackInSlot(i);
                if ( rune.getItem() instanceof ColorRuneItem ) staffList.add(rune);
            }
        }
        return staffList;
    }

    public static @Nonnull ItemStack getHeldCastingItem(Player playerEntity) {
        ItemStack wand = playerEntity.getMainHandItem().getItem() instanceof CastingItem ? playerEntity.getMainHandItem() : null;
        return wand == null ? (playerEntity.getOffhandItem().getItem() instanceof CastingItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : wand;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.hasTag() && castingItem.getTag().contains("UUID") && CastingItem.getData(castingItem).getUuid() != null;
    }

    public static WandData getData(ItemStack stack) {
        if ( !(stack.getItem() instanceof CastingItem) ) return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if ( !tag.contains("UUID") ) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }
        else uuid = tag.getUUID("UUID");
        return WandManager.get().getOrCreateWand(uuid, ((CastingItem)stack.getItem()).tier);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return tier.rarity;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new WandCaps(stack);
    }

    static class WandCaps implements ICapabilityProvider {
        private final ItemStack stack;

        public WandCaps(ItemStack stack) {
            this.stack = stack;
        }

        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if ( cap == ForgeCapabilities.ITEM_HANDLER ) {
                if ( !optional.isPresent() ) optional = WandManager.get().getCapability(stack);
                return optional.cast();
            }
            else return LazyOptional.empty();
        }
    }
}
