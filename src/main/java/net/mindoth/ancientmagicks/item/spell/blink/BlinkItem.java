package net.mindoth.ancientmagicks.item.spell.blink;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlinkItem extends SpellItem {

    public BlinkItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isAncient() {
        return true;
    }

    @Override
    public int getCooldown() {
        return 80;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 14.0F;
        if ( owner != caster ) range = 0.0F;

        Vec3 pos;

        Vec3 freePoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, false, false, false);
        Vec3 blockPoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, false, true, false);
        if ( freePoint == blockPoint ) pos = freePoint;
        else pos = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, false, true, false);

        pos = new Vec3(pos.x, pos.y - 0.5D, pos.z);

        //TODO maybe try teleportToWithTicket?
        if ( owner == caster ) caster.teleportTo(pos.x, pos.y, pos.z);
        else if ( owner != null && owner.level() == caster.level() ) owner.teleportTo(pos.x, pos.y, pos.z);
        state = true;

        if ( state ) playTeleportSound(level, pos);

        return state;
    }
}
