package net.mindoth.ancientmagicks.item.spell.mindcontrol;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class MindControlEffect extends MobEffect {
    public MindControlEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final String NBT_KEY_CONTROL = "am_controlling_entity";
    public static final String NBT_KEY_SUMMON = "am_is_summon";

    public static boolean setMindControlTarget(Mob mob, LivingEntity owner, Level level) {
        LivingEntity mindControlTarget = findMindControlTarget(mob, owner, level);
        if ( mindControlTarget != null ) {
            mob.setTarget(mindControlTarget);
            return true;
        }
        else return false;
    }

    public static LivingEntity findMindControlTarget(Mob cMob, LivingEntity cMobOwner, Level level) {
        List<LivingEntity> possibleTargets = ShadowEvents.getEntitiesAround(cMob, level, cMob.getAttributeValue(Attributes.FOLLOW_RANGE), null);
        possibleTargets.remove(cMob);
        possibleTargets.removeIf(e -> e instanceof ArmorStand);
        possibleTargets.removeIf(e -> SpellItem.isAlly(cMobOwner, e));
        possibleTargets.removeIf(e -> e instanceof Mob mob && SpellItem.isMinionsOwner(cMobOwner, mob) && cMobOwner.getLastHurtMob() != mob);

        LivingEntity newTarget = null;

        for ( LivingEntity possibleTarget : possibleTargets ) {
            if ( isTargetable(cMobOwner, possibleTarget) ) {
                if ( newTarget == null || cMob.distanceTo(possibleTarget) < cMob.distanceTo(newTarget) ) newTarget = possibleTarget;
            }
        }

        return newTarget;
    }

    private static boolean isTargetable(LivingEntity cMobOwner, LivingEntity target) {
        if ( cMobOwner == null ) return false;
        if ( SpellItem.isAlly(cMobOwner, target.getLastHurtByMob()) ) return true;
        else if ( SpellItem.isAlly(cMobOwner, target) ) return cMobOwner.getLastHurtMob() == target || cMobOwner.getLastHurtByMob() == target;
        else return cMobOwner.getLastHurtMob() == target || cMobOwner.getLastHurtByMob() == target || (target instanceof Mob mob && mob.getTarget() == cMobOwner);
    }

    private static void handleTargeting(Level level, Mob cMob, LivingEntity cMobTarget) {
        if ( !cMob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) ) return;
        CompoundTag tag = cMob.getPersistentData();
        if ( tag.hasUUID(NBT_KEY_CONTROL) ) {
            Entity entity = getEntityByUUID(level, tag.getUUID(NBT_KEY_CONTROL));
            if ( entity instanceof LivingEntity owner ) {
                if ( isTargetable(cMobTarget, cMob) || setMindControlTarget(cMob, owner, level) ) return;
            }
        }
        else cMob.setTarget(null);
    }

    @SubscribeEvent
    public static void onMindControlledUpdate(final LivingEvent.LivingTickEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        if ( event.getEntity() instanceof Mob mob && mob.tickCount % 50 == 0 && mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) ) {
            if ( mob.getTarget() == null || !mob.getTarget().isAlive() ) handleTargeting(mob.level(), mob, mob.getTarget());
        }
    }

    @SubscribeEvent
    public static void onSetMindControlTarget(final LivingChangeTargetEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        if ( !event.getEntity().hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) ) return;
        if ( event.getEntity() instanceof Mob mob && event.getOriginalTarget() != null ) {
            CompoundTag tag = mob.getPersistentData();
            if ( !tag.hasUUID(NBT_KEY_CONTROL) ) return;
            Entity entity = getEntityByUUID(event.getEntity().level(), tag.getUUID(NBT_KEY_CONTROL));
            if ( entity instanceof LivingEntity cMobOwner && SpellItem.isAlly(cMobOwner, event.getOriginalTarget()) ) {
                handleTargeting(mob.level(), mob, event.getOriginalTarget());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMindControlExpiration(final MobEffectEvent.Expired event) {
        if ( event.getEntity().level().isClientSide ) return;
        onMindControlEnd(event.getEffectInstance(), event.getEntity());
    }

    @SubscribeEvent
    public static void onMindControlRemoved(final MobEffectEvent.Remove event) {
        if ( event.getEntity().level().isClientSide ) return;
        onMindControlEnd(event.getEffectInstance(), event.getEntity());
    }

    private static void onMindControlEnd(MobEffectInstance instance, Entity entity) {
        if ( instance != null && instance.getEffect() == AncientMagicksEffects.MIND_CONTROL.get() && entity instanceof Mob mob ) {
            if ( mob.getPersistentData().getBoolean("am_is_minion") ) mob.kill();
            else {
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerMindControl(final MobEffectEvent.Applicable event) {
        if ( event.getEffectInstance().getEffect() == AncientMagicksEffects.MIND_CONTROL.get() ) {
            if ( event.getEntity() instanceof Player ) event.setResult(Event.Result.DENY);
            else event.setResult(Event.Result.DEFAULT);
        }
    }

    @SubscribeEvent
    public static void onMinionLootDrop(final LivingDropsEvent event) {
        if ( event.getEntity() instanceof Mob mob && mob.getPersistentData().getBoolean("am_is_minion") ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMinionXpDrop(final LivingExperienceDropEvent event) {
        if ( event.getEntity() instanceof Mob mob && mob.getPersistentData().getBoolean("am_is_minion") ) {
            event.setCanceled(true);
        }
    }

    //TODO Move this to ShadowizardLib
    @Nullable
    public static Entity getEntityByUUID(Level level, @Nullable UUID uuid) {
        if ( uuid == null || !(level instanceof ServerLevel serverLevel) ) return null;
        for ( Entity entity : serverLevel.getAllEntities() ) if ( entity != null && entity.getUUID().equals(uuid) ) return entity;
        return null;
    }
}
