package net.mindoth.ancientmagicks.item.spell.blizzard;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlizzardItem extends SpellItem {

    public BlizzardItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.AQUA.getParticleColor();
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        int adjuster = 1;
        float down = -0.2F;
        if ( caster != owner ) {
            adjuster = -1;
            down = 0.0F;
        }

        float range = 14.0F;
        if ( owner != caster ) range = 0.0F;

        Vec3 pos = ShadowEvents.getPoint(level, caster, range, 0.25F, caster == owner, false, true, true, false);

        for ( int i = 0; i < 2; i++ ) spawnIcicles(owner, caster, level, pos, yRot, adjuster, useTime);
        state = true;

        if ( state ) {
            if ( useTime % 3 == 0 ) playWindSound(level, pos);
        }

        return state;
    }

    private void spawnIcicles(Player owner, Entity caster, Level level, Vec3 center, float yRot, int adjuster, int useTime) {
        AbstractSpellEntity projectile = new IcicleEntity(level, owner, caster, this);
        projectile.setAdditionalData(getParticleColor());
        double newX = center.x;
        double newY = getHeight(level, center);
        double newZ = center.z;
        if ( useTime % 20 != 0 ) {
            newX += getRandomPos().x;
            newZ += getRandomPos().z;
        }
        projectile.setPos(newX, newY, newZ);
        projectile.anonShootFromRotation(90 * adjuster, 0, 0, 0.15F, 0.0F);
        level.addFreshEntity(projectile);

        ServerLevel serverLevel = (ServerLevel)level;
        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, newX, newY, newZ, 0, 0, 0, 0, 0);
    }

    private Vec3 getRandomPos() {
        float size = 2.5F;
        float randX = (float)((Math.random() * (size - (-size))) + (-size));
        float randZ = (float)((Math.random() * (size - (-size))) + (-size));
        return new Vec3(randX, 0, randZ);
    }

    private float getHeight(Level level, Vec3 center) {
        float returnHeight = (float)center.y;
        BlockPos pos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
        for ( int i = pos.getY(); i < level.getMaxBuildHeight(); i++ ) {
            BlockPos tempPos = new BlockPos(pos.getX(), i + 1, pos.getZ());
            if ( level.getBlockState(tempPos).isSolid() || i == pos.getY() + 7 ) {
                returnHeight = i;
                break;
            }
        }
        return returnHeight;
    }
}
