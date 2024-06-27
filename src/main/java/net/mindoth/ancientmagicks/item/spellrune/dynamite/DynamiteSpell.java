package net.mindoth.ancientmagicks.item.spellrune.dynamite;

import net.mindoth.ancientmagicks.item.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DynamiteSpell extends SpellRuneItem {

    public DynamiteSpell(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        float range = 14.0F;
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, true, true);
        DynamiteEntity tnt = new DynamiteEntity(level, point.x, point.y, point.z, owner);

        tnt.setFuse(tnt.life);
        tnt.speed = Math.max(0, tnt.speed);

        level.addFreshEntity(tnt);
        state = true;

        if ( state ) playMagicSummonSound(level, center);
        else playWhiffSound(level, center);

        return state;
    }
}
