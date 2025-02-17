package net.mindoth.ancientmagicks.item.temp.controlweather;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ControlWeatherItem extends SpellItem {

    public ControlWeatherItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, int useTime) {
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
