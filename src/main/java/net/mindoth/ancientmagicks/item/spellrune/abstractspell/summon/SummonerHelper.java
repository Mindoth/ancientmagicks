package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SummonerHelper {

    public static LivingEntity getAndCacheOwner(Level level, LivingEntity cachedOwner, UUID summonerUUID) {
        if ( cachedOwner != null && cachedOwner.isAlive() ) {
            return cachedOwner;
        }
        else if ( summonerUUID != null && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            if ( serverLevel.getEntity(summonerUUID) instanceof LivingEntity )
                cachedOwner = (LivingEntity)serverLevel.getEntity(summonerUUID);
            return cachedOwner;
        }
        else {
            return null;
        }
    }

    public static void serializeOwner(CompoundTag compoundTag, UUID ownerUUID) {
        if ( ownerUUID != null ) compoundTag.putUUID("Summoner", ownerUUID);
    }

    public static UUID deserializeOwner(CompoundTag compoundTag) {
        if ( compoundTag.hasUUID("Summoner") ) {
            return compoundTag.getUUID("Summoner");
        }
        return null;
    }
}
