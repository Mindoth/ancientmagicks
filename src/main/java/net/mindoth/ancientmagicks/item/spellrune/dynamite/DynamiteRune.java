package net.mindoth.ancientmagicks.item.spellrune.dynamite;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class DynamiteRune extends SpellRuneItem {

    public DynamiteRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        Level level = caster.level();
        playMagicSummonSound(level, center);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        Vec3 point = ShadowEvents.getPoint(level, caster, 3.5F, 0, caster == owner, false, true, true);
        DynamiteEntity tnt = new DynamiteEntity(level, point.x, point.y, point.z, owner);

        tnt.setFuse(tnt.life);
        tnt.speed = Math.max(0, tnt.speed);

        level.addFreshEntity(tnt);
    }
}
