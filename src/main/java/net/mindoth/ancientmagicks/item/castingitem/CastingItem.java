package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CastingItem extends Item {

    public CastingItem(Properties pProperties) {
        super(pProperties);
    }

    public static void doSpell(Player owner, Entity caster, @Nullable ItemStack stack, SpellItem spell, int useTime) {
        Item castingItem = stack != null ? stack.getItem() : null;
        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center = caster instanceof SpellPearlEntity ? caster.position() : caster.getEyePosition();

        //Check casting bonuses
        boolean hasAlacrity = caster == owner && owner.hasEffect(AncientMagicksEffects.ALACRITY.get());

        //Cast the given Spell
        if ( AncientMagicks.isSpellEnabled(spell) ) {
            //Store Spell into item
            if ( caster == owner
                    && owner.isHolding((heldItem) -> heldItem.getItem() instanceof SpellStorageItem && SpellStorageItem.getStoredSpell(heldItem) == null)
                    && owner.isHolding((heldItem) -> heldItem.getItem() instanceof StaffItem) ) {
                ItemStack vessel = owner.getMainHandItem().getItem() instanceof SpellStorageItem ? owner.getMainHandItem() : owner.getOffhandItem();
                storeSpell(owner, caster, spell, vessel);
            }
            //ACTUALLY cast the spell
            else {
                if ( spell.castMagic(owner, caster, center, xRot, yRot, useTime) && stack != null ) {
                    if ( caster == owner && castingItem instanceof StaffItem && useTime % 10 == 0 ) MagickEvents.changeMana(owner, -spell.manaCost);
                    if ( !spell.isChannel() ) handleCooldownsAndStuff(owner, stack, spell, hasAlacrity);
                }
                else {
                    if ( useTime == 0 ) whiffSpell(owner, caster, spell);
                    else if ( caster == owner && stack != null ) handleCooldownsAndStuff(owner, stack, spell, hasAlacrity);
                }
            }
        }
        else whiffSpell(owner, caster, spell);
    }

    private static void handleCooldownsAndStuff(Player owner, ItemStack castingItem, SpellItem spell, boolean hasAlacrity) {
        Item item = castingItem.getItem();
        float alacrityBonus = hasAlacrity ? 0.5F : 1.0F;
        int spellCooldown = (int)(spell.cooldown * alacrityBonus);
        if ( item instanceof StaffItem || item instanceof WandItem ) {
            if ( item instanceof StaffItem ) addCastingCooldown(owner, spell, spellCooldown);
            else addCastingCooldown(owner, SpellStorageItem.getStoredSpell(castingItem), spellCooldown);
            if ( !owner.isCreative() ) castingItem.hurtAndBreak(1, owner, (holder) -> holder.broadcastBreakEvent(owner.getUsedItemHand()));
        }
        else if ( item instanceof SpellStorageItem ) {
            owner.getCooldowns().addCooldown(item, 120);
            if ( !owner.isCreative() ) castingItem.shrink(1);
        }
        owner.stopUsingItem();
    }

    public static void addCastingCooldown(Player player, SpellItem spell, int cooldown) {
        player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static void storeSpell(Player player, Entity caster, SpellItem spell, ItemStack vessel) {
        if ( !(player instanceof ServerPlayer owner) ) return;
        owner.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            boolean state = AncientMagicks.isSpellEnabled(spell);
            int manaCost = 0;
            if ( !owner.isCreative() ) {
                if ( vessel.getItem() == AncientMagicksItems.ANCIENT_TABLET.get() ) manaCost += spell.spellTier * 100;
                else manaCost += spell.manaCost;
            }
            if ( magic.getCurrentMana() < manaCost ) state = false;
            if ( state ) {
                if ( !owner.isCreative() ) MagickEvents.changeMana(owner, -manaCost);
                ItemStack dropStack = vessel.copyWithCount(1);
                CompoundTag tag = dropStack.getOrCreateTag();
                tag.putString(SpellStorageItem.TAG_STORED_SPELL, ForgeRegistries.ITEMS.getKey(spell).toString());
                vessel.shrink(1);
                Vec3 spawnPos = ShadowEvents.getEntityCenter(owner);
                ItemEntity drop = new ItemEntity(owner.level(), spawnPos.x, spawnPos.y, spawnPos.z, dropStack);
                drop.setDeltaMovement(0, 0, 0);
                drop.setNoPickUpDelay();
                caster.level().addFreshEntity(drop);
                addCastingCooldown(owner, spell, spell.cooldown);
                owner.stopUsingItem();
            }
            else whiffSpell(owner, caster, spell);
        });
    }

    public static void whiffSpell(Player owner, Entity caster, SpellItem spell) {
        SpellItem.playWhiffSound(caster);
        addCastingCooldown(owner, spell, 20);
        owner.stopUsingItem();
    }

    public static @Nonnull ItemStack getHeldStaff(Player playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof StaffItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( staff == ItemStack.EMPTY ) staff = playerEntity.getOffhandItem().getItem() instanceof StaffItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return staff;
    }

    public static @Nonnull ItemStack getHeldCastingItem(Player playerEntity) {
        ItemStack staff = isValidCastingItem(playerEntity.getMainHandItem()) ? playerEntity.getMainHandItem() : null;
        return staff == null ? (isValidCastingItem(playerEntity.getOffhandItem()) ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        Item item = castingItem.getItem();
        return item instanceof StaffItem || (item instanceof SpellStorageItem && SpellStorageItem.getStoredSpell(castingItem) != null);
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
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
