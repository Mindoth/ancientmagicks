package net.mindoth.ancientmagicks.item.spell.chaoticpolymorph;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.concurrent.ThreadLocalRandom;

public class ChaoticPolymorphTablet extends SpellTabletItem {

    public ChaoticPolymorphTablet(Properties pProperties, boolean isChannel, int cooldown) {
        super(pProperties, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 14.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        //TODO Check if game is in peaceful. Maybe find a way to do conversion instead of spawning and removing
        if ( level instanceof ServerLevel serverLevel && target instanceof Mob targetMob ) {
            if ( AncientMagicks.MOB_LIST.isEmpty() ) AncientMagicks.createMobList(serverLevel);
            int index = ThreadLocalRandom.current().nextInt(0, AncientMagicks.MOB_LIST.size());
            EntityType<?> type = AncientMagicks.MOB_LIST.get(index);
            Mob sheep = convertTo(targetMob, type, false);
            if ( sheep != null ) {
                ForgeEventFactory.onFinalizeSpawn(sheep, serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                serverLevel.tryAddFreshEntityWithPassengers(sheep);
                if ( sheep.isAddedToWorld() ) {
                    sheep.spawnAnim();
                    ShadowEvents.summonParticleLine(ParticleTypes.ENTITY_EFFECT, caster, ShadowEvents.getEntityCenter(caster), ShadowEvents.getEntityCenter(sheep),
                            0, 1, 85F / 255F, 1, 1);
                    state = true;
                }
            }
        }

        if ( state ) playMagicSound(level, center);

        return state;
    }

    private Mob convertTo(Mob target, EntityType<?> type, boolean pTransferInventory) {
        if ( target.isRemoved() ) return null;
        else {
            Entity entity = type.create(target.level());
            if ( entity == null ) return null;
            else if ( entity instanceof Mob t ) {
                t.copyPosition(target);
                t.setBaby(target.isBaby());
                t.setNoAi(target.isNoAi());
                if ( target.hasCustomName() ) {
                    t.setCustomName(target.getCustomName());
                    t.setCustomNameVisible(target.isCustomNameVisible());
                }
                if ( target.isPersistenceRequired() ) t.setPersistenceRequired();
                t.setInvulnerable(target.isInvulnerable());
                if ( pTransferInventory ) {
                    t.setCanPickUpLoot(target.canPickUpLoot());
                    for ( EquipmentSlot equipmentslot : EquipmentSlot.values() ) {
                        ItemStack itemstack = target.getItemBySlot(equipmentslot);
                        if ( !itemstack.isEmpty() ) {
                            t.setItemSlot(equipmentslot, itemstack.copyAndClear());
                            //TODO set the correct equipment drop chances
                            t.setDropChance(equipmentslot, 0);
                        }
                    }
                }
                target.level().addFreshEntity(t);
                if ( target.isPassenger() ) {
                    Entity vehicle = target.getVehicle();
                    target.stopRiding();
                    t.startRiding(vehicle, true);
                }
                target.discard();
                return t;
            }
        }
        return null;
    }
}
