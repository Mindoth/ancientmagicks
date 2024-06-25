package net.mindoth.ancientmagicks.item.spellrune.blink;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlinkRune extends SpellRuneItem {

    public BlinkRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        Level level = caster.level();
        Vec3 point = ShadowEvents.getPoint(level, caster, 14.0F, 0, caster == owner, false, false, false);
        playEnderSound(level, point);
        caster.teleportTo(point.x, point.y, point.z);
    }
}
