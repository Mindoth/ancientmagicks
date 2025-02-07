package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

    public static void doSpell(LivingEntity owner, Entity caster, @Nullable ItemStack stack, SpellItem spell, int useTime) {
        Item castingItem = stack != null ? stack.getItem() : null;
        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center = caster instanceof SpellPearlEntity ? caster.position() : caster.getEyePosition();

        //Check casting bonuses
        boolean hasAlacrity = caster instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.ALACRITY.get());

        //Cast the given Spell
        if ( AncientMagicks.isSpellEnabled(spell) ) {
            //Store Spell into item
            if ( caster == owner
                    && owner.isHolding((heldItem) -> heldItem.getItem() instanceof SpecialCastingItem && SpecialCastingItem.getStoredSpell(heldItem) == null)
                    && owner.isHolding((heldItem) -> heldItem.getItem() instanceof StaffItem) ) {
                ItemStack vessel = owner.getMainHandItem().getItem() instanceof SpecialCastingItem ? owner.getMainHandItem() : owner.getOffhandItem();
                storeSpell(owner, caster, spell, vessel);
            }
            //ACTUALLY cast the spell
            else if ( stack != null ) {
                if ( spell.castMagic(owner, caster, center, xRot, yRot, useTime) ) {
                    if ( castingItem instanceof StaffItem && useTime % 10 == 0 ) MagickEvents.changeMana(caster, -spell.getManaCost());
                    if ( caster instanceof LivingEntity living ) {
                        //Handle cooldown on non-channel spells right after casting
                        if ( !spell.isChannel() ) handleCooldownsAndStuff(living, stack, spell, hasAlacrity);
                        //Reduce durability on first tick of spells
                        if ( useTime == 0 ) addItemDamage(stack, 1, living);
                    }
                }
                else {
                    if ( useTime == 0 ) whiffSpell(caster, spell);
                    //If spell fails while it's being used, it is a channel spell
                    else if ( spell.isChannel() ) handleCooldownsAndStuff(caster, stack, spell, hasAlacrity);
                    //Manual channel spell stopping is handled in MagickEvents.class
                }
            }
            //Handling for Spell Pearl
            else spell.castMagic(owner, caster, center, xRot, yRot, useTime);
        }
        else whiffSpell(caster, spell);
    }

    private static void handleCooldownsAndStuff(Entity entity, ItemStack castingItem, SpellItem spell, boolean hasAlacrity) {
        Item item = castingItem.getItem();
        float alacrityBonus = hasAlacrity ? 0.5F : 1.0F;
        int spellCooldown = (int)(spell.getCooldown() * alacrityBonus);
        if ( item instanceof StaffItem || item instanceof WandItem ) {
            if ( item instanceof WandItem ) spell = SpecialCastingItem.getStoredSpell(castingItem);
            addCastingCooldown(entity, spell, spellCooldown);
        }
        if ( entity instanceof LivingEntity living ) living.stopUsingItem();
    }

    public static void addItemDamage(ItemStack castingItem, int amount, LivingEntity living) {
        castingItem.hurtAndBreak(amount, living, (holder) -> holder.broadcastBreakEvent(living.getUsedItemHand()));
    }

    public static void addCastingCooldown(Entity entity, SpellItem spell, int cooldown) {
        if ( entity instanceof Player player ) player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static void storeSpell(LivingEntity living, Entity caster, SpellItem spell, ItemStack vessel) {
        if ( !(living instanceof ServerPlayer owner) ) return;
        owner.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            boolean state = AncientMagicks.isSpellEnabled(spell);
            int manaCost = 0;
            if ( !owner.isCreative() ) manaCost += spell.getManaCost();
            if ( magic.getCurrentMana() < manaCost ) state = false;
            if ( state ) {
                if ( !owner.isCreative() ) MagickEvents.changeMana(owner, -manaCost);
                ItemStack dropStack = vessel.copyWithCount(1);
                CompoundTag tag = dropStack.getOrCreateTag();
                tag.putString(SpecialCastingItem.TAG_STORED_SPELL, ForgeRegistries.ITEMS.getKey(spell).toString());
                vessel.shrink(1);
                Vec3 spawnPos = ShadowEvents.getEntityCenter(owner);
                ItemEntity drop = new ItemEntity(owner.level(), spawnPos.x, spawnPos.y, spawnPos.z, dropStack);
                drop.setDeltaMovement(0, 0, 0);
                drop.setNoPickUpDelay();
                caster.level().addFreshEntity(drop);
                addCastingCooldown(owner, spell, spell.getCooldown());
                owner.stopUsingItem();
            }
            else whiffSpell(caster, spell);
        });
    }

    public static void whiffSpell(Entity caster, SpellItem spell) {
        SpellItem.playWhiffSound(caster);
        if ( caster instanceof LivingEntity living ) {
            addCastingCooldown(living, spell, 20);
            living.stopUsingItem();
        }
    }

    public static @Nonnull ItemStack getHeldStaff(LivingEntity playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof StaffItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( staff == ItemStack.EMPTY ) staff = playerEntity.getOffhandItem().getItem() instanceof StaffItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return staff;
    }

    public static @Nonnull ItemStack getHeldCastingItem(LivingEntity playerEntity) {
        ItemStack staff = isValidCastingItem(playerEntity.getMainHandItem()) ? playerEntity.getMainHandItem() : null;
        return staff == null ? (isValidCastingItem(playerEntity.getOffhandItem()) ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        Item item = castingItem.getItem();
        return item instanceof StaffItem || (item instanceof SpecialCastingItem && SpecialCastingItem.getStoredSpell(castingItem) != null);
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
