package net.mindoth.ancientmagicks.item.spell.blink;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlinkTablet extends TabletItem {

    public BlinkTablet(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 point = ShadowEvents.getPoint(level, caster, 14.0F, 0, caster == owner, false, false, true, false);
        //TODO maybe try teleportToWithTicket?
        caster.teleportTo(point.x, point.y, point.z);
        state = true;

        if ( state ) playEnderSound(level, point);

        return state;
    }
}
