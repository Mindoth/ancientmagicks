package net.mindoth.ancientmagicks.item.spellrune.witchspark;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class WitchSparkEntity extends AbstractSpellEntity {

    public WitchSparkEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.WITCH_SPARK.get(), level);
    }

    public WitchSparkEntity(EntityType<WitchSparkEntity> entityType, World level) {
        super(entityType, level);
    }

    public WitchSparkEntity(World level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.WITCH_SPARK.get(), level, owner, caster, rune);
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
    public float getDefaultLife() {
        return 40.0F;
    }

    @Override
    protected void doMobEffects(EntityRayTraceResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            dealDamage((LivingEntity)result.getEntity());
        }
    }
}
