package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UseOnPositionTemplate extends RuneItem {
    public UseOnPositionTemplate(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getPositions().isEmpty() ) {
            if ( spellData.getLatestPosition() != null ) {
                Vec3 position = spellData.getLatestPosition().getPos();
                Level level = spellData.getLatestPosition().getLevel();
                spellData.purgePositions(1);
                spellData = result(caster, spellData, position, level);
            }
            else spellData.purgePositions(1);
        }
        else spellData.setValid(false);
        return spellData;
    }

    protected SpellData result(Entity caster, SpellData spellData, Vec3 pos, Level level) {
        return spellData;
    }
}
