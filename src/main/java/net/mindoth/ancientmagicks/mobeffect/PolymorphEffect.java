package net.mindoth.ancientmagicks.mobeffect;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class PolymorphEffect extends MobEffect {

    public PolymorphEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final String NBT_KEY_OLD_MOB = "am_polymorphed_entity";

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( living instanceof Mob target && target.level() instanceof ServerLevel level && target.getType() != EntityType.SHEEP ) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", EntityType.getKey(target.getType()).toString());
            target.saveWithoutId(tag);
            Sheep sheep = target.convertTo(EntityType.SHEEP, false);
            if ( target.hasEffect(ModEffects.POLYMORPH.get()) ) sheep.addEffect(target.getEffect(ModEffects.POLYMORPH.get()));
            sheep.finalizeSpawn(level, level.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
            sheep.getPersistentData().put(NBT_KEY_OLD_MOB, tag);
        }
    }

    @SubscribeEvent
    public static void transformBackWhenAttacked(final LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(ModEffects.POLYMORPH.get()) ) living.removeEffect(ModEffects.POLYMORPH.get());
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( living instanceof Mob target && target.level() instanceof ServerLevel level && target.getPersistentData().contains(NBT_KEY_OLD_MOB) ) {
            transformBack(target.getPersistentData().getCompound(NBT_KEY_OLD_MOB), level, living);
        }
    }

    private boolean transformBack(CompoundTag tag, ServerLevel level, LivingEntity living) {
        if ( tag.isEmpty() || !(living instanceof Mob oldMob) ) return false;
        ForgeRegistries.ENTITY_TYPES.getValue(EntityType.getKey(oldMob.getType()));
        return EntityType.create(tag, level).map((entity -> {
            entity.setPos(oldMob.position());
            entity.setDeltaMovement(oldMob.getDeltaMovement());
            if ( entity instanceof LivingEntity newLiving && newLiving.hasEffect(ModEffects.POLYMORPH.get()) ) {
                newLiving.removeEffect(ModEffects.POLYMORPH.get());
            }
            level.addFreshEntity(entity);
            oldMob.discard();
            //if ( entity instanceof Mob newMob ) ChaoticPolymorphItem.convertMob(oldMob, newMob, level, false);
            return entity;
        })).isPresent();
    }

    @SubscribeEvent
    public static void onEntityPolymorph(final MobEffectEvent.Applicable event) {
        if ( event.getEffectInstance().getEffect() == ModEffects.POLYMORPH.get() ) {
            if ( event.getEntity() instanceof Player ) event.setResult(Event.Result.DENY);
            else event.setResult(Event.Result.DEFAULT);
        }
    }
}
