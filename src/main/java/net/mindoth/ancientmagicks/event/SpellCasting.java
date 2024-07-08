package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.List;

public class SpellCasting {

    public static void doSpell(Player owner, Entity caster, ItemStack castingItem, SpellTabletItem spellTabletItem, int useTime) {
        owner.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
            List<SpellTabletItem> knownSpells = ClientSpellData.stringListToSpellList(spell.getKnownSpells());
            boolean doWhiff = false;
            if ( knownSpells.contains(spellTabletItem) || castingItem.getItem() instanceof SpellTabletItem || owner.isCreative() ) {
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

                //This actually casts the given Spell
                if ( AncientMagicks.isSpellEnabled(spellTabletItem) ) {
                    if ( spellTabletItem.castMagic(owner, caster, center, xRot, yRot, useTime) ) {
                        if ( useTime == 0 && caster == owner ) extractCost(owner);
                        if ( !spellTabletItem.isChannel ) {
                            SpellTabletItem.learnSpell(owner, spellTabletItem, castingItem);
                            owner.stopUsingItem();
                        }
                    }
                    else doWhiff = true;
                }
                else doWhiff = true;
            }
            else doWhiff = true;
            if ( doWhiff ) whiffSpell(owner, caster, spellTabletItem, castingItem, useTime);
        });
    }

    public static void whiffSpell(Player owner, Entity caster, SpellTabletItem tablet, ItemStack castingItem, int useTime) {
        if ( useTime > 0 ) SpellTabletItem.learnSpell(owner, tablet, castingItem);
        else addCastingCooldown(owner, tablet, 20);
        RuneItem.playWhiffSound(caster);
        owner.stopUsingItem();
    }

    public static void extractCost(Player owner) {
        if ( !AncientMagicksCommonConfig.FREE_SPELLS.get() && !owner.isCreative() ) owner.giveExperiencePoints(-1);
    }

    public static void makeTablets(Player owner, Entity caster, SpellTabletItem spellTabletItem, ItemStack slateStack) {
        if ( !AncientMagicks.isSpellEnabled(spellTabletItem) ) return;
        int stackCount = 0;
        for ( int i = 0; i < slateStack.getCount(); i++ ) {
            if ( stackCount >= slateStack.getCount() ) break;
            boolean canAfford = false;
            if ( owner.totalExperience >= 1 || owner.isCreative() ) {
                canAfford = true;
                extractCost(owner);
            }
            if ( canAfford ) stackCount += 1;
        }
        slateStack.shrink(stackCount);
        ItemStack dropStack = new ItemStack(spellTabletItem, stackCount);
        Vec3 spawnPos = ShadowEvents.getEntityCenter(owner);
        ItemEntity drop = new ItemEntity(owner.level(), spawnPos.x, spawnPos.y, spawnPos.z, dropStack);
        drop.setDeltaMovement(0, 0, 0);
        drop.setNoPickUpDelay();
        caster.level().addFreshEntity(drop);
        addCastingCooldown(owner, spellTabletItem, 20);
        owner.stopUsingItem();
    }

    public static int getCastingCooldown(int defaultCooldown, boolean hasAlacrity) {
        float alacrityBonus = hasAlacrity ? 0.5F : 1.0F;
        return (int)(defaultCooldown * alacrityBonus);
    }

    public static void addCastingCooldown(Player player, SpellTabletItem spell, int cooldown) {
        player.getCooldowns().addCooldown(spell, cooldown);
    }

    public static @Nonnull ItemStack getHeldSlateItem(Player playerEntity) {
        ItemStack slate = playerEntity.getMainHandItem().getItem() == AncientMagicksItems.EMPTY_RUNE.get() ? playerEntity.getMainHandItem() : null;
        return slate == null ? (playerEntity.getOffhandItem().getItem() == AncientMagicksItems.EMPTY_RUNE.get() ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : slate;
    }

    public static @Nonnull ItemStack getHeldTabletItem(Player playerEntity) {
        ItemStack tablet = playerEntity.getMainHandItem().getItem() instanceof SpellTabletItem ? playerEntity.getMainHandItem() : null;
        return tablet == null ? (playerEntity.getOffhandItem().getItem() instanceof SpellTabletItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : tablet;
    }

    public static @Nonnull ItemStack getHeldCastingItem(Player playerEntity) {
        ItemStack staff = playerEntity.getMainHandItem().getItem() instanceof CastingItem ? playerEntity.getMainHandItem() : null;
        return staff == null ? (playerEntity.getOffhandItem().getItem() instanceof CastingItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : staff;
    }
}
