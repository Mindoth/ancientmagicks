package net.mindoth.ancientmagicks.mobeffect;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ChaoticPolymorphEffect extends InstantenousMobEffect {

    public ChaoticPolymorphEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( !(living instanceof Mob oldMob) ) return;
        Level level = oldMob.level();
        if ( !(level instanceof ServerLevel serverLevel) ) return;
        List<EntityType<?>> polymobList = ForgeRegistries.ENTITY_TYPES.getValues().stream()
                .filter(entityType -> isPolymobEnabled(entityType) && entityType != oldMob.getType() && entityType.create(level) instanceof Mob).toList();
        if ( polymobList.isEmpty() ) return;
        int index = ThreadLocalRandom.current().nextInt(0, polymobList.size());
        Entity entity = polymobList.get(index).create(level);
        if ( entity instanceof Mob newMob ) convertMob(oldMob, newMob, serverLevel, false);
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

    @SubscribeEvent
    public static void onEntityChaoticPolymorph(final MobEffectEvent.Applicable event) {
        if ( event.getEffectInstance().getEffect() == AncientMagicksEffects.CHAOTIC_POLYMORPH.get() ) {
            if ( event.getEntity() instanceof Player ) event.setResult(Event.Result.DENY);
            else event.setResult(Event.Result.DEFAULT);
        }
    }
}
