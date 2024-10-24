package net.mindoth.ancientmagicks.item.spell.chaoticpolymorph;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.concurrent.ThreadLocalRandom;

public class ChaoticPolymorphItem extends AbstractSpellRayCast {

    public ChaoticPolymorphItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean isHarmful() {
        return true;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        if ( level instanceof ServerLevel serverLevel && target instanceof Mob oldMob ) {
            if ( AncientMagicks.MOB_LIST.isEmpty() ) AncientMagicks.createMobList(serverLevel);
            int index = ThreadLocalRandom.current().nextInt(0, AncientMagicks.MOB_LIST.size());
            Entity entity = AncientMagicks.MOB_LIST.get(index).create(level);
            if ( entity instanceof Mob newMob ) {
                Mob mob = convertMob(oldMob, newMob, serverLevel, false);
                if ( mob.isAddedToWorld() ) return true;
            }
        }
        return false;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
    }

    @Override
    protected int getRed() {
        return 170;
    }

    @Override
    protected int getGreen() {
        return 25;
    }

    @Override
    protected int getBlue() {
        return 170;
    }

    private Mob convertMob(Mob oldMob, Mob newMob, ServerLevel serverLevel, boolean pTransferInventory) {
        newMob.copyPosition(oldMob);
        newMob.setBaby(oldMob.isBaby());
        newMob.setNoAi(oldMob.isNoAi());
        if ( oldMob.hasCustomName() ) {
            newMob.setCustomName(oldMob.getCustomName());
            newMob.setCustomNameVisible(oldMob.isCustomNameVisible());
        }

        if ( oldMob.isPersistenceRequired() ) {
            newMob.setPersistenceRequired();
        }

        newMob.setInvulnerable(oldMob.isInvulnerable());
        if ( pTransferInventory ) {
            newMob.setCanPickUpLoot(oldMob.canPickUpLoot());

            for ( EquipmentSlot equipmentslot : EquipmentSlot.values() ) {
                ItemStack itemstack = oldMob.getItemBySlot(equipmentslot);
                if ( !itemstack.isEmpty() ) {
                    newMob.setItemSlot(equipmentslot, itemstack.copyAndClear());

                    //TODO Find a way to properly transfer dropChance
                    newMob.setDropChance(equipmentslot, 0);
                    //newMob.setDropChance(equipmentslot, oldMob.getEquipmentDropChance(equipmentslot));
                }
            }
        }

        ForgeEventFactory.onFinalizeSpawn(newMob, serverLevel, serverLevel.getCurrentDifficultyAt(newMob.blockPosition()), MobSpawnType.CONVERSION, null, null);
        serverLevel.tryAddFreshEntityWithPassengers(newMob);
        newMob.spawnAnim();
        if ( oldMob.isPassenger() ) {
            Entity entity = oldMob.getVehicle();
            oldMob.stopRiding();
            newMob.startRiding(entity, true);
        }

        oldMob.discard();
        return newMob;
    }
}
