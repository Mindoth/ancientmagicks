package net.mindoth.ancientmagicks.item.spell.acidarrow;

import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
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

    public AcidArrowEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.ACID_ARROW.get(), level, owner, caster, spell);
        this.setNoGravity(false);
    }

    @Override
    public int defaultPower() {
        return 4;
    }

    @Override
    public int defaultDie() {
        return 4;
    }

    @Override
    protected int getRenderType() {
        return 2;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        SpellItem.attackEntity(this.owner, target, this, calcDamage());
    }
}
