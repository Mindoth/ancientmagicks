package net.mindoth.ancientmagicks.item.spell.dynamite;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DynamiteItem extends SpellItem {

    public DynamiteItem(Properties pProperties, int spellLevel) {
        super(pProperties, spellLevel);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 3.5F;
        if ( owner != caster ) range = 0.0F;

        Vec3 pos;

        Vec3 freePoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, false, false, false);
        Vec3 blockPoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, false, true, false);
        if ( freePoint == blockPoint ) pos = freePoint;
        else pos = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, false, true, false);

        pos = new Vec3(pos.x, pos.y - 0.5D, pos.z);

        DynamiteEntity tnt = new DynamiteEntity(level, pos.x, pos.y, pos.z, owner);

        tnt.setFuse(tnt.life);
        tnt.speed = Math.max(0, tnt.speed);

        level.addFreshEntity(tnt);
        state = true;

        if ( state ) {
            level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            playMagicSummonSound(level, center);
        }

        return state;
    }
}
