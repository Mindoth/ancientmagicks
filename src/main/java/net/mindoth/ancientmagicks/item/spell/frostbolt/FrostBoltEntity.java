package net.mindoth.ancientmagicks.item.spell.frostbolt;

import net.mindoth.ancientmagicks.item.SpellTabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class FrostBoltEntity extends AbstractSpellEntity {

    public FrostBoltEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.FROST_BOLT.get(), level);
    }

    public FrostBoltEntity(EntityType<FrostBoltEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FrostBoltEntity(Level level, LivingEntity owner, Entity caster, SpellTabletItem rune) {
        super(AncientMagicksEntities.FROST_BOLT.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 4.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.6F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            LivingEntity living = (LivingEntity)result.getEntity();
            dealDamage(living);
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, false, false));
        }
    }
}
