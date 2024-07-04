package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class CastingItem extends Item {

    public CastingItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    public static void doSpell(Player owner, Entity caster, ItemStack castingItem, TabletItem tabletItem, int useTime) {
        float xRot = caster.getXRot();
        float yRot = caster.getYRot();
        Vec3 center;
        float distance = 0.0F;

        if ( distance > 0 ) {
            //Adjusters are there to flip rotation if the caster is not a LivingEntity. Don't ask me why this works like this...
            int adjuster = 1;
            if ( caster != owner ) adjuster = -1;
            Vec3 direction = ShadowEvents.calculateViewVector(xRot * adjuster, yRot * adjuster).normalize();
            direction = direction.multiply(distance, distance, distance);
            center = caster.getEyePosition().add(direction);
        }
        else center = caster.getEyePosition();

        //Check casting bonuses
        boolean hasAlacrity = caster == owner && owner.hasEffect(AncientMagicksEffects.ALACRITY.get());

        //This actually casts the given Spell
        if ( AncientMagicks.isSpellEnabled(tabletItem) ) {
            if ( useTime == 0 && caster == owner ) extractCost(owner, tabletItem);
            if ( tabletItem.castMagic(owner, caster, center, xRot, yRot, useTime) ) {
                if ( !tabletItem.isChannel ) {
                    if ( castingItem.getItem() instanceof TabletItem && !owner.isCreative() ) castingItem.shrink(1);
                    addCastingCooldown(owner, tabletItem, getCastingCooldown(tabletItem.cooldown, hasAlacrity));
                    owner.stopUsingItem();
                }
            }
            else whiffSpell(owner, caster, center, tabletItem);
        }
        else whiffSpell(owner, caster, center, tabletItem);
    }

    public static void extractCost(Player owner, TabletItem tabletItem) {
        if ( !AncientMagicksCommonConfig.FREE_SPELLS.get() && !owner.isCreative() ) owner.giveExperiencePoints(-tabletItem.tier);
    }

    public static int getCastingCooldown(int defaultCooldown, boolean hasAlacrity) {
        float alacrityBonus = hasAlacrity ? 0.5F : 1.0F;
        return (int)(defaultCooldown * alacrityBonus);
    }

    public static void whiffSpell(Player owner, Entity caster, Vec3 center, TabletItem tablet) {
        RuneItem.playWhiffSound(caster);
        addCastingCooldown(owner, tablet, 20);
        owner.stopUsingItem();
    }

    public void makeTablets(Player owner, Entity caster, TabletItem tabletItem, ItemStack slateStack) {
        if ( !AncientMagicks.isSpellEnabled(tabletItem) ) return;
        int stackCount = 0;
        for ( int i = 0; i < slateStack.getCount(); i++ ) {
            if ( stackCount >= slateStack.getCount() ) break;
            boolean canAfford = false;
            if ( owner.totalExperience >= tabletItem.tier || owner.isCreative() ) {
                canAfford = true;
                extractCost(owner, tabletItem);
            }
            if ( canAfford ) stackCount += 1;
        }
        slateStack.shrink(stackCount);
        ItemStack dropStack = new ItemStack(tabletItem, stackCount);
        Vec3 spawnPos = ShadowEvents.getEntityCenter(owner);
        ItemEntity drop = new ItemEntity(owner.level(), spawnPos.x, spawnPos.y, spawnPos.z, dropStack);
        drop.setDeltaMovement(0, 0, 0);
        drop.setNoPickUpDelay();
        caster.level().addFreshEntity(drop);
        addCastingCooldown(owner, tabletItem, 20);
        owner.stopUsingItem();
    }

    public static void addCastingCooldown(Player player, TabletItem spell, int cooldown) {
        player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static @Nonnull ItemStack getHeldSlateItem(Player playerEntity) {
        ItemStack slate = playerEntity.getMainHandItem().getItem() == AncientMagicksItems.STONE_SLATE.get() ? playerEntity.getMainHandItem() : null;
        return slate == null ? (playerEntity.getOffhandItem().getItem() == AncientMagicksItems.STONE_SLATE.get() ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : slate;
    }

    public static @Nonnull ItemStack getHeldTabletItem(Player playerEntity) {
        ItemStack tablet = playerEntity.getMainHandItem().getItem() instanceof TabletItem ? playerEntity.getMainHandItem() : null;
        return tablet == null ? (playerEntity.getOffhandItem().getItem() instanceof TabletItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : tablet;
    }

    public static @Nonnull ItemStack getHeldCastingItem(Player playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof CastingItem ? playerEntity.getMainHandItem() : null;
        return staff == null ? (playerEntity.getOffhandItem().getItem() instanceof CastingItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.getItem() instanceof CastingItem;
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
