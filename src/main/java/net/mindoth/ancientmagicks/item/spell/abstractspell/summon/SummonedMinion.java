package net.mindoth.ancientmagicks.item.spell.abstractspell.summon;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface SummonedMinion {
    LivingEntity getSummoner();
    void onUnSummon();

    default boolean isAlliedSummon(Entity entity) {
        if ( getSummoner() == null ) return false;
        boolean isFellowSummon = entity == getSummoner() || entity.isAlliedTo(getSummoner());
        boolean hasCommonOwner = entity instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() == getSummoner();
        boolean isAlly = entity instanceof LivingEntity living && SpellItem.isAlly(getSummoner(), living);
        return isFellowSummon || hasCommonOwner || isAlly;
    }

    default void onDeathHelper() {
        if ( this instanceof LivingEntity entity ) {
            Level level = entity.level();
            var deathMessage = entity.getCombatTracker().getDeathMessage();

            if ( !level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && getSummoner() instanceof ServerPlayer player ) {
                player.sendSystemMessage(deathMessage);
            }
        }
    }

    default void onRemovedHelper(Entity entity, SummonTimer timer) {
        var reason = entity.getRemovalReason();
        if ( reason != null && getSummoner() instanceof ServerPlayer player && reason.shouldDestroy() ) {
            var effect = player.getEffect(timer);
            if ( effect != null ) {
                var decrement = new MobEffectInstance(timer, effect.getDuration(), effect.getAmplifier() - 1, false, false, true);
                if ( decrement.getAmplifier() >= 0 ) {
                    player.getActiveEffectsMap().put(timer, decrement);
                    player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), decrement));
                }
                else {
                    player.removeEffect(timer);
                }
            }
        }
    }
}