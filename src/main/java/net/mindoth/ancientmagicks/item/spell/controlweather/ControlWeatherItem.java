package net.mindoth.ancientmagicks.item.spell.controlweather;

import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ControlWeatherItem extends SpellItem {

    public ControlWeatherItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        if ( !level.getLevelData().isRaining() && !level.getLevelData().isThundering() ) level.getLevelData().setRaining(true);
        else if ( level.getLevelData().isRaining() || level.getLevelData().isThundering() ) level.getLevelData().setRaining(false);
        state = true;

        if ( state ) {
            playMagicSound(level, center);
        }

        return state;
    }
}
