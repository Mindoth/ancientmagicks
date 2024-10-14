package net.mindoth.ancientmagicks.item.spell.blizzard;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.icicle.IcicleEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlizzardItem extends SpellItem {

    public BlizzardItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getCooldown() {
        return 40;
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

        spawnIcicles(owner, caster, level, pos, yRot, adjuster, useTime);
        state = true;

        if ( state ) {
            if ( useTime % 3 == 0 ) playWindSound(level, pos);
        }

        return state;
    }

    private void spawnIcicles(Player owner, Entity caster, Level level, Vec3 center, float yRot, int adjuster, int useTime) {
        AbstractSpellEntity projectile = new IcicleEntity(level, owner, caster, this);
        projectile.speed *= 0.5F;
        projectile.setColor(AbstractSpellEntity.getSpellColor("white"), 0.3F);
        if ( useTime % 20 == 0 ) projectile.setPos(center.x, getHeight(level, center), center.z);
        else {
            Vec3 setPos = new Vec3(center.x + getRandomPos().x, getHeight(level, center), center.z + getRandomPos().z);
            /*BlockPos newPos = new BlockPos(Mth.floor(setPos.x), Mth.floor(setPos.y), Mth.floor(setPos.z));
            while ( level.getBlockState(newPos).isSolid() ) {
                setPos = new Vec3(center.x + getRandomPos().x, getHeight(level, center), center.z + getRandomPos().z);
                newPos = new BlockPos(Mth.floor(setPos.x), Mth.floor(setPos.y), Mth.floor(setPos.z));
            }*/
            projectile.setPos(setPos);
        }
        projectile.anonShootFromRotation(90, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
        level.addFreshEntity(projectile);
    }

    private Vec3 getRandomPos() {
        float size = 2.0F;
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
