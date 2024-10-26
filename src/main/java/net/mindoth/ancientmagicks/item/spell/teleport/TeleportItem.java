package net.mindoth.ancientmagicks.item.spell.teleport;

import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class TeleportItem extends SpellItem {

    public TeleportItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 32.0F;
        if ( owner != caster ) range = 0.0F;

        Vec3 pos;

        Vec3 freePoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, false, false, false);
        Vec3 blockPoint = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, false, false, true, false);
        if ( freePoint == blockPoint ) pos = freePoint;
        else pos = ShadowEvents.getPoint(level, caster, range, 0.0F, caster == owner, true, false, true, false);

        pos = new Vec3(pos.x, pos.y - 0.5D, pos.z);

        //TODO maybe try teleportToWithTicket?
        EntityTeleportEvent.TeleportCommand event = net.minecraftforge.event.ForgeEventFactory.onEntityTeleportCommand(caster, pos.x, pos.y, pos.z);
        if ( !event.isCanceled() ) {
            if ( owner == caster ) caster.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            else if ( owner != null && owner.level() == caster.level() ) owner.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            state = true;
        }

        if ( state ) {
            playTeleportSound(level, pos);
        }

        return state;
    }
}
