package net.mindoth.ancientmagicks.item.temp.witcharrow;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
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

    public WitchArrowEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.WITCH_ARROW.get(), level, owner, caster, spell);
    }

    @Override
    public int defaultPower() {
        return 3;
    }

    @Override
    public int defaultDie() {
        return 4;
    }

    @Override
    public boolean defaultHoming() {
        return true;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        SpellItem.attackEntity(this.owner, target, this, calcDamage());
    }
}
