package net.mindoth.ancientmagicks.item.spell.experiencestream;

import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class ExperienceStreamEntity extends AbstractSpellEntity {

    public ExperienceStreamEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.EXPERIENCE_BEAM.get(), level);
    }

    public ExperienceStreamEntity(EntityType<ExperienceStreamEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ExperienceStreamEntity(Level level, LivingEntity owner, Entity caster, SpellTabletItem rune, boolean spawnOrbs) {
        super(AncientMagicksEntities.EXPERIENCE_BEAM.get(), level, owner, caster, rune);
        this.spawnOrbs = spawnOrbs;
    }

    private boolean spawnOrbs;

    @Override
    public float getDefaultPower() {
        return 1.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.6F;
    }

    @Override
    public float getDefaultLife() {
        return 20.0F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            dealDamage((LivingEntity)result.getEntity());
        }
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        if ( this.spawnOrbs ) spawnOrb();
    }

    @Override
    protected void doExpirationEffects() {
        if ( this.spawnOrbs ) spawnOrb();
        doDeathEffects();
    }

    private void spawnOrb() {
        int value = Mth.floor(this.power);
        if ( value <= 0 ) return;
        Level level = this.level();
        Vec3 center = new Vec3(ShadowEvents.getEntityCenter(this).x, this.position().y, ShadowEvents.getEntityCenter(this).z);
        ExperienceOrb xpOrb = new ExperienceOrb(level, center.x, center.y, center.z, value);
        level.addFreshEntity(xpOrb);
    }

    @Override
    protected void doClientTickEffects() {
    }

    public int getIcon() {
        if ( this.power >= 2477 ) {
            return 10;
        }
        else if ( this.power >= 1237 ) {
            return 9;
        }
        else if ( this.power >= 617 ) {
            return 8;
        }
        else if ( this.power >= 307 ) {
            return 7;
        }
        else if ( this.power >= 149 ) {
            return 6;
        }
        else if ( this.power >= 73 ) {
            return 5;
        }
        else if ( this.power >= 37 ) {
            return 4;
        }
        else if ( this.power >= 17 ) {
            return 3;
        }
        else if ( this.power >= 7 ) {
            return 2;
        }
        else {
            return this.power >= 3 ? 1 : 0;
        }
    }
}
