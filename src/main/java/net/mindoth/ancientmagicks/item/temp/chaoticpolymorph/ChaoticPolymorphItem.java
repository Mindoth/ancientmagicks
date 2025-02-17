package net.mindoth.ancientmagicks.item.temp.chaoticpolymorph;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellRayCast;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ChaoticPolymorphItem extends AbstractSpellRayCast {

    public ChaoticPolymorphItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
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
        System.out.println(disabledPolymobs);
        return disabledPolymobs.isEmpty() || !disabledPolymobs.contains(entityType);
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        if ( level instanceof ServerLevel serverLevel ) {
            Mob oldMob = (Mob)target;
            List<EntityType<?>> polymobList = ForgeRegistries.ENTITY_TYPES.getValues().stream()
                    .filter(entityType -> isPolymobEnabled(entityType) && entityType != oldMob.getType() && entityType.create(level) instanceof Mob).toList();
            if ( !polymobList.isEmpty() ) {
                int index = ThreadLocalRandom.current().nextInt(0, polymobList.size());
                Entity entity = polymobList.get(index).create(level);
                if ( entity instanceof Mob newMob ) {
                    convertMob(oldMob, newMob, serverLevel, false);
                    addEnchantParticles(newMob, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8, getRenderType());
                }
            }
        }
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
