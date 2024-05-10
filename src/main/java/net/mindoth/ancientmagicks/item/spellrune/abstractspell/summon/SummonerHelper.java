package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class SummonerHelper {

    public static LivingEntity getAndCacheOwner(World level, LivingEntity cachedOwner, UUID summonerUUID) {
        if ( cachedOwner != null && cachedOwner.isAlive() ) {
            return cachedOwner;
        }
        else if ( summonerUUID != null && level instanceof ServerWorld ) {
            ServerWorld serverLevel = (ServerWorld)level;
            if ( serverLevel.getEntity(summonerUUID) instanceof LivingEntity )
                cachedOwner = (LivingEntity)serverLevel.getEntity(summonerUUID);
            return cachedOwner;
        }
        else {
            return null;
        }
    }

    public static void serializeOwner(CompoundNBT compoundTag, UUID ownerUUID) {
        if ( ownerUUID != null ) compoundTag.putUUID("Summoner", ownerUUID);
    }

    public static UUID deserializeOwner(CompoundNBT compoundTag) {
        if ( compoundTag.hasUUID("Summoner") ) {
            return compoundTag.getUUID("Summoner");
        }
        return null;
    }
}
