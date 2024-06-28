package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.client.gui.inventory.WandManager;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.network.capabilities.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack wand = player.getItemInHand(handIn);
            if ( isValidCastingItem(wand) ) {
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                    if ( item instanceof TabletItem tabletItem && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(tabletItem) ) {
                        if ( player.totalExperience >= tabletItem.tier || player.isCreative() ) player.startUsingItem(handIn);
                        else {
                            addCastingCooldown(player, tabletItem, 20);
                            RuneItem.playWhiffSound(level, ShadowEvents.getEntityCenter(player));
                        }
                    }
                });
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            if ( isValidCastingItem(wand) ) {
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                    if ( item instanceof TabletItem tabletItem ) doSpell(player, player, wand, tabletItem, getUseDuration(wand) - timeLeft, player.isCreative());
                });
            }
        }
    }

    public static void doSpell(Player owner, Entity caster, ItemStack wand, TabletItem spell, int useTime, boolean isFree) {
        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center;
        float distance = 0.5F;

        if ( distance > 0 ) {

            //Adjusters are there to flip rotation if the caster is not a LivingEntity. Don't ask why this works like this...
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vec3 direction = ShadowEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition().add(direction);
        }
        else center = caster.getEyePosition();

        if ( caster == owner ) {
            //Check if player is creating tablets or not
            if ( (owner.getMainHandItem().getItem() == AncientMagicksItems.STONE_SLATE.get() || owner.getOffhandItem().getItem() == AncientMagicksItems.STONE_SLATE.get()) ) {
                ItemStack tabletStack;
                if ( owner.getMainHandItem().getItem() == AncientMagicksItems.STONE_SLATE.get() ) tabletStack = owner.getMainHandItem();
                else tabletStack = owner.getOffhandItem();
                int stackCount = 0;
                for ( int i = 0; i < tabletStack.getCount(); i++ ) {
                    if ( stackCount >= tabletStack.getCount() ) break;
                    boolean canAfford = false;
                    if ( isFree ) canAfford = true;
                    else if ( owner.totalExperience >= spell.tier ) {
                        canAfford = true;
                        owner.giveExperiencePoints(-spell.tier);
                    }
                    if ( canAfford ) stackCount += 1;
                }
                tabletStack.shrink(stackCount);
                ItemStack dropStack = new ItemStack(spell, stackCount);
                ItemEntity drop = new ItemEntity(owner.level(), ShadowEvents.getEntityCenter(owner).x, ShadowEvents.getEntityCenter(owner).y, ShadowEvents.getEntityCenter(owner).z, dropStack);
                drop.setDeltaMovement(0, 0, 0);
                drop.setNoPickUpDelay();
                caster.level().addFreshEntity(drop);
                addCastingCooldown(owner, spell, 20);
            }
            else {
                //This actually casts the given Spell
                if ( spell.castMagic(owner, caster, center, xRot, yRot, useTime) ) {
                    if ( wand.getItem() instanceof TabletItem && !owner.isCreative() ) wand.shrink(1);
                    //These are cost, cooldown and channelling related handling
                    addCastingCooldown(owner, spell, spell.tier * 20);
                    if ( !isFree ) owner.giveExperiencePoints(-spell.tier);
                }
                else {
                    addCastingCooldown(owner, spell, 20);
                    RuneItem.playWhiffSound(caster.level(), center);
                }
            }
        }
        owner.stopUsingItem();
    }

    public static void addCastingCooldown(Player player, TabletItem spell, int cooldown) {
        player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static List<ItemStack> getWandList(ItemStack wand) {
        List<ItemStack> wandList = Lists.newArrayList();
        if ( isValidCastingItem(wand) ) {
            WandData data = CastingItem.getData(wand);
            for ( int i = 0; i < data.getHandler().getSlots(); i++ ) {
                ItemStack rune = data.getHandler().getStackInSlot(i);
                if ( rune.getItem() instanceof ColorRuneItem ) wandList.add(rune);
            }
        }
        return wandList;
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
