package net.mindoth.ancientmagicks.item.spell.chaoticpolymorph;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.spell.EntityTargetSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ChaoticPolymorphSpellItem extends EntityTargetSpell {

    public ChaoticPolymorphSpellItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result) {
        return result instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity
                && allyFilter(owner, entityHitResult.getEntity()) && mobTypeFilter(entityHitResult.getEntity());
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob;
    }

    public static boolean isPolymobEnabled(EntityType<?> entityType) {
        List<EntityType<?>> disabledPolymobs = Lists.newArrayList();
        List<String> configString = AncientMagicksCommonConfig.DISABLED_POLYMOBS.get();
        for ( String string : configString ) {
            if ( Objects.equals(string.split(":")[1], "*") ) {
                for ( EntityType<?> type : ForgeRegistries.ENTITY_TYPES.getValues() ) {
                    String typeId = type.toString().split("\\.")[1];
                    if ( typeId.equals(string.split(":")[0]) ) disabledPolymobs.add(type);
                }
            }
            else disabledPolymobs.add(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(string)));
        }
        return disabledPolymobs.isEmpty() || !disabledPolymobs.contains(entityType);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        boolean state = false;
        if ( level instanceof ServerLevel serverLevel ) {
            EntityHitResult entityHitResult = (EntityHitResult)result;
            Mob oldMob = (Mob)entityHitResult.getEntity();
            List<EntityType<?>> polymobList = ForgeRegistries.ENTITY_TYPES.getValues().stream()
                    .filter(entityType -> isPolymobEnabled(entityType) && entityType != oldMob.getType() && entityType.create(level) instanceof Mob).toList();
            if ( !polymobList.isEmpty() ) {
                int index = ThreadLocalRandom.current().nextInt(0, polymobList.size());
                Entity entity = polymobList.get(index).create(level);
                if ( entity instanceof Mob newMob ) {
                    convertMob(oldMob, newMob, serverLevel, false);
                    addEnchantParticles(newMob, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
                    state = true;
                }
            }
        }
        return state;
    }

    public static void convertMob(Mob oldMob, Mob newMob, ServerLevel serverLevel, boolean pTransferInventory) {
        newMob.copyPosition(oldMob);
        newMob.setBaby(oldMob.isBaby());
        newMob.setNoAi(oldMob.isNoAi());
        if ( oldMob.hasCustomName() ) {
            newMob.setCustomName(oldMob.getCustomName());
            newMob.setCustomNameVisible(oldMob.isCustomNameVisible());
        }
        if ( oldMob.isPersistenceRequired() ) newMob.setPersistenceRequired();
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
        if ( oldMob.isPassenger() ) {
            Entity entity = oldMob.getVehicle();
            oldMob.stopRiding();
            newMob.startRiding(entity, true);
        }
        oldMob.discard();
        //return newMob;
    }
}
