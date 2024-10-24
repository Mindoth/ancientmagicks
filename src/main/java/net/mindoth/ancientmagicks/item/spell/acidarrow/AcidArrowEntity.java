package net.mindoth.ancientmagicks.item.spell.acidarrow;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class AcidArrowEntity extends AbstractSpellEntity {

    public AcidArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.ACID_ARROW.get(), level);
    }

    public AcidArrowEntity(EntityType<AcidArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AcidArrowEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.ACID_ARROW.get(), level, owner, caster, rune);
        this.setNoGravity(false);
    }

    @Override
    protected float getGravity() {
        return 0.03F;
    }

    @Override
    public float getDefaultPower() {
        return 8.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.6F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            LivingEntity living = (LivingEntity)result.getEntity();
            dealDamage(living, 4.0F);
        }
    }
}
