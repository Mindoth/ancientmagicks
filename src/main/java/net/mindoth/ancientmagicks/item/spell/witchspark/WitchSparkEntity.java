package net.mindoth.ancientmagicks.item.spell.witchspark;

import net.mindoth.ancientmagicks.item.SpellTabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class WitchSparkEntity extends AbstractSpellEntity {

    public WitchSparkEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.WITCH_SPARK.get(), level);
    }

    public WitchSparkEntity(EntityType<WitchSparkEntity> entityType, Level level) {
        super(entityType, level);
    }

    public WitchSparkEntity(Level level, LivingEntity owner, Entity caster, SpellTabletItem rune) {
        super(AncientMagicksEntities.WITCH_SPARK.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 6.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.6F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            dealDamage((LivingEntity)result.getEntity());
        }
    }
}
