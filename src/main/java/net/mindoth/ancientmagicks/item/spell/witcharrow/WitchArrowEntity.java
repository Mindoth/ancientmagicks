package net.mindoth.ancientmagicks.item.spell.witcharrow;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class WitchArrowEntity extends AbstractSpellEntity {

    public WitchArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.WITCH_ARROW.get(), level);
    }

    public WitchArrowEntity(EntityType<WitchArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public WitchArrowEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.WITCH_ARROW.get(), level, owner, caster, rune);
        this.setNoGravity(false);
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
    public boolean getDefaultHoming() {
        return true;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            LivingEntity living = (LivingEntity)result.getEntity();
            dealDamage(living, 1.0F);
        }
    }
}
