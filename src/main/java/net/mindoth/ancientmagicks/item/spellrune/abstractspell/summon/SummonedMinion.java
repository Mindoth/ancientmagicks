package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public interface SummonedMinion {
    LivingEntity getSummoner();
    void onUnSummon();

    default boolean isAlliedSummon(Entity entity) {
        if ( getSummoner() == null ) return false;
        boolean isFellowSummon = entity == getSummoner() || entity.isAlliedTo(getSummoner());
        boolean hasCommonOwner = entity instanceof OwnableEntity && ((OwnableEntity)entity).getOwner() == getSummoner();
        return isFellowSummon || hasCommonOwner;
    }

    default void onDeathHelper() {
        if ( this instanceof LivingEntity ) {
            LivingEntity entity = (LivingEntity)this;
            World level = entity.level;
            ITextComponent deathMessage = entity.getCombatTracker().getDeathMessage();

            if ( !level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && getSummoner() instanceof ServerPlayerEntity ) {
                ServerPlayerEntity player = (ServerPlayerEntity)getSummoner();
                player.displayClientMessage(deathMessage, false);
            }
        }
    }

    default void onRemovedHelper(Entity entity, SummonTimer timer) {
        //var reason = entity.getRemovalReason();
        if ( /*reason != null && */getSummoner() instanceof ServerPlayerEntity/* && reason.shouldDestroy()*/ ) {
            ServerPlayerEntity player = (ServerPlayerEntity)getSummoner();
            EffectInstance effect = player.getEffect(timer);
            if ( effect != null ) {
                EffectInstance decrement = new EffectInstance(timer, effect.getDuration(), effect.getAmplifier() - 1, false, false, true);
                if ( decrement.getAmplifier() >= 0 ) {
                    player.getActiveEffectsMap().put(timer, decrement);
                    player.connection.send(new SPlayEntityEffectPacket(player.getId(), decrement));
                }
                else {
                    player.removeEffect(timer);
                }
            }
        }
    }
}