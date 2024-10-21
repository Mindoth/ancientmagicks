package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.event.ManaEvents;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
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

    public static void doSpell(Player owner, Entity caster, @Nullable ItemStack castingItem, SpellItem spell, int useTime) {
        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center = caster.getEyePosition();

        //Check casting bonuses
        boolean hasAlacrity = caster == owner && owner.hasEffect(AncientMagicksEffects.ALACRITY.get());

        //This actually casts the given Spell
        if ( AncientMagicks.isSpellEnabled(spell) ) {
            if ( caster == owner && owner.isHolding((item) -> item.getItem() == AncientMagicksItems.SPELL_PEARL.get()
                    && (!item.hasTag() || (item.getTag() != null && !item.getTag().contains("spell_pearl"))))
                    && owner.isHolding((item) -> item.getItem() instanceof CastingItem && !(item.getItem() instanceof SpellPearlItem)) ) {
                ItemStack pearlStack;
                if ( owner.getMainHandItem().getItem() == AncientMagicksItems.SPELL_PEARL.get() ) pearlStack = owner.getMainHandItem();
                else pearlStack = owner.getOffhandItem();
                makePearls(owner, caster, spell, pearlStack);
            }
            else {
                if ( spell.castMagic(owner, caster, center, xRot, yRot, useTime) && castingItem != null ) {
                    if ( !spell.isChannel() ) handleCooldownsAndStuff(owner, castingItem, spell, hasAlacrity);
                }
                else {
                    if ( useTime == 0 ) whiffSpell(owner, caster, spell);
                    else if ( caster == owner && castingItem != null ) handleCooldownsAndStuff(owner, castingItem, spell, hasAlacrity);
                }
            }
        }
        else whiffSpell(owner, caster, spell);
    }

    private static void handleCooldownsAndStuff(Player owner, ItemStack castingItem, SpellItem spell, boolean hasAlacrity) {
        if ( castingItem.getItem() instanceof SpellPearlItem ) {
            if ( !owner.isCreative() ) castingItem.shrink(1);
            owner.getCooldowns().addCooldown(AncientMagicksItems.SPELL_PEARL.get(), 120);
            owner.stopUsingItem();
        }
        else {
            addCastingCooldown(owner, spell, getCastingCooldown(spell.cooldown, hasAlacrity));
            owner.stopUsingItem();
        }
    }

    public static int getCastingCooldown(int defaultCooldown, boolean hasAlacrity) {
        float alacrityBonus = hasAlacrity ? 0.5F : 1.0F;
        return (int)(defaultCooldown * alacrityBonus);
    }

    public static void whiffSpell(Player owner, Entity caster, SpellItem tablet) {
        SpellItem.playWhiffSound(caster);
        addCastingCooldown(owner, tablet, 20);
        owner.stopUsingItem();
    }

    public static void makePearls(Player owner, Entity caster, SpellItem spell, ItemStack pearlStack) {
        if ( !AncientMagicks.isSpellEnabled(spell) ) return;
        if ( !owner.isCreative() ) ManaEvents.changeMana(owner, -spell.manaCost);
        ItemStack dropStack = pearlStack.copyWithCount(1);
        CompoundTag tag = dropStack.getOrCreateTag();
        tag.putString("spell_pearl", ForgeRegistries.ITEMS.getKey(spell).toString());
        pearlStack.shrink(1);
        Vec3 spawnPos = ShadowEvents.getEntityCenter(owner);
        ItemEntity drop = new ItemEntity(owner.level(), spawnPos.x, spawnPos.y, spawnPos.z, dropStack);
        drop.setDeltaMovement(0, 0, 0);
        drop.setNoPickUpDelay();
        caster.level().addFreshEntity(drop);
        addCastingCooldown(owner, spell, spell.cooldown);
        owner.stopUsingItem();
    }

    public static void addCastingCooldown(Player player, SpellItem spell, int cooldown) {
        player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static @Nonnull ItemStack getHeldTabletItem(Player playerEntity) {
        ItemStack tablet = playerEntity.getMainHandItem().getItem() instanceof SpellItem ? playerEntity.getMainHandItem() : null;
        return tablet == null ? (playerEntity.getOffhandItem().getItem() instanceof SpellItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : tablet;
    }

    public static @Nonnull ItemStack getHeldCastingItem(Player playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof CastingItem ? playerEntity.getMainHandItem() : null;
        return staff == null ? (playerEntity.getOffhandItem().getItem() instanceof CastingItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.getItem() instanceof CastingItem && !(castingItem.getItem() instanceof SpellPearlItem);
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
}
